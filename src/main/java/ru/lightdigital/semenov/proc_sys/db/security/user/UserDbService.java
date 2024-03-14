package ru.lightdigital.semenov.proc_sys.db.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserDbService {
    private final UserRepository repository;

    @Transactional
    public User findByLogin(String login) {
        return repository.findByLogin(login).orElseThrow(NoSuchElementException::new);
    }
}
