# Card-02: Autenticação JWT + RBAC

**Status:** Concluído
**Data:** 23/08/2026

## O que foi feito?
Implementamos as bases sólidas da segurança da API-Convite. A premissa central desta etapa foi fechar a API usando Tokens JWT, mantendo a arquitetura Stateless e estabelecendo a infraestrutura do Spring Security para regras de controle de acesso (RBAC).

### Dependências Adicionadas
- `spring-boot-starter-security`: O motor do Spring Security.
- `jjwt` (versão 0.11.5): Para as assinaturas e decodificação do JSON Web Token.
- `spring-boot-starter-validation`: Bean Validation (`@Valid`, `@NotBlank`, etc).
- `mapstruct`: Ferramenta para gerenciar de forma limpa os DTOs (padrão MapStruct exigido no projeto).

### Ajuste de Entidade
- `Usuario` passou a implementar a interface `UserDetails`, integrando-se nativamente ao contexto do Spring. As permissões foram setadas a partir do enum `Perfil` (ex: `ROLE_ORGANIZADOR`).

### Camada `infra/security/`
Criamos o pilar da segurança da aplicação:
1. **SecurityConfig**: Desligou o CSRF, configurou gerência Stateless e bloqueou todas as requisições (com exceção da `/auth/login`). Habilitou o `@EnableMethodSecurity`.
2. **SecurityFilter**: Filtro interceptador. Ele captura tokens "Bearer", usa o `TokenService` para extrair o e-mail, e autentica a requisição via `SecurityContextHolder`.
3. **TokenService**: Cuida da emissão e quebra (parse) do token JWT. Definimos a validade inicial para 2 horas e assinamos usando `HMAC-SHA256` configurado via a env `JWT_SECRET`.

### Login e DTOs
- Criados `LoginDTO`, `TokenDTO` e o `UsuarioResponseDTO`.
- Criado o `UsuarioMapper` integrando com o *MapStruct*.
- Criada a rota `POST /auth/login` gerida pelo `AutenticacaoController`.
- Criado o `AutenticacaoService` que varre o DB (implementando `UserDetailsService`) injetando as rotinas no `AuthenticationManager`.

### Complemento: Cadastro e Exceções
- Criado tratamento de erros global em `infra/exception/GlobalExceptionHandler.java` capturando erros de validação (400), erros de negócio (400) e conflitos (409) através do objeto padronizado `ErrorResponse.java` (Swagger compatível).
- Adicionada a dependência do SpringDoc OpenAPI.
- Implementado o serviço `CadastroUsuarioService` com MapStruct para converter o DTO e fazer o Hash da senha antes de persistir.
- Protegido o endpoint de criação (`POST /usuarios`) com `@PreAuthorize("hasRole('ADMIN')")`, garantindo o isolamento RBAC.

## Próximos Passos
O usuário Master (Role ADMIN) será criado direto na base (PostgreSQL). A partir da fundação criada hoje, basta usarmos a anotação `@PreAuthorize` nos próximos cards para garantir o isolamento correto de cada perfil (Controlador vs Organizador).
