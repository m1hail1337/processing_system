package ru.lightdigital.semenov.proc_sys.controller.operator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.lightdigital.semenov.proc_sys.dto.application.ApplicationDto;
import ru.lightdigital.semenov.proc_sys.dto.application.status.StatusChangingRequest;
import ru.lightdigital.semenov.proc_sys.service.application.ApplicationService;

import java.util.List;

@RestController
@RequestMapping("/operator/applications")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer")
@Tag(name = "Контроллер отправленных заявок", description = """
        Осуществляет операции только над заявками в состоянии SENT. К этому контроллеру имеют доступ только пользователи
        с ролью "Оператор".
        """)
public class SentApplicationController {

    private final ApplicationService applicationService;

    @Operation(summary = "Просмотр отправленных заявок",
            description = """
                    Просмотр заявок всех пользователей в состоянии SENT (необходимы права оператора)
                    """)
    @GetMapping
    public ResponseEntity<List<ApplicationDto>> getAllSentApplications(
            @RequestParam(name = "offset", defaultValue = "1") Integer offset,
            @RequestParam(name = "sortBy", defaultValue = "asc") String sortBy,
            @RequestParam(name = "firstName", required = false) String firstName
    ) {
        List<ApplicationDto> response = applicationService.getSentApplications(offset, sortBy, firstName);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Просмотр отправленной заявки",
            description = """
                    Просмотр заявки в состоянии SENT (необходимы права оператора)
                    """)
    @GetMapping("{id}")
    public ResponseEntity<ApplicationDto> getSentApplication(@PathVariable("id") long applicationId) {
        try {
            ApplicationDto response = applicationService.getSentApplication(applicationId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Изменение статуса заявки",
            description = """
                    Изменяет статус заявки с SENT на ACCEPTED или DECLINED (необходимы права оператора)
                    """)
    @PatchMapping("{id}")
    public ResponseEntity<Void> changeApplicationStatus(
            @PathVariable("id") long applicationId,
            @RequestBody StatusChangingRequest request) {
        try {
            applicationService.changeStatus(applicationId, request.status());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
