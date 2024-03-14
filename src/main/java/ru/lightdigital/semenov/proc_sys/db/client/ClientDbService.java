package ru.lightdigital.semenov.proc_sys.db.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class ClientDbService {

    private final ClientRepository repository;

    public List<Client> getAllClients() {
        return repository.findAll();
    }

    @Transactional
    public void setOperatorRoleForClient(String clientId) {
        Client client = repository.findById(clientId).orElseThrow(NoSuchElementException::new);
        client.getRoles().add(Role.ROLE_OPERATOR);
        repository.save(client);
    }
}
