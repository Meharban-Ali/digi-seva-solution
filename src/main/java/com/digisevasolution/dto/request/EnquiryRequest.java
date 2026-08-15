package com.digisevasolution.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EnquiryRequest {

    @NotBlank(message = "Customer name is required")
    private String name;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$|^\\+?[1-9]\\d{1,14}$", message = "Please provide a valid phone number (10-digit mobile or international format)")
    private String phone;

    @Email(message = "Please provide a valid email address format")
    private String email;

    private Long serviceId;

    private String message;

    public EnquiryRequest() {
    }

    public EnquiryRequest(String name, String phone, String email, Long serviceId, String message) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.serviceId = serviceId;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
