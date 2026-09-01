# Documento de Requisitos — Gestor de Eventos & Convites

Documento de Requisitos — Gestor de Eventos & Convites
Versão: 1.0  |  Data: 03/08/2026  |  Status: Em definição / aprovado para escopo inicial
# 1. Visão Geral do Produto
O Gestor de Eventos & Convites é uma plataforma focada na organização de listas de presença e no controle seguro de
acesso a eventos por meio de QR Code ou código único validável.
O sistema permitirá que organizadores criem eventos, cadastrem convidados, enviem convites com código individual e
acompanhem entradas em tempo real. Na portaria, controladores de acesso poderão escanear ou digitar códigos para
liberar, negar ou autorizar manualmente entradas, conforme regras configuradas pelo organizador.
O convidado será um usuário passivo, sem necessidade de criar conta ou login, recebendo o convite por e-mail e
apresentando o QR Code/código no momento do acesso ao evento.
# 2. Objetivo do Projeto
Gerenciar convidados e eventos.
Gerar convites individuais com código único.
Enviar convites por e-mail com QR Code.
Controlar acessos na portaria com leitura rápida.
Evitar reutilização, falsificação ou entradas indevidas.
Aplicar regras de horário, tolerância e acompanhantes.
Fornecer visibilidade em tempo real para o organizador.
# 3. Escopo Inicial
## 3.1 Faz parte do escopo
Cadastro de organizadores e controladores de acesso.
Criação e edição de eventos.
Cadastro manual de convidados e importação via CSV/Excel.
Geração de código único por convite e QR Code vinculado.
Envio de convite por e-mail.
Controle de status do convite.
Validação de acesso por QR Code ou código digitado.
Registro de check-in com data/hora.
Bloqueio de reutilização do mesmo convite.
Regras de antecedência e tolerância de atraso.
Controle de acompanhantes por convite.
Tela de validação para portaria.
Dashboard simples para acompanhamento de entradas.
3.2 Não faz parte do escopo inicial
Venda de ingressos e pagamentos online.
App nativo para convidados.
Rede social do evento.
Check-in por reconhecimento facial.
Catracas físicas integradas.
03/08/2026, 12:50
Gestor de Eventos & Convites — Documento de Requisitos
1/14


Emissão fiscal.
Marketplace de eventos.
# 4. Perfis de Usuário
Perfil
Descrição
Permissões principais
Organizador / Criador
Usuário que cria e administra
eventos.
Criar eventos, gerenciar convidados, configurar regras, enviar
convites, visualizar relatórios e autorizar exceções.
Controlador de Acesso
/ Portaria
Usuário responsável pela
validação na entrada.
Escanear/digitar códigos, visualizar status do convite, registrar
entrada e consultar lista básica.
Convidado
Pessoa que recebe o convite.
Receber e-mail com QR Code e apresentá-lo na portaria. Não
possui conta ou login.
# 5. Conceitos Principais
Conceito
Descrição
Evento
Encontro criado pelo organizador, com data, local, regras e convidados.
Convidado
Pessoa convidada para participar do evento.
Convite
Registro individual que vincula um convidado a um evento.
Código de Acesso
Código único gerado para validação do convite.
QR Code
Representação visual do código de acesso para leitura rápida.
Check-in
Registro de entrada do convidado na portaria.
Janela de Acesso
Período em que o convite pode ser validado.
Tolerância de Atraso
Tempo adicional após o início do evento em que entradas ainda são permitidas.
Acompanhantes
Pessoas adicionais permitidas além do titular do convite.
# 6. Requisitos Funcionais
## 6.1 Gestão de Eventos
ID
Requisito
Prioridade
- RF-001
O organizador deve poder criar um evento.
Alta
- RF-002
O organizador deve poder editar eventos criados.
Alta
- RF-003
O organizador deve poder cancelar ou encerrar eventos.
Média
- RF-004
O evento deve possuir nome, descrição, local, data e horário de início e término.
Alta
- RF-005
O evento deve permitir configuração de janela de acesso.
Alta
- RF-006
O evento deve permitir configurar antecedência mínima para entrada.
Alta
- RF-007
O evento deve permitir configurar tolerância máxima de atraso.
Alta
- RF-008
O evento deve permitir definir limite de acompanhantes por convite.
Alta
03/08/2026, 12:50
Gestor de Eventos & Convites — Documento de Requisitos
2/14


- RF-009
O evento deve exibir link para Google Maps e/ou Waze.
Baixa
- RF-010
O organizador deve poder visualizar a lista de convidados do evento.
Alta
## 6.2 Gestão de Convidados
ID
Requisito
Prioridade
- RF-011
O organizador deve poder cadastrar convidados individualmente.
Alta
- RF-012
O organizador deve poder editar dados de convidados.
Alta
- RF-013
O organizador deve poder remover convidados de um evento.
Alta
- RF-014
O sistema deve permitir importar convidados via arquivo CSV.
Média
- RF-015
O sistema deve permitir importar convidados via arquivo Excel.
Média
- RF-016
O sistema deve validar dados importados antes de salvar.
Alta
- RF-017
O sistema deve identificar e-mails duplicados no mesmo evento.
Alta
- RF-018
O sistema deve permitir visualizar erros de importação.
Média
- RF-019
O sistema deve permitir buscar convidados por nome, e-mail ou código.
Alta
- RF-020
O organizador deve poder definir quantidade de acompanhantes por convidado, quando permitido.
Alta
## 6.3 Geração de Convites e Códigos de Acesso
ID
Requisito
Prioridade
- RF-021
O sistema deve gerar um convite para cada convidado vinculado ao evento.
Alta
- RF-022
Cada convite deve possuir código único.
Alta
- RF-023
O código deve ser gerado com hash assinado ou token criptográfico.
Alta
- RF-024
O sistema deve gerar QR Code correspondente ao código do convite.
Alta
- RF-025
O QR Code deve conter identificação segura do convite.
Alta
- RF-026
O sistema deve permitir reenvio individual de convite.
Alta
- RF-027
O sistema deve permitir reenvio em lote de convites.
Média
- RF-028
O sistema deve permitir revogar um convite.
Alta
- RF-029
O sistema deve permitir gerar novo código para um convite revogado ou comprometido.
Média
- RF-030
O convite deve possuir status.
Alta
## 6.4 Envio de Convites por E-mail
ID
Requisito
Prioridade
- RF-031
O sistema deve enviar convite por e-mail ao convidado.
Alta
- RF-032
O e-mail deve conter nome do convidado.
Alta
- RF-033
O e-mail deve conter nome do evento.
Alta
- RF-034
O e-mail deve conter data e horário do evento.
Alta
- RF-035
O e-mail deve conter local do evento.
Alta
03/08/2026, 12:50
Gestor de Eventos & Convites — Documento de Requisitos
3/14


- RF-036
O e-mail deve conter link para Google Maps e/ou Waze.
Baixa
- RF-037
O e-mail deve exibir QR Code no corpo.
Alta
- RF-038
O e-mail deve anexar QR Code em imagem ou PDF.
Média
- RF-039
O e-mail deve conter instruções de acesso.
Média
- RF-040
O sistema deve registrar data/hora do envio do convite.
Alta
- RF-041
O sistema deve registrar falhas de envio.
Média
- RF-042
O organizador deve poder visualizar status de envio dos convites.
Média
## 6.5 Controle de Acesso na Portaria
ID
Requisito
Prioridade
- RF-043
O controlador deve poder acessar módulo de portaria via navegador.
Alta
- RF-044
O controlador deve poder escanear QR Code pela câmera.
Alta
- RF-045
O controlador deve poder digitar manualmente o código.
Alta
- RF-046
O sistema deve validar o código informado.
Alta
- RF-047
O sistema deve verificar se o convite pertence ao evento ativo.
Alta
- RF-048
O sistema deve verificar se o convite está dentro da janela de acesso.
Alta
- RF-049
O sistema deve verificar se o convite já foi utilizado.
Alta
- RF-050
O sistema deve verificar quantidade de acompanhantes autorizados.
Alta
- RF-051
O sistema deve registrar check-in quando a entrada for liberada.
Alta
- RF-052
O sistema deve impedir reutilização do mesmo convite.
Alta
- RF-053
O sistema deve exibir resposta visual clara de sucesso ou erro.
Alta
- RF-054
O sistema deve permitir autorização manual por organizador em casos excepcionais.
Média
- RF-055
O sistema deve registrar toda tentativa de validação.
Alta
- RF-056
O sistema deve permitir visualizar histórico de tentativas.
Média
## 6.6 Dashboard e Relatórios
ID
Requisito
Prioridade
- RF-057
O organizador deve poder visualizar total de convidados.
Alta
- RF-058
O organizador deve poder visualizar total de entradas realizadas.
Alta
- RF-059
O organizador deve poder visualizar convites utilizados.
Alta
- RF-060
O organizador deve poder visualizar convites pendentes.
Média
- RF-061
O organizador deve poder visualizar convites expirados.
Média
- RF-062
O organizador deve poder visualizar taxa de comparecimento.
Média
- RF-063
O dashboard deve atualizar em tempo real.
Média
- RF-064
O organizador deve poder exportar lista de presença.
Baixa
- RF-065
O organizador deve poder filtrar por nome, status ou horário de entrada.
Média
03/08/2026, 12:50
Gestor de Eventos & Convites — Documento de Requisitos
4/14


# 7. Regras de Negócio
### RN-001 — Código único por convite
Cada convite deve possuir um código único, não repetido e não previsível. O sistema deve garantir que nenhum código
ativo seja duplicado dentro do mesmo evento ou globalmente.
### RN-002 — Código não reutilizável
Após o primeiro check-in válido, o convite deve ser marcado como Utilizado. Se um convite já utilizado for apresentado
novamente, o sistema deve negar acesso.
### RN-003 — Janela de acesso
A janela de acesso é o período em que o convite pode ser validado.
Início da janela = Data/Hora início do evento − Antecedência permitida
Fim da janela   = Data/Hora início do evento + Tolerância de atraso
Exemplo:
Evento: 19:00 | Antecedência: 30 min | Tolerância: 30 min
Janela de acesso: 18:30 até 19:30
### RN-004 — Antecedência
O convite não deve ser aceito antes do início da janela configurada.
Evento inicia às 19:00 | Antecedência: 30 minutos
Entrada às 18:10 → Negada
Entrada às 18:45 → Válida
### RN-005 — Tolerância de atraso
Entradas após o horário de início podem ser permitidas apenas até o limite configurado.
Evento inicia às 19:00 | Tolerância: 30 minutos
Entrada às 19:20 → Válida
Entrada às 19:31 → Expirada
### RN-006 — Status do convite
Status
Descrição
Pendente
Convite criado/enviado, mas ainda não utilizado.
Valido
Convite está dentro da janela de acesso e pode ser utilizado.
Expirado
Convite está fora da janela de acesso ou após tolerância máxima.
Utilizado
Convite já foi validado na portaria.
Revogado
Convite foi cancelado manualmente pelo organizador.
### RN-007 — Prioridade do status (ordem de validação)
1. Verificar se o código existe.
03/08/2026, 12:50
Gestor de Eventos & Convites — Documento de Requisitos
5/14


2. Verificar se pertence ao evento correto.
3. Verificar se não está revogado.
4. Verificar se já foi utilizado.
5. Verificar janela de acesso.
6. Verificar capacidade de acompanhantes.
7. Liberar ou negar acesso.
### RN-008 — Acompanhantes
O convite pode ser individual ou permitir acompanhantes. Se acompanhantes = 0, somente o titular entra. Se
acompanhantes = N, o titular pode trazer até N pessoas adicionais.
Total permitido = 1 titular + N acompanhantes
Exemplo: Acompanhantes permitidos: 2 → Total permitido: 3 pessoas
Opcionalmente, o sistema pode registrar entrada separada por acompanhante para controle mais rigoroso.
### RN-009 — Autorização manual
Em casos excepcionais, o organizador pode autorizar manualmente uma entrada (convidado sem e-mail, erro de leitura
do QR Code, chegada após tolerância, problema técnico na portaria). A autorização manual deve: exigir usuário com
perfil organizador; registrar motivo; registrar data/hora; gerar trilha de auditoria.
### RN-010 — Revogação de convite
O organizador pode revogar um convite a qualquer momento antes do uso. Após revogado: o convite não pode mais ser
validado; o QR Code antigo deve ser invalidado; o status deve ser alterado para Revogado.
### RN-011 — Reemissão de convite
Se um convite for revogado ou suspeito de comprometimento, o organizador pode gerar novo código. O código antigo
deve ser invalidado; o novo código mantém vínculo com o mesmo convidado/evento; o histórico preserva códigos
anteriores.
### RN-012 — Importação de convidados
A importação deve validar: campos obrigatórios, formato de e-mail, duplicidade no mesmo evento, limite máximo de
registros por arquivo e erros por linha.
nome,email,telefone,acompanhantes
João Silva,joao@email.com,+5511999999999,1
Maria Souza,maria@email.com,+5511988888888,0
### RN-013 — E-mail do convidado
O envio do convite depende de um e-mail válido. Não enviar convite se o e-mail for inválido; registrar tentativa de envio;
permitir reenvio manual; não criar conta para o convidado.
### RN-014 — Evento encerrado
Após o horário de término do evento ou encerramento manual: novos check-ins devem ser bloqueados; o dashboard
permanece disponível para consulta; o evento deve ficar em modo somente leitura.
03/08/2026, 12:50
Gestor de Eventos & Convites — Documento de Requisitos
6/14


### RN-015 — Segurança do código
O código de acesso deve ser: único, não sequencial, não previsível, assinado ou associado a um hash/token válido e
validado no servidor. O QR Code não deve conter dados sensíveis além do identificador necessário para validação.
# 8. Máquina de Estados do Convite
De
Para
Gatilho
—
Pendente
Convite criado/enviado
Pendente
Valido
Entrou na janela de acesso
Pendente
Expirado
Janela de acesso terminou sem uso
Pendente
Revogado
Organizador revogou
Valido
Utilizado
Check-in realizado
Valido
Expirado
Tolerância ultrapassada
Valido
Revogado
Organizador revogou
Expirado
Utilizado
Autorização manual excepcional
# 9. Fluxo Principal do Sistema
1. Organizador cria evento.
2. Define regras de horário, tolerância e acompanhantes.
3. Vincula convidados.
4. Sistema gera convites com códigos únicos.
5. Sistema envia e-mails com QR Code.
6. Convidado recebe convite.
7. Convidado chega ao evento.
8. Controlador escaneia ou digita código.
9. Sistema valida: código válido? já utilizado? dentro da janela? acompanhantes dentro do limite?
10. Resultado: acesso liberado (com check-in registrado) ou acesso negado/expirado/autorização manual.
# 10. Fluxo de Validação na Portaria
## 10.1 Entrada de dados
Leitura de QR Code pela câmera.
Digitação manual do código.
Busca por nome/e-mail, com autorização manual, quando habilitado.
## 10.2 Etapas de validação
1. Receber código informado.
2. Consultar convite correspondente.
3. Validar evento ativo.
4. Validar status do convite.
5. Validar se já houve check-in.
6. Validar janela de acesso.
7. Validar acompanhantes permitidos.
8. Retornar resultado.
03/08/2026, 12:50
Gestor de Eventos & Convites — Documento de Requisitos
7/14


## 10.3 Respostas possíveis
Situação
Status retornado
Ação do sistema
Código válido e disponível
ACESSO_LIBERADO
Registrar check-in e exibir mensagem verde.
Código inexistente
CODIGO_INVALIDO
Exibir mensagem vermelha.
Código revogado
CONVITE_REVOGADO
Exibir mensagem vermelha.
Código já utilizado
CONVITE_JA_UTILIZADO
Exibir mensagem vermelha com horário da entrada
anterior.
Fora da janela por antecipação
FORA_DO_HORARIO
Exibir mensagem amarela/vermelha.
Tolerância excedida
ATRASO_EXCEDIDO
Exibir mensagem amarela e permitir autorização manual,
se habilitado.
Evento encerrado
EVENTO_ENCERRADO
Exibir mensagem vermelha.
Limite de acompanhantes
excedido
ACOMPANHANTES_EXCEDIDOS
Exibir mensagem vermelha.
# 11. Requisitos de Interface
## 11.1 Tela do Organizador — Lista de Eventos
Criar novo evento; listar eventos ativos, futuros e encerrados; visualizar status; acessar dashboard; editar evento;
gerenciar convidados.
## 11.2 Tela do Organizador — Gestão de Convidados
Visualizar convidados; buscar por nome, e-mail ou código; adicionar, editar e remover convidado; importar
convidados; reenviar convite; revogar convite; visualizar status do convite.
## 11.3 Tela do Organizador — Dashboard
Total de convidados; total de presentes; percentual de comparecimento; últimos check-ins; gráfico/linha do
tempo de entradas; filtro por status; exportação da lista.
## 11.4 Tela do Controlador — Portaria
Deve ser simples, rápida, responsiva, compatível com celular/tablet, com botões grandes e feedback visual imediato.
Elementos principais:
Botão para escanear QR Code.
Campo para digitar código manualmente.
Área de resposta com cores: verde (liberado), vermelho (negado), amarelo (atenção/autorização manual).
Informações do convidado: nome, evento, acompanhantes permitidos, horário do check-in.
Botão de autorização manual, quando aplicável.
# 12. Requisitos de Dados
## 12.1 Entidade: Usuario
Campo
Tipo
Obrigatório
Descrição
id
UUID
Sim
Identificador do usuário.
03/08/2026, 12:50
Gestor de Eventos & Convites — Documento de Requisitos
8/14


nome
string
Sim
Nome do usuário.
email
string
Sim
E-mail de acesso.
senha_hash
string
Sim
Credencial criptografada.
perfil
enum
Sim
ORGANIZADOR, CONTROLADOR, ADMIN.
criado_em
datetime
Sim
Data de criação.
## 12.2 Entidade: Evento
Campo
Tipo
Obrigatório
Descrição
id
UUID
Sim
Identificador do evento.
organizador_id
UUID
Sim
Usuário organizador responsável.
nome
string
Sim
Nome do evento.
descricao
text
Não
Descrição do evento.
local_nome
string
Sim
Nome do local.
endereco
string
Sim
Endereço completo.
link_maps
string
Não
Link para Google Maps/Waze.
data_inicio
datetime
Sim
Início do evento.
data_termino
datetime
Sim
Término do evento.
antecedencia_minutos
integer
Sim
Minutos permitidos antes do início.
tolerancia_atraso_minutos
integer
Sim
Minutos permitidos após o início.
acompanhantes_padrao
integer
Sim
Limite padrão de acompanhantes.
status
enum
Sim
RASCUNHO, ATIVO, ENCERRADO, CANCELADO.
criado_em / atualizado_em
datetime
Sim
Datas de criação/atualização.
## 12.3 Entidade: Convidado
Campo
Tipo
Obrigatório
Descrição
id
UUID
Sim
Identificador do convidado.
evento_id
UUID
Sim
Evento ao qual pertence.
nome
string
Sim
Nome do convidado.
email
string
Sim
E-mail para envio do convite.
telefone
string
Não
Telefone opcional.
acompanhantes_permitidos
integer
Sim
Quantidade de acompanhantes.
observacoes
text
Não
Anotações internas.
criado_em
datetime
Sim
Data de criação.
## 12.4 Entidade: Convite
Campo
Tipo
Obrigatório
Descrição
03/08/2026, 12:50
Gestor de Eventos & Convites — Documento de Requisitos
9/14


id
UUID
Sim
Identificador do convite.
convidado_id
UUID
Sim
Convidado vinculado.
evento_id
UUID
Sim
Evento vinculado.
codigo
string
Sim
Código único de acesso.
qr_code_url
string
Não
URL da imagem do QR Code.
status
enum
Sim
PENDENTE, VALIDO, EXPIRADO, UTILIZADO, REVOGADO.
enviado_em
datetime
Não
Data/hora do último envio.
status_envio
enum
Não
NAO_ENVIADO, ENVIADO, FALHA.
criado_em / atualizado_em
datetime
Sim
Datas de criação/atualização.
## 12.5 Entidade: CheckIn
Campo
Tipo
Obrigatório
Descrição
id
UUID
Sim
Identificador do check-in.
convite_id
UUID
Sim
Convite validado.
evento_id
UUID
Sim
Evento relacionado.
controlador_id
UUID
Sim
Usuário que realizou a validação.
data_hora
datetime
Sim
Momento exato da entrada.
tipo_entrada
enum
Sim
QR_CODE, CODIGO_DIGITADO, AUTORIZACAO_MANUAL.
resultado
enum
Sim
LIBERADO, NEGADO, AUTORIZADO_MANUALMENTE.
motivo_negativa
string
Não
Motivo quando negado.
dispositivo
string
Não
Identificação opcional do dispositivo.
## 12.6 Entidade: AutorizacaoManual
Campo
Tipo
Obrigatório
Descrição
id
UUID
Sim
Identificador da autorização.
evento_id
UUID
Sim
Evento relacionado.
convite_id
UUID
Não
Convite autorizado, se existente.
organizador_id
UUID
Sim
Organizador que autorizou.
controlador_id
UUID
Não
Controlador que solicitou.
motivo
string
Sim
Motivo da autorização.
criado_em
datetime
Sim
Data/hora da autorização.
# 13. Requisitos Não Funcionais
## 13.1 Usabilidade
ID
Requisito
03/08/2026, 12:50
Gestor de Eventos & Convites — Documento de Requisitos
10/14


- RNF-001
A tela de portaria deve ser simples e rápida.
- RNF-002
O tempo de resposta da validação deve ser baixo.
- RNF-003
A interface deve funcionar bem em celulares e tablets.
- RNF-004
O fluxo de leitura deve exigir o mínimo de cliques.
- RNF-005
As mensagens de erro devem ser claras.
## 13.2 Performance
ID
Requisito
- RNF-006
A validação de um código deve ocorrer preferencialmente em menos de 2 segundos.
- RNF-007
O dashboard deve suportar atualização frequente sem sobrecarga excessiva.
- RNF-008
Importações devem ser processadas de forma assíncrona quando houver grande volume.
- RNF-009
O sistema deve suportar múltiplos controladores simultâneos no mesmo evento.
## 13.3 Segurança
ID
Requisito
- RNF-010
Senhas devem ser armazenadas com hash seguro.
- RNF-011
Tokens de sessão devem expirar por inatividade.
- RNF-012
QR Codes devem usar identificadores seguros e validação server-side.
- RNF-013
O sistema deve impedir acesso não autorizado ao módulo de portaria.
- RNF-014
Toda autorização manual deve gerar log.
- RNF-015
Tentativas de validação devem ser registradas para auditoria.
- RNF-016
APIs devem usar autenticação e autorização.
- RNF-017
Dados pessoais devem ser protegidos conforme boas práticas.
## 13.4 Disponibilidade e Confiabilidade
ID
Requisito
- RNF-018
O sistema deve estar disponível durante o horário do evento.
- RNF-019
O registro de check-in deve ser idempotente ou protegido contra duplicidade.
- RNF-020
Falhas de envio de e-mail devem ser registradas e reprocessáveis.
- RNF-021
O sistema deve possuir backup ou mecanismo de recuperação de dados.
## 13.5 Privacidade
ID
Requisito
- RNF-022
Dados de convidados devem ser usados apenas para finalidade do evento.
- RNF-023
O organizador deve ser responsável pelos dados inseridos.
- RNF-024
Convidados não devem ter acesso a dados de outros convidados.
03/08/2026, 12:50
Gestor de Eventos & Convites — Documento de Requisitos
11/14


- RNF-025
Deve haver opção de exclusão de dados quando aplicável.
# 14. Regras de Validação de Horário
## 14.1 Exemplo de configuração
Evento: Aniversário de Empresa
Data: 10/09/2026
Horário de início: 19:00 | Término: 23:30
Antecedência permitida: 30 minutos
Tolerância de atraso: 30 minutos
## 14.2 Janela resultante
Início da liberação: 18:30
Fim da liberação: 19:30
Encerramento do evento: 23:30
## 14.3 Casos de teste
Horário da tentativa
Status esperado
Observação
18:10
Negado
Antes da antecedência permitida.
18:30
Válido
Início da janela de acesso.
18:55
Válido
Antes do início do evento.
19:15
Válido
Dentro da tolerância.
19:30
Válido
Limite da tolerância.
19:31
Expirado
Tolerância ultrapassada.
22:00
Negado / autorização manual
Depende da regra de entradas tardias.
23:31
Negado
Evento encerrado.
# 15. Fluxos de Exceção
## 15.1 Convidado sem e-mail
Organizador busca convidado pelo nome; autoriza manualmente; sistema registra autorização com motivo.
## 15.2 QR Code ilegível
Controlador digita código manualmente; convidado apresenta código do e-mail; organizador autoriza
manualmente, se necessário.
## 15.3 Convidado com convite já utilizado
Sistema exibe horário e operador do check-in anterior; acesso negado; organizador pode autorizar manualmente
em caso de erro operacional.
03/08/2026, 12:50
Gestor de Eventos & Convites — Documento de Requisitos
12/14


## 15.4 Falha de internet na portaria
Escopo inicial: exibir mensagem de indisponibilidade; validações apenas com conexão. Fase futura: modo offline
com sincronização posterior.
## 15.5 E-mail não recebido
Organizador verifica status de envio; sistema permite reenvio; organizador pode apresentar código manualmente
na portaria.
# 16. Critérios de Aceite
16.1 Criação de evento: Dado que um organizador está logado, quando cria um evento com dados válidos, então o
evento deve ser salvo e aparecer na lista de eventos.
16.2 Geração de convite: Dado que um convidado foi vinculado a um evento, quando o sistema processa o convite,
então deve gerar código único e QR Code correspondente.
16.3 Envio de e-mail: Dado que um convite possui e-mail válido, quando o organizador solicita envio, então o sistema
deve enviar e-mail com QR Code e registrar o envio.
16.4 Entrada válida: Dado que um convite está válido e não utilizado, quando o controlador escaneia o QR Code, então
o sistema deve registrar check-in e exibir acesso liberado.
16.5 Reutilização bloqueada: Dado que um convite já foi utilizado, quando o mesmo QR Code é apresentado
novamente, então o sistema deve negar acesso e exibir status Utilizado.
16.6 Tolerância excedida: Dado que a tolerância configurada é de 30 minutos, quando um convidado tenta entrar 31
minutos após o início, então o sistema deve exibir Expirado ou Atraso excedido.
16.7 Acompanhantes: Dado que um convite permite 2 acompanhantes, quando o total de entradas solicitadas
ultrapassa 3 pessoas, então o sistema deve negar acesso por excesso de acompanhantes.
# 17. Métricas e Indicadores Sugeridos
Métrica
Descrição
Taxa de comparecimento
Percentual de convidados que realizaram check-in.
Tempo médio de validação
Tempo entre leitura do QR Code e resposta.
Taxa de erro por código inválido
Quantidade de tentativas com código inexistente.
Taxa de reuso
Tentativas de utilizar convites já registrados.
Taxa de atraso
Entradas após o horário de início.
Taxa de autorização manual
Quantidade de entradas liberadas manualmente.
Taxa de falha de envio de e-mail
Percentual de e-mails não entregues.
# 18. Roadmap de Evolução
Fase 1 — MVP
Cadastro de organizador; criação de evento; cadastro manual de convidados; geração de convite com código
único; QR Code; envio por e-mail; tela de validação da portaria; registro de check-in; bloqueio de reuso; regras de
03/08/2026, 12:50
Gestor de Eventos & Convites — Documento de Requisitos
13/14


antecedência e tolerância.
Fase 2 — Consolidação
Importação CSV/Excel; dashboard em tempo real; autorização manual com auditoria; reenvio em lote; exportação
de relatórios; histórico completo de tentativas; controle de acompanhantes por entrada.
Fase 3 — Expansão
RSVP; convite em PDF; Google Wallet; Apple Wallet; modo offline para portaria; multiportarias; controle por
lotes/grupos; notificações por SMS/WhatsApp.
# 19. Recursos Futuros Sugeridos
## 19.1 Confirmação de Presença — RSVP
Antes do envio do QR Code final, o e-mail pode conter botões Confirmar presença e Não poderei ir. Benefícios:
redução de custos, lista mais precisa, melhor planejamento e liberação de vagas para suplentes.
## 19.2 Acompanhamento em Tempo Real
Dashboard com total de entradas por minuto, percentual de presentes, gráfico de chegada dos convidados e alertas de
pico na portaria.
## 19.3 Modo Offline para Portaria
Permitir que o controlador baixe a lista de convites antes do evento, realize validações sem internet, armazene check-ins
localmente e sincronize automaticamente ao reconectar. Requisitos: evitar conflito de dados, impedir reuso entre
dispositivos offline e garantir integridade da sincronização.
## 19.4 Carteiras Digitais
Integração com Google Wallet e Apple Wallet. Benefícios: melhor experiência do convidado, acesso rápido ao convite e
redução de dependência do e-mail.
# 20. Considerações Finais
O Gestor de Eventos & Convites resolve um problema real de segurança, organização e controle de acesso em eventos.
O MVP deve priorizar simplicidade operacional, confiabilidade na validação e segurança contra reutilização de códigos. A
base do sistema deve ser construída em torno de três pilares:
1. Convite único e seguro: cada convidado possui um código válido, rastreável e não reutilizável.
2. Validação rápida na portaria: o controlador precisa de uma interface simples, com resposta imediata e clara.
3. Gestão em tempo real para o organizador: o organizador deve acompanhar entradas, status e possíveis
exceções com transparência.
Com essa estrutura, o produto pode evoluir gradualmente para recursos como RSVP, modo offline, carteiras digitais,
relatórios avançados e integrações com outros canais de comunicação.
03/08/2026, 12:50
Gestor de Eventos & Convites — Documento de Requisitos
14/14