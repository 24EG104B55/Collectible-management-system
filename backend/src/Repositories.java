package com.raretrack;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public class Repositories {}

@Repository
interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

@Repository
interface ItemRepository extends JpaRepository<Item, Long> {}

@Repository
interface ListingRepository extends JpaRepository<Listing, Long> {
    List<Listing> findByStatus(ListingStatus status);
    List<Listing> findByItemIdAndStatus(Long itemId, ListingStatus status);
}

@Repository
interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByListingIdOrderByBidTimeDesc(Long listingId);
}

@Repository
interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    List<PriceHistory> findByListingIdOrderByRecordedAtAsc(Long listingId);
}
