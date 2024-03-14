package ru.lightdigital.semenov.proc_sys.db.client;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;
import org.springframework.data.repository.cdi.Eager;
import ru.lightdigital.semenov.proc_sys.db.security.user.User;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Setter
@Getter
@ToString
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "clients_roles")
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private final Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private User user;
}
