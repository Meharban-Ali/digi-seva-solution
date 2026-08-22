package com.digisevasolution.service;

import com.digisevasolution.dto.request.ChangePasswordRequest;
import com.digisevasolution.dto.request.LoginRequest;
import com.digisevasolution.dto.request.VerifyOtpRequest;
import com.digisevasolution.dto.response.AdminUserDto;
import com.digisevasolution.dto.response.JwtAuthResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    void initiateLogin(LoginRequest loginRequest);
    JwtAuthResponse verifyOtpAndLogin(VerifyOtpRequest verifyOtpRequest);
    void changePassword(String currentUserEmail, ChangePasswordRequest changePasswordRequest);
    AdminUserDto updateProfileAvatar(String currentUserEmail, MultipartFile file);
    AdminUserDto updateProfile(String currentUserEmail, String fullName, String profileImageUrl);
}
