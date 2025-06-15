package com.zedaconta.api.model;

/**
 * Enum que define os papéis de usuário no sistema.
 * INVALID_USER: Usuário registrado mas com e-mail não validado
 * USER: Usuário comum com e-mail validado
 * ADMIN: Administrador do sistema
 * FRONTEND: Papel especial para requisições vindas do frontend
 */
public enum Role {
    INVALID_USER,
    USER,
    ADMIN,
    FRONTEND
}
