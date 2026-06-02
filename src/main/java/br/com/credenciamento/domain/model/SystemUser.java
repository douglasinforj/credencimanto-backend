package br.com.credenciamento.domain.model;

import org.hibernate.annotations.CreationTimestamp;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.credenciamento.domain.enums.UserRole;
import br.com.credenciamento.domain.enums.DocumentType;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "system_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(unique = true, nullable = false, length = 200)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", length = 20)
    private DocumentType documentType;

    @Column(name = "document_number", length = 30)
    private String documeString;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // -----UserDetails------Spring Security------------


    /**
     * Collection <?> - Collection do java (Lista, Set, Queue) - <?> indica qualquer uma.
     * GrantedAuthority - Interface do Spring Security, representa uma permissão
     * getAuthorities - Retorna permissões dos usuários
     * new SimpleGrantedAuthority - Implantação pronta da interface
     * "ROLE_" - convensão do Spring Security
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }      //Verifica se a conta expirou

    @Override
    public boolean isAccountNonLocked()  { return true; }      //Verifica se a conta esta bloqueada

    @Override
    public boolean isCredentialsNonExpired() { return true; }  //Verifica se a senha expirou

    @Override
    public boolean isEnabled() { return active; }    //Verifica se usuário esta ativo


}
