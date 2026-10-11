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

    // AUTH
    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req) { return ResponseEntity.ok(service.login(req)); }

    // ITEMS
    @PostMapping("/items")
    public ResponseEntity<Item> createItem(@Valid @RequestBody ItemRequest req) { return new ResponseEntity<>(service.createItem(req), HttpStatus.CREATED); }

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

    @GetMapping("/listings/{id}/history")
    public ResponseEntity<List<PriceHistory>> getHistory(@PathVariable Long id) { return ResponseEntity.ok(service.getPriceHistory(id)); }

    @GetMapping("/listings/{id}/bids")
    public ResponseEntity<List<Bid>> getBids(@PathVariable Long id) { return ResponseEntity.ok(service.getBids(id)); }

    @GetMapping("/listings/{id}/compare")
    public ResponseEntity<List<ListingComparisonDTO>> compareSellers(@PathVariable Long id) { return ResponseEntity.ok(service.getOtherSellersComparison(id)); }

    // ADMIN
    @GetMapping("/admin/stats")
    public ResponseEntity<DashboardStatsDTO> getStats() { return ResponseEntity.ok(service.getStats()); }

    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getAdminUsers() { return ResponseEntity.ok(service.getAllUsers()); }
}
