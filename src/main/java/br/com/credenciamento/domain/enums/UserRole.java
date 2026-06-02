package br.com.credenciamento.domain.enums;

public enum UserRole {
    ADMIN,                    //acesso total: usuário, eventos, relatórios
    OPERADOR,                 // cadastro e importação de participantes
    PORTEIRO,                 // somente check-in no dia do evento
    PALESTRANTE               // acesso ao próprio perfil e agenda
}
