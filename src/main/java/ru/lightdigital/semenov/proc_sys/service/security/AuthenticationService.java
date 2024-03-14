package ru.lightdigital.semenov.proc_sys.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lightdigital.semenov.proc_sys.db.security.token.Token;
import ru.lightdigital.semenov.proc_sys.db.security.token.TokenDbService;
import ru.lightdigital.semenov.proc_sys.db.security.token.TokenType;
import ru.lightdigital.semenov.proc_sys.db.security.user.User;
import ru.lightdigital.semenov.proc_sys.db.security.user.UserDbService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class AuthenticationService implements UserDetailsService {

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    private final TokenDbService tokenDbService;
    private final UserDbService userDbService;

    public User findByLogin(String login) {
        return userDbService.findByLogin(login);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByLogin(username);
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                new BCryptPasswordEncoder().encode(user.getPassword()),
                user.getClient().getRoles().stream().map(
                        role -> new SimpleGrantedAuthority(role.name())
                ).collect(Collectors.toList())
        );
    }

    @Transactional
    public void saveNewToken(String jwt, User user) {
        Token token = Token.builder()
                .token(jwt)
                .type(TokenType.BEARER)
                .creationTime(LocalDateTime.now())
                .revoked(false)
                .user(user).build();
        tokenDbService.revokeAllUserTokens(token.getUser());
        tokenDbService.save(token);
    }

    @Transactional
    public void revokeToken(String token) {
        tokenDbService.revokeToken(token);
    }

    @Scheduled(initialDelay = 1, fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    protected void expireOldTokens() {
        tokenDbService.expireOldTokens(jwtLifetime);
    }
}
