package com.digisevasolution.repository;

import com.digisevasolution.entity.Enquiry;
import com.digisevasolution.entity.EnquiryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
    Page<Enquiry> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Enquiry> findByStatusOrderByCreatedAtDesc(EnquiryStatus status, Pageable pageable);
    long countByPhoneAndCreatedAtAfter(String phone, LocalDateTime after);
}
