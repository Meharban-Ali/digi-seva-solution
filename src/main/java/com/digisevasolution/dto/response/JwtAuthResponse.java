package com.digisevasolution.dto.response;

public class JwtAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private long expiresInMs;
    private AdminUserDto user;

    public JwtAuthResponse() {
    }

    public JwtAuthResponse(String accessToken, long expiresInMs, AdminUserDto user) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
        this.expiresInMs = expiresInMs;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public long getExpiresInMs() {
        return expiresInMs;
    }

    public void setExpiresInMs(long expiresInMs) {
        this.expiresInMs = expiresInMs;
    }

    public AdminUserDto getUser() {
        return user;
    }

    public void setUser(AdminUserDto user) {
        this.user = user;
    }
}
