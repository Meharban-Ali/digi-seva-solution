package com.digisevasolution.repository;

import com.digisevasolution.entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findTopByEmailAndVerifiedFalseOrderByCreatedAtDesc(String email);
    Optional<OtpToken> findTopByEmailOrderByCreatedAtDesc(String email);
    void deleteByEmail(String email);
}
