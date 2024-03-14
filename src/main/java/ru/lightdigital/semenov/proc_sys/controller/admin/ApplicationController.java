package ru.lightdigital.semenov.proc_sys.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.lightdigital.semenov.proc_sys.dto.application.ApplicationDto;
import ru.lightdigital.semenov.proc_sys.service.application.ApplicationService;

import java.util.List;

@RequestMapping("/admin/applications")
@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер заявок (роль: Администратор)",
        description = "Описывает операции, которые можно выполнять с заявками")
@SecurityRequirement(name = "bearer")
public class ApplicationController {
    private final ApplicationService applicationService;

    @Operation(summary = "Просмотр всех заявок",
            description = """
                    Просмотр заявок всех пользователей со статусом SENT, ACCEPTED, DECLINED
                    (необходимы права администратора)
                    """)
    @GetMapping
    public ResponseEntity<List<ApplicationDto>> getAllApplications(
            @RequestParam(name = "offset", defaultValue = "1") Integer offset,
            @RequestParam(name = "sortBy", defaultValue = "asc") String sortBy,
            @RequestParam(name = "firstName", required = false) String firstName
    ) {
        List<ApplicationDto> response = applicationService.getAllApplications(offset, sortBy, firstName);
        return ResponseEntity.ok(response);
    }
}
