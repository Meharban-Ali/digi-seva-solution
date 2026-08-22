package com.digisevasolution.service.impl;

import com.digisevasolution.dto.request.ChangePasswordRequest;
import com.digisevasolution.dto.request.LoginRequest;
import com.digisevasolution.dto.request.VerifyOtpRequest;
import com.digisevasolution.dto.response.AdminUserDto;
import com.digisevasolution.dto.response.JwtAuthResponse;
import com.digisevasolution.entity.AdminUser;
import com.digisevasolution.entity.MediaType;
import com.digisevasolution.exception.ApiException;
import com.digisevasolution.exception.InvalidCredentialsException;
import com.digisevasolution.exception.ResourceNotFoundException;
import com.digisevasolution.repository.AdminUserRepository;
import com.digisevasolution.service.AuthService;
import com.digisevasolution.service.CloudinaryService;
import com.digisevasolution.service.OtpService;
import com.digisevasolution.util.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final JwtTokenProvider tokenProvider;
    private final CloudinaryService cloudinaryService;

    public AuthServiceImpl(AdminUserRepository adminUserRepository,
                           PasswordEncoder passwordEncoder,
                           OtpService otpService,
                           JwtTokenProvider tokenProvider,
                           CloudinaryService cloudinaryService) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.tokenProvider = tokenProvider;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public void initiateLogin(LoginRequest loginRequest) {
        AdminUser adminUser = adminUserRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), adminUser.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Generate 6-digit OTP and send via email
        otpService.generateAndSendOtp(adminUser.getEmail());
    }

    @Override
    @Transactional
    public JwtAuthResponse verifyOtpAndLogin(VerifyOtpRequest verifyOtpRequest) {
        AdminUser adminUser = adminUserRepository.findByEmail(verifyOtpRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email address"));

        // Verify OTP code
        otpService.verifyOtp(verifyOtpRequest.getEmail(), verifyOtpRequest.getOtpCode());

        // Generate JWT Token
        String token = tokenProvider.generateTokenForEmail(adminUser.getEmail());
        long expirationMs = tokenProvider.getJwtExpirationInMs();

        AdminUserDto userDto = new AdminUserDto(
                adminUser.getId(),
                adminUser.getEmail(),
                adminUser.getFullName(),
                adminUser.getProfileImageUrl(),
                adminUser.isFirstLogin()
        );

        return new JwtAuthResponse(token, expirationMs, userDto);
    }

    @Override
    @Transactional
    public void changePassword(String currentUserEmail, ChangePasswordRequest changePasswordRequest) {
        AdminUser adminUser = adminUserRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("AdminUser", "email", currentUserEmail));

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), adminUser.getPasswordHash())) {
            throw new InvalidCredentialsException("Current password does not match");
        }

        // Update to new encoded password and set isFirstLogin = false
        adminUser.setPasswordHash(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        adminUser.setFirstLogin(false);
        adminUserRepository.save(adminUser);
    }

    @Override
    @Transactional
    public AdminUserDto updateProfileAvatar(String currentUserEmail, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Profile image file cannot be empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only image files (JPEG, PNG, WebP) are allowed for profile avatar");
        }

        AdminUser adminUser = adminUserRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("AdminUser", "email", currentUserEmail));

        Map<String, Object> uploadResult = cloudinaryService.uploadFile(file, MediaType.IMAGE);
        String imageUrl = (String) uploadResult.get("secure_url");

        adminUser.setProfileImageUrl(imageUrl);
        AdminUser saved = adminUserRepository.save(adminUser);

        return new AdminUserDto(
                saved.getId(),
                saved.getEmail(),
                saved.getFullName(),
                saved.getProfileImageUrl(),
                saved.isFirstLogin()
        );
    }

    @Override
    @Transactional
    public AdminUserDto updateProfile(String currentUserEmail, String fullName, String profileImageUrl) {
        AdminUser adminUser = adminUserRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("AdminUser", "email", currentUserEmail));

        if (fullName != null && !fullName.isBlank()) {
            adminUser.setFullName(fullName.trim());
        }

        if (profileImageUrl != null) {
            adminUser.setProfileImageUrl(profileImageUrl.trim());
        }

        AdminUser saved = adminUserRepository.save(adminUser);

        return new AdminUserDto(
                saved.getId(),
                saved.getEmail(),
                saved.getFullName(),
                saved.getProfileImageUrl(),
                saved.isFirstLogin()
        );
    }
}
