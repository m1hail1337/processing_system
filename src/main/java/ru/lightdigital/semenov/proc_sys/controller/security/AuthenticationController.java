package ru.lightdigital.semenov.proc_sys.controller.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.lightdigital.semenov.proc_sys.db.security.token.Token;
import ru.lightdigital.semenov.proc_sys.db.security.token.TokenType;
import ru.lightdigital.semenov.proc_sys.db.security.user.User;
import ru.lightdigital.semenov.proc_sys.dto.security.LoginRequest;
import ru.lightdigital.semenov.proc_sys.dto.security.JwtTokenDto;
import ru.lightdigital.semenov.proc_sys.service.security.AuthenticationService;
import ru.lightdigital.semenov.proc_sys.utils.JwtTokenUtils;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер аутентификации", description = "Предоставляет возможности аутентификации пользователей")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @Operation(summary = "Авторизация в систему", description = "Выдает авторизационный JWT токен")
    public ResponseEntity<JwtTokenDto> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.login(), request.password()
            ));
            User user = authenticationService.findByLogin(request.login());
            UserDetails userDetails = authenticationService.loadUserByUsername(request.login());
            String jwt = jwtTokenUtils.generateToken(userDetails);
            authenticationService.saveNewToken(jwt, user);
            return ResponseEntity.ok(new JwtTokenDto(jwt));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (NoSuchElementException | UsernameNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/logout")
    @Operation(summary = "Выход из системы",
            description = "Отзывает авторизационный JWT токен: для продолжения работы клиенту необходимо получить новый")
    public ResponseEntity<Void> logout(@RequestBody JwtTokenDto token) {
        try {
            authenticationService.revokeToken(token.jwt());
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
