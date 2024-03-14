package ru.lightdigital.semenov.proc_sys.dto.client;

import lombok.Builder;
import ru.lightdigital.semenov.proc_sys.db.client.Role;

import java.util.Set;

@Builder
public record ClientDto (
        String id,
        String firstName,
        String lastName,
        String phoneNumber,
        Set<Role> roles

) { }
