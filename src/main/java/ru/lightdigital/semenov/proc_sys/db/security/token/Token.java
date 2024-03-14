package ru.lightdigital.semenov.proc_sys.db.security.token;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.lightdigital.semenov.proc_sys.db.security.user.User;

import java.time.LocalDateTime;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@ToString
@Table(name = "auth_tokens")
public class Token {
    @Id
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType type;
    
    private LocalDateTime creationTime;
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Token() {
    }
}
