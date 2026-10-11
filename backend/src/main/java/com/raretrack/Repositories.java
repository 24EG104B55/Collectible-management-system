package com.raretrack;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public class Repositories {}

@Repository
interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

@Repository
interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByPartNumber(String partNumber);
    List<Item> findByStockQtyLessThanEqual(Integer threshold);

    @Query("SELECT i FROM Item i WHERE " +
           "(:query IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(i.partNumber) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:rarity IS NULL OR i.rarityLevel = :rarity)")
    List<Item> searchItems(@Param("query") String query,
                           @Param("rarity") String rarity);
}

@Repository
interface ListingRepository extends JpaRepository<Listing, Long> {
    List<Listing> findByStatus(ListingStatus status);
    List<Listing> findByItemId(Long itemId);
    List<Listing> findByItemIdAndStatus(Long itemId, ListingStatus status);
    List<Listing> findBySellerId(Long sellerId);
}

@Repository
interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByListingIdOrderByBidTimeDesc(Long listingId);
    List<Bid> findByBidderIdOrderByBidTimeDesc(Long bidderId);
}

@Repository
interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    List<PriceHistory> findByListingIdOrderByRecordedAtAsc(Long listingId);
}

@Repository
interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    List<StockTransaction> findByItemIdOrderByTransactionDateDesc(Long itemId);
}
