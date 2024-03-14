package ru.lightdigital.semenov.proc_sys.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lightdigital.semenov.proc_sys.db.client.Client;
import ru.lightdigital.semenov.proc_sys.db.client.ClientDbService;
import ru.lightdigital.semenov.proc_sys.dto.client.ClientDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    
    private final ClientDbService clientDbService;

    public List<ClientDto> getAllUsers() {
        return clientDbService.getAllClients().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public void setOperatorRoleForClient(String clientId) {
        clientDbService.setOperatorRoleForClient(clientId);
    }

    private ClientDto convertToDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .phoneNumber(client.getPhoneNumber())
                .roles(client.getRoles())
                .build();
    }
}
