# Card-01: Modelagem de Dados Relacional e Flyway

**Status:** Concluído
**Data:** 22/08/2026

## O que foi feito?
Este documento registra a entrega do Card-01 do Épico 1, focada na estruturação fundamental da base de dados e dos pacotes da aplicação.

1. **Estrutura de Pacotes (Package-by-Feature)**
   - Estabelecemos a estrutura recomendada em `src/main/java/br/com/passos/api_convite`.
   - Divisão em `infra/` e `domain/`.
   - Subdomínios criados: `usuario`, `evento`, `convidado`, `convite`, `checkin`, `portaria`.
   
2. **Configuração de Banco de Dados**
   - Configurado `application.yaml` apontando para a base Postgres, parametrizado com variáveis de ambiente.
   - Flyway ativado para versionamento de esquema.

3. **Migrações Flyway**
   Foram criados os 6 scripts iniciais mapeando fielmente a documentação:
   - `V1__create_table_usuario.sql`
   - `V2__create_table_evento.sql`
   - `V3__create_table_convidado.sql`
   - `V4__create_table_convite.sql`
   - `V5__create_table_checkin.sql`
   - `V6__create_table_autorizacao_manual.sql`

4. **Modelagem de Entidades (JPA)**
   - Criados os Models anotados com `@Entity` e configurados via Lombok (`@Getter`, `@Setter`, `@NoArgsConstructor`).
   - Mapeamentos de cardinalidade configurados (ex: `@ManyToOne`).
   - Enums mapeados (como `Perfil`, `StatusEvento`, etc).

## Próximos Passos (Referência)
- O próximo card a ser desenvolvido é o **Card-02**, que focará no Sistema de Autenticação JWT e na implementação do controle de acesso baseado em Roles (RBAC).
