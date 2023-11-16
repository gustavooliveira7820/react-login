package com.gusta.lever.repository;

import com.gusta.lever.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleManagerRepository extends JpaRepository<Product, UUID> {
    Optional<String> findCompanyEmailByUuid(UUID uuid);

    Page<Product> findByScheduledDate(Pageable pageable, LocalDate scheduledDate);

    Page<Product> findByScheduledDateAndCollected(Pageable pageable, LocalDate scheduledDate, Boolean collected);

    @Modifying
    @Query("UPDATE Product p SET p.scheduledDate = :newValue WHERE p.uuid = :productId")
    void updateScheduledDate(@Param("productId") UUID productId, @Param("newValue") LocalDate newValue);

    @Modifying
    @Query("UPDATE Product p SET p.collected = true WHERE p.uuid = :productId")
    void updateCollectedStatus(@Param("productId") UUID productId);
}
