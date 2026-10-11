package com.raretrack;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RareTrackService {

    private final UserRepository userRepo;
    private final ItemRepository itemRepo;
    private final ListingRepository listingRepo;
    private final BidRepository bidRepo;
    private final PriceHistoryRepository priceRepo;
    private final StockTransactionRepository stockRepo;

    public RareTrackService(UserRepository userRepo, ItemRepository itemRepo, ListingRepository listingRepo,
                            BidRepository bidRepo, PriceHistoryRepository priceRepo, StockTransactionRepository stockRepo) {
        this.userRepo = userRepo; this.itemRepo = itemRepo; this.listingRepo = listingRepo;
        this.bidRepo = bidRepo; this.priceRepo = priceRepo; this.stockRepo = stockRepo;
    }

    // AUTH
    public AuthResponse login(AuthRequest req) {
        User user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + req.getUsername()));
        if (!user.getPassword().equals(req.getPassword())) throw new InvalidBidException("Invalid credentials");
        return new AuthResponse(user.getId(), user.getUsername(), user.getEmail(), user.getFullName(), user.getRole(), "mock-jwt-" + user.getId());
    }

    public AuthResponse register(AuthRequest req) {
        if (userRepo.existsByUsername(req.getUsername())) throw new InvalidBidException("Username is taken");
        User user = userRepo.save(new User(
                req.getUsername(),
                req.getEmail() != null ? req.getEmail() : req.getUsername() + "@raretrack.com",
                req.getPassword(),
                req.getFullName() != null ? req.getFullName() : req.getUsername(),
                req.getRole() != null ? req.getRole() : Role.BUYER
        ));
        return new AuthResponse(user.getId(), user.getUsername(), user.getEmail(), user.getFullName(), user.getRole(), "mock-jwt-" + user.getId());
    }

    public List<User> getAllUsers() { return userRepo.findAll(); }
    public void deleteUser(Long id) { userRepo.deleteById(id); }

    // ITEMS
    public List<Item> searchItems(String q, String r) {
        String query = (q != null && !q.isBlank()) ? q.trim() : null;
        String rarity = (r != null && !r.isBlank() && !r.equalsIgnoreCase("ALL")) ? r.trim() : null;
        return itemRepo.searchItems(query, rarity);
    }

    public Item getItemById(Long id) {
        return itemRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item not found: " + id));
    }

    @Transactional
    public Item createItem(ItemRequest req) {
        Item item = itemRepo.save(new Item(req.getName(), req.getDescription(), req.getCondition(),
                req.getRarityLevel(), req.getPartNumber(), req.getStockQty()));
        stockRepo.save(new StockTransaction(item, StockTransaction.Type.IN, item.getStockQty(), "Initial intake"));
        return item;
    }

    public Item updateItem(Long id, ItemRequest req) {
        Item item = getItemById(id);
        item.setName(req.getName()); item.setDescription(req.getDescription());
        item.setCondition(req.getCondition()); item.setRarityLevel(req.getRarityLevel()); item.setPartNumber(req.getPartNumber());
        item.setStockQty(req.getStockQty());
        return itemRepo.save(item);
    }

    public void deleteItem(Long id) { itemRepo.deleteById(id); }
    public List<Item> getLowStockItems(Integer threshold) { return itemRepo.findByStockQtyLessThanEqual(threshold != null ? threshold : 2); }

    // LISTINGS & SELLER COMPARISON
    public List<Listing> getAllActiveListings() { return listingRepo.findByStatus(ListingStatus.ACTIVE); }
    public Listing getListingById(Long id) {
        return listingRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Listing not found: " + id));
    }
    public List<Listing> getListingsByItemId(Long itemId) { return listingRepo.findByItemId(itemId); }
    public List<Listing> getListingsBySellerId(Long sellerId) { return listingRepo.findBySellerId(sellerId); }

    @Transactional
    public Listing createListing(ListingRequest req) {
        Item item = getItemById(req.getItemId());
        User seller = userRepo.findById(req.getSellerId()).orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
        if (item.getStockQty() <= 0) throw new OutOfStockException("Item out of stock");

        LocalDateTime end = req.getAuctionEndTime();
        if (req.getListingType() == ListingType.AUCTION && end == null) end = LocalDateTime.now().plusDays(7);
        Double starting = req.getStartingBid() != null ? req.getStartingBid() : req.getCurrentPrice();

        Listing listing = listingRepo.save(new Listing(item, seller, req.getListingType(), req.getCurrentPrice(), starting, end));
        priceRepo.save(new PriceHistory(listing, listing.getCurrentPrice(), "CREATED", LocalDateTime.now()));
        return listing;
    }

    @Transactional
    public Listing buyNow(Long id, Long buyerId) {
        Listing listing = getListingById(id);
        if (listing.getStatus() != ListingStatus.ACTIVE) throw new OutOfStockException("Listing not active");
        Item item = listing.getItem();
        if (item.getStockQty() <= 0) throw new OutOfStockException("Out of stock");

        item.setStockQty(item.getStockQty() - 1);
        itemRepo.save(item);
        stockRepo.save(new StockTransaction(item, StockTransaction.Type.SOLD, 1, "Purchased by buyer " + buyerId));

        listing.setStatus(ListingStatus.SOLD);
        return listingRepo.save(listing);
    }

    public Listing closeListing(Long id) {
        Listing listing = getListingById(id);
        listing.setStatus(ListingStatus.EXPIRED);
        return listingRepo.save(listing);
    }

    public List<ListingComparisonDTO> getOtherSellersComparison(Long id) {
        Listing curr = getListingById(id);
        List<Listing> list = listingRepo.findByItemIdAndStatus(curr.getItem().getId(), ListingStatus.ACTIVE);
        if (list.size() <= 1) {
            // Also include any other active listings
            for (Listing other : listingRepo.findByStatus(ListingStatus.ACTIVE)) {
                if (list.stream().noneMatch(l -> l.getId().equals(other.getId()))) list.add(other);
            }
        }
        if (list.isEmpty()) return new ArrayList<>();

        double min = list.stream().mapToDouble(Listing::getCurrentPrice).min().orElse(curr.getCurrentPrice());
        return list.stream().sorted(Comparator.comparingDouble(Listing::getCurrentPrice)).map(l -> {
            ListingComparisonDTO dto = new ListingComparisonDTO();
            dto.setListingId(l.getId());
            dto.setSellerId(l.getSeller().getId());
            dto.setSellerName(l.getSeller().getFullName() != null ? l.getSeller().getFullName() : l.getSeller().getUsername());
            dto.setSellerEmail(l.getSeller().getEmail());
            dto.setPrice(l.getCurrentPrice());
            dto.setListingType(l.getListingType());
            dto.setItemCondition(l.getItem().getCondition());
            dto.setStockQty(l.getItem().getStockQty());
            dto.setBestPrice(Math.abs(l.getCurrentPrice() - min) < 0.001);
            dto.setPriceDifferenceFromBest(Math.round((l.getCurrentPrice() - min) * 100.0) / 100.0);
            return dto;
        }).collect(Collectors.toList());
    }

    // BIDS & PRICE TRACKING
    @Transactional
    public Bid placeBid(Long id, BidRequest req) {
        Listing listing = getListingById(id);
        if (listing.getStatus() != ListingStatus.ACTIVE) throw new ListingExpiredException("Auction closed");
        if (listing.getListingType() != ListingType.AUCTION) throw new InvalidBidException("Only AUCTION listings accept bids");
        if (listing.getAuctionEndTime() != null && LocalDateTime.now().isAfter(listing.getAuctionEndTime())) {
            listing.setStatus(ListingStatus.EXPIRED);
            listingRepo.save(listing);
            throw new ListingExpiredException("Auction ended");
        }
        User bidder = userRepo.findById(req.getBidderId()).orElseThrow(() -> new ResourceNotFoundException("Bidder not found"));
        if (listing.getSeller().getId().equals(bidder.getId())) throw new InvalidBidException("Cannot bid on own listing");
        if (req.getBidAmount() <= listing.getCurrentPrice()) throw new InvalidBidException("Bid must exceed $" + listing.getCurrentPrice());

        Bid bid = bidRepo.save(new Bid(listing, bidder, req.getBidAmount()));
        listing.setCurrentPrice(req.getBidAmount());
        listingRepo.save(listing);
        priceRepo.save(new PriceHistory(listing, req.getBidAmount(), "BID by " + bidder.getUsername(), LocalDateTime.now()));
        return bid;
    }

    public List<Bid> getBids(Long id) { return bidRepo.findByListingIdOrderByBidTimeDesc(id); }
    public List<PriceHistory> getPriceHistory(Long id) { return priceRepo.findByListingIdOrderByRecordedAtAsc(id); }

    // ADMIN TELEMETRY
    public DashboardStatsDTO getStats() {
        List<Listing> active = listingRepo.findByStatus(ListingStatus.ACTIVE);
        double volume = active.stream().mapToDouble(Listing::getCurrentPrice).sum();
        return new DashboardStatsDTO(itemRepo.count(), active.size(), bidRepo.count(), userRepo.count(), Math.round(volume * 100.0) / 100.0);
    }
}
