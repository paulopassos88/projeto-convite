package br.com.passos.api_convite.domain.convite.controller;

import br.com.passos.api_convite.AbstractIntegrationTest;
import br.com.passos.api_convite.domain.convidado.model.Convidado;
import br.com.passos.api_convite.domain.convidado.repository.ConvidadoRepository;
import br.com.passos.api_convite.domain.convite.model.Convite;
import br.com.passos.api_convite.domain.convite.model.StatusConvite;
import br.com.passos.api_convite.domain.convite.model.StatusEnvio;
import br.com.passos.api_convite.domain.convite.repository.ConviteRepository;
import br.com.passos.api_convite.domain.evento.model.Evento;
import br.com.passos.api_convite.domain.evento.model.StatusEvento;
import br.com.passos.api_convite.domain.evento.repository.EventoRepository;
import br.com.passos.api_convite.domain.usuario.model.Perfil;
import br.com.passos.api_convite.domain.usuario.model.Usuario;
import br.com.passos.api_convite.domain.usuario.repository.UsuarioRepository;
import br.com.passos.api_convite.infra.security.TokenService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

class ConviteControllerTest extends AbstractIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ConviteRepository conviteRepository;

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
    private Convidado convidadoEvento1;
    private Convidado convidadoEvento2;

    @BeforeEach
    void setUp() {
        this.mockMvc = webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();

        conviteRepository.deleteAll();
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

        eventoOrg1 = criarEventoMock(organizador1, "Casamento Real", StatusEvento.ATIVO);
        eventoRepository.save(eventoOrg1);

        eventoOrg2 = criarEventoMock(organizador2, "Meetup de IA", StatusEvento.ATIVO);
        eventoRepository.save(eventoOrg2);

        convidadoEvento1 = criarConvidadoMock(eventoOrg1, "Alice Silva", "alice@teste.com");
        convidadoRepository.save(convidadoEvento1);

        convidadoEvento2 = criarConvidadoMock(eventoOrg2, "Bob Souza", "bob@teste.com");
        convidadoRepository.save(convidadoEvento2);
    }

    @Test
    @DisplayName("Cenário 1: Deve gerar convite com sucesso para convidado vinculado ao evento (201 Created)")
    void gerarConvite_ComSucesso_RetornaCreated() throws Exception {
        mockMvc.perform(post("/eventos/" + eventoOrg1.getId() + "/convidados/" + convidadoEvento1.getId() + "/convite")
                        .header("Authorization", "Bearer " + tokenOrg1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.eventoId").value(eventoOrg1.getId().toString()))
                .andExpect(jsonPath("$.convidadoId").value(convidadoEvento1.getId().toString()))
                .andExpect(jsonPath("$.nomeConvidado").value("Alice Silva"))
                .andExpect(jsonPath("$.emailConvidado").value("alice@teste.com"))
                .andExpect(jsonPath("$.codigo").isNotEmpty())
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.statusEnvio").value("NAO_ENVIADO"));

        assertThat(conviteRepository.findAllByEventoId(eventoOrg1.getId(), null)).hasSize(1);
    }

    @Test
    @DisplayName("Cenário 2: Deve retornar 409 Conflict ao tentar gerar convite duplicado para o mesmo convidado")
    void gerarConvite_Duplicado_RetornaConflict() throws Exception {
        criarConvitePersistido(eventoOrg1, convidadoEvento1, "COD12345");

        mockMvc.perform(post("/eventos/" + eventoOrg1.getId() + "/convidados/" + convidadoEvento1.getId() + "/convite")
                        .header("Authorization", "Bearer " + tokenOrg1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Já existe um convite gerado para este convidado no evento informado."));
    }

    @Test
    @DisplayName("Cenário 3: Deve retornar 404 Not Found ao tentar gerar convite para convidado inexistente")
    void gerarConvite_ConvidadoInexistente_RetornaNotFound() throws Exception {
        mockMvc.perform(post("/eventos/" + eventoOrg1.getId() + "/convidados/" + UUID.randomUUID() + "/convite")
                        .header("Authorization", "Bearer " + tokenOrg1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Cenário 4: Deve retornar 404 Not Found ao tentar gerar convite para convidado de outro evento")
    void gerarConvite_ConvidadoDeOutroEvento_RetornaNotFound() throws Exception {
        mockMvc.perform(post("/eventos/" + eventoOrg1.getId() + "/convidados/" + convidadoEvento2.getId() + "/convite")
                        .header("Authorization", "Bearer " + tokenOrg1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Cenário 5: Deve retornar 400 Bad Request ao tentar gerar convite em evento cancelado")
    void gerarConvite_EventoCancelado_RetornaBadRequest() throws Exception {
        Evento eventoCancelado = criarEventoMock(organizador1, "Evento Cancelado", StatusEvento.CANCELADO);
        eventoRepository.save(eventoCancelado);

        Convidado convidadoCancelado = criarConvidadoMock(eventoCancelado, "Teste", "teste@cancelado.com");
        convidadoRepository.save(convidadoCancelado);

        mockMvc.perform(post("/eventos/" + eventoCancelado.getId() + "/convidados/" + convidadoCancelado.getId() + "/convite")
                        .header("Authorization", "Bearer " + tokenOrg1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Não é possível realizar operações de convite em um evento encerrado ou cancelado."));
    }

    @Test
    @DisplayName("Cenário 6: Deve retornar 403 Forbidden para requisições não autenticadas")
    void gerarConvite_NaoAutenticado_RetornaForbidden() throws Exception {
        mockMvc.perform(post("/eventos/" + eventoOrg1.getId() + "/convidados/" + convidadoEvento1.getId() + "/convite")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Cenário 7: Deve retornar 403 Forbidden quando organizador tenta gerar convite para evento de outro organizador")
    void gerarConvite_OutroOrganizador_RetornaForbidden() throws Exception {
        mockMvc.perform(post("/eventos/" + eventoOrg1.getId() + "/convidados/" + convidadoEvento1.getId() + "/convite")
                        .header("Authorization", "Bearer " + tokenOrg2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Cenário 8: Deve permitir que usuário ADMIN gere convite para qualquer evento (201 Created)")
    void gerarConvite_ComoAdmin_RetornaCreated() throws Exception {
        mockMvc.perform(post("/eventos/" + eventoOrg1.getId() + "/convidados/" + convidadoEvento1.getId() + "/convite")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.convidadoId").value(convidadoEvento1.getId().toString()));
    }

    @Test
    @DisplayName("Cenário 9: Deve listar convites de um evento com paginação (200 OK)")
    void listarConvites_Paginado_RetornaOk() throws Exception {
        Convidado convidadoB = criarConvidadoMock(eventoOrg1, "Bruno", "bruno@teste.com");
        convidadoRepository.save(convidadoB);

        criarConvitePersistido(eventoOrg1, convidadoEvento1, "COD11111");
        criarConvitePersistido(eventoOrg1, convidadoB, "COD22222");

        mockMvc.perform(get("/eventos/" + eventoOrg1.getId() + "/convites")
                        .header("Authorization", "Bearer " + tokenOrg1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].codigo").exists());
    }

    @Test
    @DisplayName("Cenário 10: Deve filtrar convites por status")
    void listarConvites_ComFiltroStatus_RetornaApenasFiltrados() throws Exception {
        criarConvitePersistido(eventoOrg1, convidadoEvento1, "COD11111");

        mockMvc.perform(get("/eventos/" + eventoOrg1.getId() + "/convites?status=PENDENTE")
                        .header("Authorization", "Bearer " + tokenOrg1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].status").value("PENDENTE"));

        mockMvc.perform(get("/eventos/" + eventoOrg1.getId() + "/convites?status=UTILIZADO")
                        .header("Authorization", "Bearer " + tokenOrg1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @DisplayName("Cenário 11: Deve buscar convite por ID")
    void buscarConvite_PorId_RetornaOk() throws Exception {
        Convite convite = criarConvitePersistido(eventoOrg1, convidadoEvento1, "CODSEARCH");

        mockMvc.perform(get("/eventos/" + eventoOrg1.getId() + "/convites/" + convite.getId())
                        .header("Authorization", "Bearer " + tokenOrg1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(convite.getId().toString()))
                .andExpect(jsonPath("$.codigo").value("CODSEARCH"));
    }

    @Test
    @DisplayName("Cenário 12: Deve buscar convite pelo ID do convidado")
    void buscarConvite_PorConvidadoId_RetornaOk() throws Exception {
        Convite convite = criarConvitePersistido(eventoOrg1, convidadoEvento1, "CODCONV");

        mockMvc.perform(get("/eventos/" + eventoOrg1.getId() + "/convidados/" + convidadoEvento1.getId() + "/convite")
                        .header("Authorization", "Bearer " + tokenOrg1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(convite.getId().toString()))
                .andExpect(jsonPath("$.convidadoId").value(convidadoEvento1.getId().toString()))
                .andExpect(jsonPath("$.codigo").value("CODCONV"));
    }

    @Test
    @DisplayName("Cenário 13: Deve retornar 404 Not Found para convite com ID inexistente")
    void buscarConvite_IdInexistente_RetornaNotFound() throws Exception {
        mockMvc.perform(get("/eventos/" + eventoOrg1.getId() + "/convites/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + tokenOrg1))
                .andExpect(status().isNotFound());
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

    private Convite criarConvitePersistido(Evento evento, Convidado convidado, String codigo) {
        Convite convite = new Convite();
        convite.setEvento(evento);
        convite.setConvidado(convidado);
        convite.setCodigo(codigo);
        convite.setStatus(StatusConvite.PENDENTE);
        convite.setStatusEnvio(StatusEnvio.NAO_ENVIADO);
        return conviteRepository.save(convite);
    }
}
