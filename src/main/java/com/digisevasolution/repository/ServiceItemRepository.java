package com.digisevasolution.repository;

import com.digisevasolution.entity.DeliveryMode;
import com.digisevasolution.entity.ServiceItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {

    @EntityGraph(attributePaths = {"category"})
    Page<ServiceItem> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"category"})
    List<ServiceItem> findByIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc();

    @EntityGraph(attributePaths = {"category"})
    List<ServiceItem> findByIsActiveTrueAndDeliveryModeOrderByDisplayOrderAscCreatedAtDesc(DeliveryMode deliveryMode);

    @EntityGraph(attributePaths = {"category"})
    List<ServiceItem> findByIsActiveTrueAndIsFeaturedTrueOrderByDisplayOrderAscCreatedAtDesc();

    @EntityGraph(attributePaths = {"category"})
    List<ServiceItem> findByIsActiveTrueAndDeliveryModeAndIsFeaturedTrueOrderByDisplayOrderAscCreatedAtDesc(DeliveryMode deliveryMode);

    @EntityGraph(attributePaths = {"category"})
    Optional<ServiceItem> findByIdAndIsActiveTrue(Long id);

    long countByCategoryId(Long categoryId);

    @EntityGraph(attributePaths = {"category"})
    List<ServiceItem> findByIsActiveTrueAndCategoryIdOrderByDisplayOrderAscCreatedAtDesc(Long categoryId);

    @EntityGraph(attributePaths = {"category"})
    List<ServiceItem> findByIsActiveTrueAndCategorySlugOrderByDisplayOrderAscCreatedAtDesc(String categorySlug);

    @EntityGraph(attributePaths = {"category"})
    List<ServiceItem> findByIsActiveTrueAndCategoryIdAndDeliveryModeOrderByDisplayOrderAscCreatedAtDesc(Long categoryId, DeliveryMode deliveryMode);

    @EntityGraph(attributePaths = {"category"})
    List<ServiceItem> findByIsActiveTrueAndCategorySlugAndDeliveryModeOrderByDisplayOrderAscCreatedAtDesc(String categorySlug, DeliveryMode deliveryMode);
}

