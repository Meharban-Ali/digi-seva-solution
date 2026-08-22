package com.digisevasolution.controller;

import com.digisevasolution.dto.request.ChangePasswordRequest;
import com.digisevasolution.dto.request.LoginRequest;
import com.digisevasolution.dto.request.VerifyOtpRequest;
import com.digisevasolution.dto.response.AdminUserDto;
import com.digisevasolution.dto.response.ApiResponse;
import com.digisevasolution.dto.response.JwtAuthResponse;
import com.digisevasolution.security.CustomUserDetails;
import com.digisevasolution.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/auth")
@Tag(name = "Admin Auth Module", description = "Endpoints for partner login, OTP verification, password, and profile management")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Admin Step 1 Login", description = "Validate email and password credentials, then generate and send a 6-digit OTP to the admin's email.")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        authService.initiateLogin(loginRequest);
        ApiResponse<String> response = ApiResponse.success("OTP sent to your email. Please verify to complete login.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Admin Step 2 OTP Verification", description = "Verify the 6-digit OTP sent to email and return a signed Bearer JWT token.")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> verifyOtp(@Valid @RequestBody VerifyOtpRequest verifyOtpRequest) {
        JwtAuthResponse authResponse = authService.verifyOtpAndLogin(verifyOtpRequest);
        ApiResponse<JwtAuthResponse> response = ApiResponse.success("OTP verified successfully. Login completed.", authResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    @Operation(summary = "Admin Password Update", description = "Update the admin password and clear the temporary password flag. Requires valid JWT.")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        authService.changePassword(email, changePasswordRequest);
        ApiResponse<String> response = ApiResponse.success("Password changed successfully.");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/profile/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload Profile Picture Avatar", description = "Upload profile picture image file to Cloudinary and update admin user profile. Requires valid JWT.")
    public ResponseEntity<ApiResponse<AdminUserDto>> uploadProfileAvatar(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        AdminUserDto updatedUser = authService.updateProfileAvatar(email, file);
        ApiResponse<AdminUserDto> response = ApiResponse.success("Profile picture updated successfully.", updatedUser);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    @Operation(summary = "Update Profile Details", description = "Update admin profile full name or profile image URL. Requires valid JWT.")
    public ResponseEntity<ApiResponse<AdminUserDto>> updateProfile(
            @RequestBody Map<String, String> requestBody,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        String fullName = requestBody.get("fullName");
        String profileImageUrl = requestBody.get("profileImageUrl");
        AdminUserDto updatedUser = authService.updateProfile(email, fullName, profileImageUrl);
        ApiResponse<AdminUserDto> response = ApiResponse.success("Profile details updated successfully.", updatedUser);
        return ResponseEntity.ok(response);
    }
}
