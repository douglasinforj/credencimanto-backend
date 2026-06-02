package br.com.credenciamento.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.List;
import java.util.ArrayList;


import br.com.credenciamento.domain.enums.EventStatus;
import br.com.credenciamento.domain.enums.ParticipantType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity                   //indica que a class representa uma entidade do banco de dados, hibernate/JPA entende que essa classe deve ser mapeada para a tabela
@Table(name = "events")   //Define o nome da Tabela no banco
@Getter                   // Anotação do Lombok, gera automaticamente os métodos get
@Setter                   // Anotação do Lombok, gera automaticamente os métodos set
@NoArgsConstructor        // Cria construtor Vazio, caso contrario o Hibernate pode lançar erros ao carregar o banco de dados
@AllArgsConstructor       // Cria construtor com todos os atributos
@Builder                  //Implementa o padrão Builder, utilizado em aplicações profissionais para criar objetos de forma legível
public class Event {

    @Id                                                 //Define chave primaria
    @GeneratedValue(strategy = GenerationType.UUID)     //Faz a geração automatica do identificado usando UUID, não depende de numeração do banco
    private UUID id;

    @Column(nullable = false, length = 200)             // @Column - Personaliza a coluna
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false, length = 300)
    private String location;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Enumerated(EnumType.STRING)              //Mapeia um Enum
    @Column(nullable = false, length = 20)
    @Builder.Default                         // Anotação do Lombok - Define valor padrão quando o Builder for utilizado
    private EventStatus status = EventStatus.DRAFT;   //Valor vindo do enum

    @Column(name = "badgeTemplate", length = 50)
    @Builder.Default
    private String badgeTemplate = "DEFAULT";    //armazena o modelo do cracha

    /**
     * OneToMany - Indicando relacionamento
     * mapperBy - Indica quem é o dono do relacionamento "Event" - Fk fica para a tabela Participant
     * CascadeTyde.ALL - propaga operações para os filhos
     * orphanRemoval = true - Remove filhos órfãos
     */
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ParticipantType>participants = new ArrayList<>();     //Lista os tipos de participante

    @CreationTimestamp                               //Preenche automaticamente a data de criação
    @Column(name = "created_at", updatable = false)  // updatable - impede alterações posteriores
    private LocalDateTime createdAt;

    @UpdateTimestamp                        //Atualiza automaticamente a cada UPDATE.
    @Column(name = "updated_at")
    private LocalDateTime updateAt;






}
