package com.raretrack;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RareTrackController {

    private final RareTrackService service;

    public RareTrackController(RareTrackService service) { this.service = service; }

    @GetMapping("/")
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("Collectible Items Management System API is running!");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    // AUTH
    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req) { return ResponseEntity.ok(service.login(req)); }

    @PostMapping("/auth/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest req) { return ResponseEntity.ok(service.register(req)); }

    @GetMapping("/auth/users")
    public ResponseEntity<List<User>> getUsers() { return ResponseEntity.ok(service.getAllUsers()); }

    // ITEMS
    @GetMapping("/items")
    public ResponseEntity<List<Item>> getItems(@RequestParam(required = false) String query,
                                               @RequestParam(required = false) String rarity) {
        return ResponseEntity.ok(service.searchItems(query, rarity));
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<Item> getItem(@PathVariable Long id) { return ResponseEntity.ok(service.getItemById(id)); }

    @PostMapping("/items")
    public ResponseEntity<Item> createItem(@Valid @RequestBody ItemRequest req) { return new ResponseEntity<>(service.createItem(req), HttpStatus.CREATED); }

    @PutMapping("/items/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @Valid @RequestBody ItemRequest req) { return ResponseEntity.ok(service.updateItem(id, req)); }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) { service.deleteItem(id); return ResponseEntity.noContent().build(); }

    @GetMapping("/items/low-stock")
    public ResponseEntity<List<Item>> getLowStock(@RequestParam(defaultValue = "2") Integer threshold) { return ResponseEntity.ok(service.getLowStockItems(threshold)); }

    @GetMapping("/items/{id}/listings")
    public ResponseEntity<List<Listing>> getItemListings(@PathVariable Long id) { return ResponseEntity.ok(service.getListingsByItemId(id)); }

    // LISTINGS & MULTI-SELLER COMPARISON
    @GetMapping("/listings")
    public ResponseEntity<List<Listing>> getListings() { return ResponseEntity.ok(service.getAllActiveListings()); }

    @GetMapping("/listings/{id}")
    public ResponseEntity<Listing> getListing(@PathVariable Long id) { return ResponseEntity.ok(service.getListingById(id)); }

    @PostMapping("/listings")
    public ResponseEntity<Listing> createListing(@Valid @RequestBody ListingRequest req) { return new ResponseEntity<>(service.createListing(req), HttpStatus.CREATED); }

    @PostMapping("/listings/{id}/bid")
    public ResponseEntity<Bid> placeBid(@PathVariable Long id, @Valid @RequestBody BidRequest req) { return ResponseEntity.ok(service.placeBid(id, req)); }

    @PostMapping("/listings/{id}/buy")
    public ResponseEntity<Listing> buyNow(@PathVariable Long id, @RequestParam Long buyerId) { return ResponseEntity.ok(service.buyNow(id, buyerId)); }

    @PutMapping("/listings/{id}/close")
    public ResponseEntity<Listing> closeListing(@PathVariable Long id) { return ResponseEntity.ok(service.closeListing(id)); }

    @GetMapping("/listings/{id}/history")
    public ResponseEntity<List<PriceHistory>> getHistory(@PathVariable Long id) { return ResponseEntity.ok(service.getPriceHistory(id)); }

    @GetMapping("/listings/{id}/bids")
    public ResponseEntity<List<Bid>> getBids(@PathVariable Long id) { return ResponseEntity.ok(service.getBids(id)); }

    @GetMapping("/listings/{id}/compare")
    public ResponseEntity<List<ListingComparisonDTO>> compareSellers(@PathVariable Long id) { return ResponseEntity.ok(service.getOtherSellersComparison(id)); }

    @GetMapping("/listings/seller/{sellerId}")
    public ResponseEntity<List<Listing>> getSellerListings(@PathVariable Long sellerId) { return ResponseEntity.ok(service.getListingsBySellerId(sellerId)); }

    // ADMIN
    @GetMapping("/admin/stats")
    public ResponseEntity<DashboardStatsDTO> getStats() { return ResponseEntity.ok(service.getStats()); }

    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getAdminUsers() { return ResponseEntity.ok(service.getAllUsers()); }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<Void> deleteAdminUser(@PathVariable Long id) { service.deleteUser(id); return ResponseEntity.noContent().build(); }

    @PutMapping("/admin/listings/{id}/cancel")
    public ResponseEntity<Listing> cancelListing(@PathVariable Long id) { return ResponseEntity.ok(service.closeListing(id)); }
}
