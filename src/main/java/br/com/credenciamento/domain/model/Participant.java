package br.com.credenciamento.domain.model;

import br.com.credenciamento.domain.enums.DocumentType;
import br.com.credenciamento.domain.enums.ParticipantType;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.GenerationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(
    /**
     * Email e CPF poderão aparecer em varios eventos, mas não duas vezes no mesmo evento
     */
    name = "participants",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_participant_email_event",
            columnNames = {"email", "eventi_id"}
        ),
        @UniqueConstraint(
            name = "uk_participant_document_event",
            columnNames = {"document_type", "document_number", "event_id"}
        )  
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 200)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", length = 20)
    private DocumentType documentType;

    @Column(name = "document_number", length = 30)
    private String documentNumber;

    @Column(length = 20)
    private String phone;

    @Column(length = 200)
    private String organization;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ParticipantType type = ParticipantType.PARTICIPANT;

    // FOTO
    @Column(name = "photo_path", length = 500)
    private String photoPath;                     // Caminho no Storage (local ou S3)

    // QR Code
    @Column(name = "qr_code_path", length = 500)
    private String qrCodePath;                   // PNG gerado na criação

    @Column(name = "qr_code_token", unique = true, nullable = false, length = 100)
    private String qrCodeToken;                  // UUID usado para validar checking

    // CHECK-IN
    @Column(name = "checked_in")
    @Builder.Default
    private boolean checkedIn = false;

    @Column(name = "checked_in_at")
    private LocalDateTime checkedInAt;

    // RELACIONAMENTO
    /**
     *  ManyToOne - muito participanetes podem estar em um unido evento
     *  fetch = FetchType.LAZY - Controla quando o objeto relacionado será carregado do banco
     *  Lazy loading = Só buscar o evento quando ele realmente for utilizado
     *  optional = false - Relacionamento é obrigatório
     *  @JoinColumn(name = "event_id", nullable = false) - Define a chave estrangeira
     *  
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime updatedAt;

}
