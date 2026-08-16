package com.digisevasolution.service.impl;

import com.digisevasolution.entity.OtpToken;
import com.digisevasolution.exception.InvalidOtpException;
import com.digisevasolution.exception.OtpExpiredException;
import com.digisevasolution.exception.OtpRateLimitException;
import com.digisevasolution.repository.OtpTokenRepository;
import com.digisevasolution.service.OtpService;
import com.digisevasolution.service.ResendEmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class OtpServiceImpl implements OtpService {

    private static final int OTP_EXPIRATION_MINUTES = 10;
    private static final int RATE_LIMIT_SECONDS = 60;

    private final OtpTokenRepository otpTokenRepository;
    private final ResendEmailService resendEmailService;
    private final SecureRandom secureRandom = new SecureRandom();

    public OtpServiceImpl(OtpTokenRepository otpTokenRepository, ResendEmailService resendEmailService) {
        this.otpTokenRepository = otpTokenRepository;
        this.resendEmailService = resendEmailService;
    }

    @Override
    @Transactional
    public void generateAndSendOtp(String email) {
        Instant now = Instant.now();

        // Enforce 60s rate limit on OTP generation per email using Instant
        Optional<OtpToken> latestOtp = otpTokenRepository.findTopByEmailOrderByCreatedAtDesc(email);
        if (latestOtp.isPresent()) {
            Instant nextAllowedTime = latestOtp.get().getCreatedAt().plusSeconds(RATE_LIMIT_SECONDS);
            if (now.isBefore(nextAllowedTime)) {
                long secondsToWait = Duration.between(now, nextAllowedTime).getSeconds();
                throw new OtpRateLimitException("Please wait " + secondsToWait + " seconds before requesting a new OTP.");
            }
        }

        // Generate 6-digit numeric OTP
        int otpInt = 100000 + secureRandom.nextInt(900000);
        String otpCode = String.valueOf(otpInt);

        Instant expiresAt = now.plus(OTP_EXPIRATION_MINUTES, ChronoUnit.MINUTES);

        OtpToken otpToken = new OtpToken(email, otpCode, expiresAt);
        otpTokenRepository.save(otpToken);

        // Deliver via Resend
        resendEmailService.sendOtpEmail(email, otpCode);
    }

    @Override
    @Transactional
    public void verifyOtp(String email, String otpCode) {
        Instant now = Instant.now();

        OtpToken otpToken = otpTokenRepository.findTopByEmailAndVerifiedFalseOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new InvalidOtpException("No active OTP found for this email. Please request a new OTP."));

        if (!otpToken.getOtpCode().equals(otpCode)) {
            throw new InvalidOtpException("Invalid OTP code. Please check and try again.");
        }

        if (now.isAfter(otpToken.getExpiresAt())) {
            throw new OtpExpiredException("OTP has expired. Please request a new OTP.");
        }

        // Mark OTP as verified
        otpToken.setVerified(true);
        otpTokenRepository.save(otpToken);
    }
}
