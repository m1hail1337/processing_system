package ru.lightdigital.semenov.proc_sys.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.lightdigital.semenov.proc_sys.db.client.Client;
import ru.lightdigital.semenov.proc_sys.db.security.user.UserDbService;
import ru.lightdigital.semenov.proc_sys.dto.application.creation.CreationRequest;
import ru.lightdigital.semenov.proc_sys.dto.application.ApplicationDto;
import ru.lightdigital.semenov.proc_sys.dto.application.creation.CreationResponse;
import ru.lightdigital.semenov.proc_sys.dto.application.edit.EditRequest;
import ru.lightdigital.semenov.proc_sys.service.application.ApplicationService;
import ru.lightdigital.semenov.proc_sys.utils.JwtTokenUtils;

import java.util.List;
import java.util.NoSuchElementException;

@RequestMapping("/user/applications")
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Контроллер заявок (роль: Пользователь)",
        description = "Описывает операции, которые можно выполнять с заявками")
@SecurityRequirement(name = "bearer")
public class ApplicationController {

    private final JwtTokenUtils jwtTokenUtils;
    private final ApplicationService applicationService;
    private final UserDbService userDbService;

    @Operation(summary = "Просмотр заявок, созданных пользователем",
            description = """
                    Выводит все заявки пользователя страницами по 5 штук. Можно сортировать по дате создания заявки.
                    """)
    @GetMapping
    public ResponseEntity<List<ApplicationDto>> getAllUserApplications(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset,
            @RequestParam(name = "sortBy", defaultValue = "asc") String sortBy
    ) {
        Client client = getClientFromAuthHeader(authHeader);
        List<ApplicationDto> response = applicationService.getApplications(client, offset, sortBy);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Просмотр заявки",
            description = "Просмотр конкретной заявки (по идентификатору), созданной пользователем")
    @GetMapping("{id}")
    public ResponseEntity<ApplicationDto> getApplication(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable("id") long applicationId
    ) {
        try {
            Client client = getClientFromAuthHeader(authHeader);
            ApplicationDto response = applicationService.getApplication(applicationId, client);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Создание (черновика) заявки",
            description = """
                     Создает заявку с введенным пользователем сообщением,
                     ее статус при создании устанавливается как SKETCH (черновик)
                    """)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<CreationResponse> createApplication(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CreationRequest creationRequest) {
        try {
            Client client = getClientFromAuthHeader(authHeader);
            CreationResponse response = applicationService.createApplication(creationRequest.message(), client);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Отправление заявки", description = "Переводит заявку в статус SENT (отправлена)")
    @PostMapping({"{id}"})
    public ResponseEntity<Void> sendApplication(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable("id") long applicationId) {
        try {
            Client client = getClientFromAuthHeader(authHeader);
            applicationService.send(client, applicationId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Редактирование заявки", description = "Заменяет сообщение в заявке на новое")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public ResponseEntity<Void> editApplication(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody EditRequest request,
            @PathVariable("id") long applicationId) {
        try {
            Client client = getClientFromAuthHeader(authHeader);
            applicationService.editApplicationMessage(client, request.message(), applicationId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    private Client getClientFromAuthHeader(String authHeader) {
        log.info(authHeader);
        String jwt = jwtTokenUtils.parseBearerJwtFromHeader(authHeader);
        log.info(jwt);
        String login = jwtTokenUtils.getUsername(jwt);
        log.info(login);
        return userDbService.findByLogin(login).getClient();
    }
}
