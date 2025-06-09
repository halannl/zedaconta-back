# ZedaConta Backend API

Backend API para a aplicação ZedaConta, desenvolvida em Java com Spring Boot.

## Tecnologias

- Java 17 (LTS)
- Spring Boot 3.2.0
- Spring Security com JWT Authentication
- Spring Data JPA
- PostgreSQL (produção)
- H2 Database (desenvolvimento)
- Maven

## Estrutura do Projeto

```
src/main/java/com/zedaconta/api/
├── config/            # Configurações da aplicação
├── controller/        # Controladores REST
├── model/             # Entidades e modelos
├── repository/        # Repositórios JPA
├── security/          # Configurações de segurança e JWT
└── service/           # Camada de serviço
```

## Funcionalidades

- Autenticação JWT para comunicação segura entre frontend e backend
- CORS configurado para permitir acesso do frontend hospedado em outro servidor
- API RESTful para operações CRUD

## Requisitos

- JDK 17+
- Maven 3.8+

## Configuração

O arquivo `application.yml` contém as configurações da aplicação, incluindo:
- Configurações de banco de dados
- Configurações de JWT
- Configurações de CORS

## Execução

Para executar a aplicação localmente:

```bash
mvn spring-boot:run
```

A API estará disponível em: http://localhost:8080/api

## Endpoints de Autenticação

- `POST /api/auth/register` - Registrar novo usuário
- `POST /api/auth/login` - Autenticar usuário e obter token JWT
