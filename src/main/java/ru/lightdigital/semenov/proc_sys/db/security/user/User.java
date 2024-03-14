package ru.lightdigital.semenov.proc_sys.db.security.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.lightdigital.semenov.proc_sys.db.client.Client;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users_auth")
public class User {
    @Id
    private String id;

    private String login;
    private String password;

    @OneToOne
    @MapsId
    @JoinColumn(name = "client_id")
    private Client client;
}
