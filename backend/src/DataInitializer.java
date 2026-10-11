package com.raretrack;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final ItemRepository itemRepo;
    private final ListingRepository listingRepo;
    private final BidRepository bidRepo;
    private final PriceHistoryRepository priceRepo;

    public DataInitializer(UserRepository userRepo, ItemRepository itemRepo, ListingRepository listingRepo,
                           BidRepository bidRepo, PriceHistoryRepository priceRepo) {
        this.userRepo = userRepo; this.itemRepo = itemRepo; this.listingRepo = listingRepo;
        this.bidRepo = bidRepo; this.priceRepo = priceRepo;
    }

    @Override
    public void run(String... args) {
        if (userRepo.count() > 0) return;

        User admin = userRepo.save(new User("admin", "admin@raretrack.com", "admin123", "System Administrator", Role.ADMIN));
        User seller1 = userRepo.save(new User("vintage_vault", "vault@antiques.com", "seller123", "Vintage Vault Spares", Role.SELLER));
        User seller2 = userRepo.save(new User("gearhead_relics", "gearhead@classicparts.com", "seller123", "Gearhead Relics", Role.SELLER));
        User buyer1 = userRepo.save(new User("collector_dan", "dan@gmail.com", "buyer123", "Daniel Sterling", Role.BUYER));
        User buyer2 = userRepo.save(new User("sophia_bids", "sophia@yahoo.com", "buyer123", "Sophia Vance", Role.BUYER));

        Item item1 = itemRepo.save(new Item("1968 Shelby GT500 Holley 4V Carburetor", "OEM Holley 4-barrel carburetor for 428 Cobra Jet.", "Restored", "Legendary", "SHELBY-CJ-428-1968", 2));
        Item item2 = itemRepo.save(new Item("Intel 8086 Gold Ceramic CPU (1978)", "Rare purple ceramic gold lid 16-bit microprocessor.", "NOS / Mint", "Ultra-Rare", "CPU-I8086-CERAMIC-01", 4));
        Item item3 = itemRepo.save(new Item("1923 Western Electric Brass Rotary Desk Phone", "Solid brass candlestick desk phone with rotary dial.", "Used / Excellent", "Rare", "TEL-WE102-BRASS-1923", 3));


        Listing listing1 = listingRepo.save(new Listing(item1, seller1, ListingType.AUCTION, 1450.0, 1200.0, LocalDateTime.now().plusDays(4)));
        Listing listing2 = listingRepo.save(new Listing(item1, seller2, ListingType.FIXED_PRICE, 1590.0, null, null));
        Listing listing3 = listingRepo.save(new Listing(item2, seller2, ListingType.AUCTION, 380.0, 250.0, LocalDateTime.now().plusDays(6)));
        Listing listing4 = listingRepo.save(new Listing(item2, seller1, ListingType.FIXED_PRICE, 340.0, null, null)); // Best Deal!
        Listing listing5 = listingRepo.save(new Listing(item3, seller1, ListingType.FIXED_PRICE, 320.0, null, null));

        priceRepo.save(new PriceHistory(listing1, 1200.0, "INITIAL_PRICE", LocalDateTime.now().minusDays(3)));
        bidRepo.save(new Bid(listing1, buyer1, 1280.0));
        priceRepo.save(new PriceHistory(listing1, 1280.0, "BID_PLACED", LocalDateTime.now().minusDays(2)));
        bidRepo.save(new Bid(listing1, buyer2, 1450.0));
        priceRepo.save(new PriceHistory(listing1, 1450.0, "BID_PLACED", LocalDateTime.now().minusHours(4)));

        priceRepo.save(new PriceHistory(listing2, 1590.0, "FIXED_PRICE", LocalDateTime.now().minusDays(2)));
        priceRepo.save(new PriceHistory(listing4, 340.0, "BEST_DEAL", LocalDateTime.now().minusDays(1)));
        priceRepo.save(new PriceHistory(listing5, 320.0, "FIXED_PRICE", LocalDateTime.now().minusDays(1)));

        System.out.println(">>> Demo data seeded successfully for Collectible Items Management System! <<<");
    }
}
