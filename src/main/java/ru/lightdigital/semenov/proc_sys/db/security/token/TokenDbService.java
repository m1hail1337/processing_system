package ru.lightdigital.semenov.proc_sys.db.security.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lightdigital.semenov.proc_sys.db.security.user.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TokenDbService {
    private final TokenRepository repository;


    public void save(Token token) {
        repository.save(token);
    }

    public void revokeAllUserTokens(User user) {
        List<Token> validTokens = repository.findAllValidTokensByLogin(user.getLogin());
        validTokens.forEach(token -> {
            token.setRevoked(true);
            repository.save(token);
        });
    }

    public List<Token> getAllValidTokensForUser(String login) {
        return repository.findAllValidTokensByLogin(login);
    }

    @Transactional
    public void revokeToken(String jwt) {
        Token token = repository.findById(jwt).orElseThrow(NoSuchElementException::new);
        token.setRevoked(true);
        repository.save(token);
    }

    public void expireOldTokens(Duration jwtLifetime) {
        List<Token> tokens = repository.findAllNotRevokedTokens();
        tokens.forEach(token -> {
            if (jwtLifetime.minus(Duration.between(token.getCreationTime(), LocalDateTime.now())).isNegative()) {
                token.setRevoked(true);
                repository.save(token);
            }
        });
    }
}
