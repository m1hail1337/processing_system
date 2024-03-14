package ru.lightdigital.semenov.proc_sys.dto.application.creation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CreationResponse (
        @JsonProperty("application_id")
        long applicationId
) {
}
