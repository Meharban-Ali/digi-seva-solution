package com.digisevasolution.dto.request;

import com.digisevasolution.entity.EnquiryStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateEnquiryStatusRequest {

    @NotNull(message = "Enquiry status is required (NEW, CONTACTED, or RESOLVED)")
    private EnquiryStatus status;

    public UpdateEnquiryStatusRequest() {
    }

    public UpdateEnquiryStatusRequest(EnquiryStatus status) {
        this.status = status;
    }

    public EnquiryStatus getStatus() {
        return status;
    }

    public void setStatus(EnquiryStatus status) {
        this.status = status;
    }
}
