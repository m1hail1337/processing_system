package ru.lightdigital.semenov.proc_sys.dto.application.status;

import ru.lightdigital.semenov.proc_sys.db.application.ApplicationStatus;

public record StatusChangingRequest (
        ApplicationStatus status
) { }
