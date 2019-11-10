package ru.timmson.auth.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckPhoneNumberResponse implements AuthServiceResponse {

    private Boolean available;

    private String status;

    private Long timeToUnblocked;

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTimeToUnblocked() {
        return timeToUnblocked;
    }

    public void setTimeToUnblocked(Long timeToUnblocked) {
        this.timeToUnblocked = timeToUnblocked;
    }
}
