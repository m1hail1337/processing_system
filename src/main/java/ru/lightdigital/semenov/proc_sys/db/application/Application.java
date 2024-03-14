package ru.lightdigital.semenov.proc_sys.db.application;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.lightdigital.semenov.proc_sys.db.client.Client;

import java.time.LocalDateTime;

@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.SKETCH;

    @Column(columnDefinition = "text")
    private String message;

    private final LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
