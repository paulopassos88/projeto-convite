package br.com.passos.api_convite.domain.convidado.controller;

import br.com.passos.api_convite.AbstractIntegrationTest;
import br.com.passos.api_convite.domain.convidado.dto.AtualizarConvidadoDTO;
import br.com.passos.api_convite.domain.convidado.dto.CriarConvidadoDTO;
import br.com.passos.api_convite.domain.convidado.model.Convidado;
import br.com.passos.api_convite.domain.convidado.repository.ConvidadoRepository;
import br.com.passos.api_convite.domain.evento.model.Evento;
import br.com.passos.api_convite.domain.evento.model.StatusEvento;
import br.com.passos.api_convite.domain.evento.repository.EventoRepository;
import br.com.passos.api_convite.domain.usuario.model.Perfil;
import br.com.passos.api_convite.domain.usuario.model.Usuario;
import br.com.passos.api_convite.domain.usuario.repository.UsuarioRepository;
import br.com.passos.api_convite.infra.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

class ConvidadoControllerTest extends AbstractIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private ConvidadoRepository convidadoRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    private Usuario organizador1;
    private Usuario organizador2;
    private Usuario admin;

    private String tokenOrg1;
    private String tokenOrg2;
    private String tokenAdmin;

    private Evento eventoOrg1;
    private Evento eventoOrg2;

    @BeforeEach
    void setUp() {
        this.mockMvc = webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();

        convidadoRepository.deleteAll();
        eventoRepository.deleteAll();
        usuarioRepository.deleteAll();

        organizador1 = new Usuario();
        organizador1.setNome("Organizador 1");
        organizador1.setEmail("org1@teste.com");
        organizador1.setSenhaHash(passwordEncoder.encode("123456"));
        organizador1.setPerfil(Perfil.ORGANIZADOR);
        usuarioRepository.save(organizador1);

        organizador2 = new Usuario();
        organizador2.setNome("Organizador 2");
        organizador2.setEmail("org2@teste.com");
        organizador2.setSenhaHash(passwordEncoder.encode("123456"));
        organizador2.setPerfil(Perfil.ORGANIZADOR);
        usuarioRepository.save(organizador2);

        admin = new Usuario();
        admin.setNome("Admin");
        admin.setEmail("admin@teste.com");
        admin.setSenhaHash(passwordEncoder.encode("123456"));
        admin.setPerfil(Perfil.ADMIN);
        usuarioRepository.save(admin);

        tokenOrg1 = tokenService.gerarToken(organizador1);
        tokenOrg2 = tokenService.gerarToken(organizador2);
        tokenAdmin = tokenService.gerarToken(admin);

        eventoOrg1 = criarEventoMock(organizador1, "Festa de Gala", StatusEvento.ATIVO);
        eventoRepository.save(eventoOrg1);

        eventoOrg2 = criarEventoMock(organizador2, "Workshop de Tecnologia", StatusEvento.ATIVO);
        eventoRepository.save(eventoOrg2);
    }

    @Test
    @DisplayName("Cenário 1: Deve cadastrar convidado com dados válidos e retornar 201 Created")
    void criarConvidado_ComDadosValidos_RetornaCreated() throws Exception {
        CriarConvidadoDTO dto = new CriarConvidadoDTO(
                "Carlos Silva",
                "carlos@teste.com",
                "+5511999999999",
                2,
                "VIP"
        );

        mockMvc.perform(post("/eventos/" + eventoOrg1.getId() + "/convidados")
                        .header("Authorization", "Bearer " + tokenOrg1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.eventoId").value(eventoOrg1.getId().toString()))
                .andExpect(jsonPath("$.nome").value("Carlos Silva"))
                .andExpect(jsonPath("$.email").value("carlos@teste.com"))
                .andExpect(jsonPath("$.acompanhantesPermitidos").value(2));
    }

    @Test
    @DisplayName("Cenário 2: Deve retornar 409 Conflict ao tentar cadastrar e-mail duplicado no mesmo evento")
    void criarConvidado_EmailDuplicadoNoMesmoEvento_RetornaConflict() throws Exception {
        Convidado existente = criarConvidadoMock(eventoOrg1, "Carlos Silva", "carlos@teste.com");
        convidadoRepository.save(existente);

        CriarConvidadoDTO dto = new CriarConvidadoDTO(
                "Carlos Outro",
                "carlos@teste.com",
                null,
                0,
                null
        );

        mockMvc.perform(post("/eventos/" + eventoOrg1.getId() + "/convidados")
                        .header("Authorization", "Bearer " + tokenOrg1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Já existe um convidado cadastrado com o e-mail informado para este evento."));
    }

    @Test
    @DisplayName("Cenário 3: Deve permitir o mesmo e-mail em eventos distintos (201 Created)")
    void criarConvidado_MesmoEmailEmEventosDistintos_RetornaCreated() throws Exception {
        Convidado convidadoEvento1 = criarConvidadoMock(eventoOrg1, "Carlos Silva", "carlos@teste.com");
        convidadoRepository.save(convidadoEvento1);

        CriarConvidadoDTO dto = new CriarConvidadoDTO(
                "Carlos Silva",
                "carlos@teste.com",
                null,
                1,
                null
        );

        mockMvc.perform(post("/eventos/" + eventoOrg2.getId() + "/convidados")
                        .header("Authorization", "Bearer " + tokenOrg2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventoId").value(eventoOrg2.getId().toString()))
                .andExpect(jsonPath("$.email").value("carlos@teste.com"));
    }

    @Test
    @DisplayName("Cenário 4: Deve retornar 400 Bad Request ao informar dados inválidos no DTO")
    void criarConvidado_DadosInvalidos_RetornaBadRequest() throws Exception {
        CriarConvidadoDTO dto = new CriarConvidadoDTO(
                "", // nome em branco
                "email-invalido", // formato inválido
                null,
                -1, // acompanhantes negativo
                null
        );

        mockMvc.perform(post("/eventos/" + eventoOrg1.getId() + "/convidados")
                        .header("Authorization", "Bearer " + tokenOrg1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.nome").exists())
                .andExpect(jsonPath("$.details.email").exists())
                .andExpect(jsonPath("$.details.acompanhantesPermitidos").exists());
    }

    @Test
    @DisplayName("Cenário 5: Deve retornar 403 Forbidden para requisições não autenticadas")
    void criarConvidado_NaoAutenticado_RetornaForbidden() throws Exception {
        CriarConvidadoDTO dto = new CriarConvidadoDTO(
                "Anonimo",
                "anonimo@teste.com",
                null,
                0,
                null
        );

        mockMvc.perform(post("/eventos/" + eventoOrg1.getId() + "/convidados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Cenário 6: Deve retornar 403 Forbidden quando organizador tenta acessar evento de outro organizador")
    void criarConvidado_OrganizadorDeOutroEvento_RetornaForbidden() throws Exception {
        CriarConvidadoDTO dto = new CriarConvidadoDTO(
                "Invasor",
                "invasor@teste.com",
                null,
                0,
                null
        );

        mockMvc.perform(post("/eventos/" + eventoOrg1.getId() + "/convidados")
                        .header("Authorization", "Bearer " + tokenOrg2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Cenário 7: Deve permitir que usuário ADMIN gerencie convidados de qualquer evento")
    void criarConvidado_ComoAdmin_RetornaCreated() throws Exception {
        CriarConvidadoDTO dto = new CriarConvidadoDTO(
                "Convidado do Admin",
                "admin.convidado@teste.com",
                null,
                1,
                null
        );

        mockMvc.perform(post("/eventos/" + eventoOrg1.getId() + "/convidados")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("admin.convidado@teste.com"));
    }

    @Test
    @DisplayName("Cenário 8: Deve listar convidados de um evento com paginação")
    void listarConvidados_Paginado_RetornaOk() throws Exception {
        convidadoRepository.save(criarConvidadoMock(eventoOrg1, "Ana Beatriz", "ana@teste.com"));
        convidadoRepository.save(criarConvidadoMock(eventoOrg1, "Bruno Costa", "bruno@teste.com"));

        mockMvc.perform(get("/eventos/" + eventoOrg1.getId() + "/convidados")
                        .header("Authorization", "Bearer " + tokenOrg1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].nome").exists());
    }

    @Test
    @DisplayName("Cenário 9: Deve filtrar convidados por nome ou e-mail na busca textual")
    void listarConvidados_ComFiltroBusca_RetornaApenasFiltrados() throws Exception {
        convidadoRepository.save(criarConvidadoMock(eventoOrg1, "Maria Silva", "maria@teste.com"));
        convidadoRepository.save(criarConvidadoMock(eventoOrg1, "João Santos", "joao@teste.com"));

        mockMvc.perform(get("/eventos/" + eventoOrg1.getId() + "/convidados?busca=maria")
                        .header("Authorization", "Bearer " + tokenOrg1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Maria Silva"));
    }

    @Test
    @DisplayName("Cenário 10: Deve buscar convidado específico por ID")
    void buscarPorId_ConvidadoExistente_RetornaOk() throws Exception {
        Convidado convidado = criarConvidadoMock(eventoOrg1, "Fernanda Lima", "fernanda@teste.com");
        convidadoRepository.save(convidado);

        mockMvc.perform(get("/eventos/" + eventoOrg1.getId() + "/convidados/" + convidado.getId())
                        .header("Authorization", "Bearer " + tokenOrg1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(convidado.getId().toString()))
                .andExpect(jsonPath("$.nome").value("Fernanda Lima"));
    }

    @Test
    @DisplayName("Cenário 11: Deve retornar 404 Not Found para ID de convidado inexistente")
    void buscarPorId_ConvidadoInexistente_RetornaNotFound() throws Exception {
        mockMvc.perform(get("/eventos/" + eventoOrg1.getId() + "/convidados/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + tokenOrg1))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Cenário 12: Deve atualizar convidado com sucesso")
    void atualizarConvidado_ComSucesso_RetornaOk() throws Exception {
        Convidado convidado = criarConvidadoMock(eventoOrg1, "Nome Antigo", "antigo@teste.com");
        convidadoRepository.save(convidado);

        AtualizarConvidadoDTO dto = new AtualizarConvidadoDTO(
                "Nome Atualizado",
                "antigo@teste.com",
                "+5511888888888",
                3,
                "Observação atualizada"
        );

        mockMvc.perform(put("/eventos/" + eventoOrg1.getId() + "/convidados/" + convidado.getId())
                        .header("Authorization", "Bearer " + tokenOrg1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"))
                .andExpect(jsonPath("$.acompanhantesPermitidos").value(3));
    }

    @Test
    @DisplayName("Cenário 13: Deve retornar 409 Conflict ao tentar atualizar convidado com e-mail de outro")
    void atualizarConvidado_EmailDuplicadoDeOutroConvidado_RetornaConflict() throws Exception {
        Convidado convidado1 = criarConvidadoMock(eventoOrg1, "Convidado 1", "c1@teste.com");
        Convidado convidado2 = criarConvidadoMock(eventoOrg1, "Convidado 2", "c2@teste.com");
        convidadoRepository.save(convidado1);
        convidadoRepository.save(convidado2);

        AtualizarConvidadoDTO dto = new AtualizarConvidadoDTO(
                "Convidado 1 Atualizado",
                "c2@teste.com", // Tentando usar o e-mail do Convidado 2
                null,
                0,
                null
        );

        mockMvc.perform(put("/eventos/" + eventoOrg1.getId() + "/convidados/" + convidado1.getId())
                        .header("Authorization", "Bearer " + tokenOrg1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Já existe outro convidado cadastrado com o e-mail informado para este evento."));
    }

    @Test
    @DisplayName("Cenário 14: Deve remover convidado com sucesso e retornar 204 No Content")
    void removerConvidado_ComSucesso_RetornaNoContent() throws Exception {
        Convidado convidado = criarConvidadoMock(eventoOrg1, "A Remover", "remover@teste.com");
        convidadoRepository.save(convidado);

        mockMvc.perform(delete("/eventos/" + eventoOrg1.getId() + "/convidados/" + convidado.getId())
                        .header("Authorization", "Bearer " + tokenOrg1))
                .andExpect(status().isNoContent());

        assertThat(convidadoRepository.findById(convidado.getId())).isEmpty();
    }

    @Test
    @DisplayName("Cenário 15: Deve retornar 400 Bad Request ao tentar cadastrar convidado em evento cancelado")
    void criarConvidado_EventoCancelado_RetornaBadRequest() throws Exception {
        Evento eventoCancelado = criarEventoMock(organizador1, "Evento Cancelado", StatusEvento.CANCELADO);
        eventoRepository.save(eventoCancelado);

        CriarConvidadoDTO dto = new CriarConvidadoDTO(
                "Teste Cancelado",
                "cancelado@teste.com",
                null,
                0,
                null
        );

        mockMvc.perform(post("/eventos/" + eventoCancelado.getId() + "/convidados")
                        .header("Authorization", "Bearer " + tokenOrg1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Não é possível realizar operações em convidados de um evento encerrado ou cancelado."));
    }

    private Evento criarEventoMock(Usuario organizador, String nome, StatusEvento status) {
        Evento evento = new Evento();
        evento.setNome(nome);
        evento.setDescricao("Descricao Teste");
        evento.setLocalNome("Local Teste");
        evento.setEndereco("Endereco Teste");
        evento.setDataInicio(LocalDateTime.now().plusDays(5));
        evento.setDataTermino(LocalDateTime.now().plusDays(6));
        evento.setAntecedenciaMinutos(30);
        evento.setToleranciaAtrasoMinutos(15);
        evento.setAcompanhantesPadrao(1);
        evento.setStatus(status);
        evento.setOrganizador(organizador);
        return evento;
    }

    private Convidado criarConvidadoMock(Evento evento, String nome, String email) {
        Convidado convidado = new Convidado();
        convidado.setEvento(evento);
        convidado.setNome(nome);
        convidado.setEmail(email);
        convidado.setTelefone("+5511988887777");
        convidado.setAcompanhantesPermitidos(1);
        convidado.setObservacoes("Observação Mock");
        return convidado;
    }
}
