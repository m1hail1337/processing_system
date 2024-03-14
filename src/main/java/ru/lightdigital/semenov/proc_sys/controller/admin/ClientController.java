package ru.lightdigital.semenov.proc_sys.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.lightdigital.semenov.proc_sys.db.client.ClientDbService;
import ru.lightdigital.semenov.proc_sys.dto.client.ClientDto;
import ru.lightdigital.semenov.proc_sys.service.client.ClientService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/clients")
@SecurityRequirement(name = "bearer")
@Tag(name = "Контроллер пользователей", description = """
        Позволяет просматривать и изменять статус пользователей. Доступен только администраторам.
        """)
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    @Operation(summary = "Информация о всех пользователях", description = """
            Выводит массив данных зарегистрированных пользователей (кроме их рег. данных и jwt-токенов)
            """)
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> clients = clientService.getAllUsers();
        return ResponseEntity.ok(clients);
    }

    @PatchMapping("{id}")
    @Operation(summary = "Назначение прав оператора", description = """
            Назначает другому пользователю роль оператора. У пользователя может быть несколько ролей. Так, например,
            если у клиента роль "User" и "Operator", то он может как создавать и отправлять заявки,
            так и принимать и отклонять их.
            """)
    public ResponseEntity<Void> setOperatorRole(@PathVariable("id") String clientId) {
        try {
            clientService.setOperatorRoleForClient(clientId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
