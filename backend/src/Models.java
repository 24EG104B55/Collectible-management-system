package com.raretrack;

import jakarta.persistence.*;
import java.time.LocalDateTime;

public class Models {}

enum Role { BUYER, SELLER, ADMIN }
enum ListingType { AUCTION, FIXED_PRICE }
enum ListingStatus { ACTIVE, SOLD, EXPIRED }

@Entity
@Table(name = "users")
class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    @Column(nullable = false)
    private String password;
    private String fullName;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDateTime createdAt = LocalDateTime.now();

    public User() {}
    public User(String username, String email, String password, String fullName, Role role) {
        this.username = username; this.email = email; this.password = password; this.fullName = fullName; this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

@Entity
@Table(name = "items")
class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "item_condition", nullable = false)
    private String condition;
    @Column(name = "rarity_level", nullable = false)
    private String rarityLevel;
    @Column(name = "part_number", nullable = false, unique = true)
    private String partNumber;
    @Column(name = "stock_qty", nullable = false)
    private Integer stockQty = 1;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Item() {}
    public Item(String name, String description, String condition, String rarityLevel, String partNumber, Integer stockQty) {
        this.name = name; this.description = description; this.condition = condition;
        this.rarityLevel = rarityLevel; this.partNumber = partNumber; this.stockQty = stockQty;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    public String getRarityLevel() { return rarityLevel; }
    public void setRarityLevel(String rarityLevel) { this.rarityLevel = rarityLevel; }
    public String getPartNumber() { return partNumber; }
    public void setPartNumber(String partNumber) { this.partNumber = partNumber; }
    public Integer getStockQty() { return stockQty; }
    public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

@Entity
@Table(name = "listings")
class Listing {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;
    @Enumerated(EnumType.STRING)
    private ListingType listingType;
    @Column(nullable = false)
    private Double currentPrice;
    private Double startingBid;
    private LocalDateTime auctionEndTime;
    @Enumerated(EnumType.STRING)
    private ListingStatus status = ListingStatus.ACTIVE;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Listing() {}
    public Listing(Item item, User seller, ListingType listingType, Double currentPrice, Double startingBid, LocalDateTime auctionEndTime) {
        this.item = item; this.seller = seller; this.listingType = listingType;
        this.currentPrice = currentPrice; this.startingBid = startingBid; this.auctionEndTime = auctionEndTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
    public User getSeller() { return seller; }
    public void setSeller(User seller) { this.seller = seller; }
    public ListingType getListingType() { return listingType; }
    public void setListingType(ListingType listingType) { this.listingType = listingType; }
    public Double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(Double currentPrice) { this.currentPrice = currentPrice; }
    public Double getStartingBid() { return startingBid; }
    public void setStartingBid(Double startingBid) { this.startingBid = startingBid; }
    public LocalDateTime getAuctionEndTime() { return auctionEndTime; }
    public void setAuctionEndTime(LocalDateTime auctionEndTime) { this.auctionEndTime = auctionEndTime; }
    public ListingStatus getStatus() { return status; }
    public void setStatus(ListingStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

@Entity
@Table(name = "bids")
class Bid {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bidder_id", nullable = false)
    private User bidder;
    @Column(nullable = false)
    private Double bidAmount;
    private LocalDateTime bidTime = LocalDateTime.now();

    public Bid() {}
    public Bid(Listing listing, User bidder, Double bidAmount) {
        this.listing = listing; this.bidder = bidder; this.bidAmount = bidAmount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Listing getListing() { return listing; }
    public void setListing(Listing listing) { this.listing = listing; }
    public User getBidder() { return bidder; }
    public void setBidder(User bidder) { this.bidder = bidder; }
    public Double getBidAmount() { return bidAmount; }
    public void setBidAmount(Double bidAmount) { this.bidAmount = bidAmount; }
    public LocalDateTime getBidTime() { return bidTime; }
    public void setBidTime(LocalDateTime bidTime) { this.bidTime = bidTime; }
}

@Entity
@Table(name = "price_history")
class PriceHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;
    @Column(nullable = false)
    private Double price;
    private String eventType = "BID";
    private LocalDateTime recordedAt = LocalDateTime.now();

    public PriceHistory() {}
    public PriceHistory(Listing listing, Double price, String eventType, LocalDateTime recordedAt) {
        this.listing = listing; this.price = price; this.eventType = eventType;
        this.recordedAt = recordedAt != null ? recordedAt : LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Listing getListing() { return listing; }
    public void setListing(Listing listing) { this.listing = listing; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}
