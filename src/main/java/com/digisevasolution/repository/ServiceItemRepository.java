package com.digisevasolution.repository;

import com.digisevasolution.entity.ServiceCategory;
import com.digisevasolution.entity.ServiceItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
    Page<ServiceItem> findAll(Pageable pageable);
    List<ServiceItem> findByIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc();
    List<ServiceItem> findByIsActiveTrueAndCategoryOrderByDisplayOrderAscCreatedAtDesc(ServiceCategory category);
    List<ServiceItem> findByIsActiveTrueAndIsFeaturedTrueOrderByDisplayOrderAscCreatedAtDesc();
    List<ServiceItem> findByIsActiveTrueAndCategoryAndIsFeaturedTrueOrderByDisplayOrderAscCreatedAtDesc(ServiceCategory category);
    Optional<ServiceItem> findByIdAndIsActiveTrue(Long id);
}
