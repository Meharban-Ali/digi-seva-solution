package com.digisevasolution.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdminUserDto {
    private Long id;
    private String email;
    private String fullName;
    private String profileImageUrl;

    @JsonProperty("isFirstLogin")
    private boolean isFirstLogin;

    public AdminUserDto() {
    }

    public AdminUserDto(Long id, String email, String fullName, boolean isFirstLogin) {
        this(id, email, fullName, null, isFirstLogin);
    }

    public AdminUserDto(Long id, String email, String fullName, String profileImageUrl, boolean isFirstLogin) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.profileImageUrl = profileImageUrl;
        this.isFirstLogin = isFirstLogin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @JsonProperty("isFirstLogin")
    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
    }
}
