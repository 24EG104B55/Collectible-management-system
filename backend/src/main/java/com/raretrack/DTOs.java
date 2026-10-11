package com.raretrack;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public class DTOs {}

class AuthRequest {
    @NotBlank private String username;
    @NotBlank private String password;
    private String email;
    private String fullName;
    private Role role = Role.BUYER;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}

class AuthResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Role role;
    private String token;

    public AuthResponse(Long id, String username, String email, String fullName, Role role, String token) {
        this.id = id; this.username = username; this.email = email; this.fullName = fullName; this.role = role; this.token = token;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public Role getRole() { return role; }
    public String getToken() { return token; }
}

class BidRequest {
    @NotNull private Long bidderId;
    @NotNull @Positive private Double bidAmount;

    public Long getBidderId() { return bidderId; }
    public void setBidderId(Long bidderId) { this.bidderId = bidderId; }
    public Double getBidAmount() { return bidAmount; }
    public void setBidAmount(Double bidAmount) { this.bidAmount = bidAmount; }
}

class ItemRequest {
    @NotBlank private String name;
    private String description;
    @NotBlank private String condition;
    @NotBlank private String rarityLevel;
    @NotBlank private String partNumber;
    @NotNull @Min(1) private Integer stockQty;

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
}

class ListingRequest {
    @NotNull private Long itemId;
    @NotNull private Long sellerId;
    @NotNull private ListingType listingType;
    @NotNull @Positive private Double currentPrice;
    private Double startingBid;
    private LocalDateTime auctionEndTime;

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }
    public ListingType getListingType() { return listingType; }
    public void setListingType(ListingType listingType) { this.listingType = listingType; }
    public Double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(Double currentPrice) { this.currentPrice = currentPrice; }
    public Double getStartingBid() { return startingBid; }
    public void setStartingBid(Double startingBid) { this.startingBid = startingBid; }
    public LocalDateTime getAuctionEndTime() { return auctionEndTime; }
    public void setAuctionEndTime(LocalDateTime auctionEndTime) { this.auctionEndTime = auctionEndTime; }
}

class ListingComparisonDTO {
    private Long listingId;
    private Long sellerId;
    private String sellerName;
    private String sellerEmail;
    private Double price;
    private ListingType listingType;
    private String itemCondition;
    private Integer stockQty;
    private boolean bestPrice;
    private Double priceDifferenceFromBest;

    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }
    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }
    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }
    public String getSellerEmail() { return sellerEmail; }
    public void setSellerEmail(String sellerEmail) { this.sellerEmail = sellerEmail; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public ListingType getListingType() { return listingType; }
    public void setListingType(ListingType listingType) { this.listingType = listingType; }
    public String getItemCondition() { return itemCondition; }
    public void setItemCondition(String itemCondition) { this.itemCondition = itemCondition; }
    public Integer getStockQty() { return stockQty; }
    public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }
    public boolean isBestPrice() { return bestPrice; }
    public void setBestPrice(boolean bestPrice) { this.bestPrice = bestPrice; }
    public Double getPriceDifferenceFromBest() { return priceDifferenceFromBest; }
    public void setPriceDifferenceFromBest(Double priceDifferenceFromBest) { this.priceDifferenceFromBest = priceDifferenceFromBest; }
}

class DashboardStatsDTO {
    private long totalItems;
    private long activeListings;
    private long totalBids;
    private long totalUsers;
    private double totalTradingVolume;

    public DashboardStatsDTO(long totalItems, long activeListings, long totalBids, long totalUsers, double totalTradingVolume) {
        this.totalItems = totalItems; this.activeListings = activeListings; this.totalBids = totalBids;
        this.totalUsers = totalUsers; this.totalTradingVolume = totalTradingVolume;
    }

    public long getTotalItems() { return totalItems; }
    public long getActiveListings() { return activeListings; }
    public long getTotalBids() { return totalBids; }
    public long getTotalUsers() { return totalUsers; }
    public double getTotalTradingVolume() { return totalTradingVolume; }
}
