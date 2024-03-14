package ru.lightdigital.semenov.proc_sys.dto.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import ru.lightdigital.semenov.proc_sys.db.application.ApplicationStatus;

import java.time.LocalDateTime;

@Builder
public record ApplicationDto (
    long id,
    ApplicationStatus status,
    String message,
    @JsonProperty("creation_date")
    LocalDateTime creationDate,
    @JsonProperty("first_name")
    String firstName,
    @JsonProperty("last_name")
    String lastName,
    @JsonProperty("phone_number")
    String phoneNumber
) { }
