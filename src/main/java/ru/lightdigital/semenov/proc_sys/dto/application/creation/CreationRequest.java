package ru.lightdigital.semenov.proc_sys.dto.application.creation;

import lombok.Builder;

@Builder
public record CreationRequest (
        String message
) { }
