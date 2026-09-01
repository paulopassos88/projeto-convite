package br.com.passos.api_convite.domain.evento.controller;

import br.com.passos.api_convite.AbstractIntegrationTest;
import br.com.passos.api_convite.domain.evento.dto.AtualizarEventoDTO;
import br.com.passos.api_convite.domain.evento.dto.CriarEventoDTO;
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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

class EventoControllerTest extends AbstractIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

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

    @BeforeEach
    void setUp() {
        this.mockMvc = webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();

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
        admin.setNome("Admin Geral");
        admin.setEmail("admin@teste.com");
        admin.setSenhaHash(passwordEncoder.encode("123456"));
        admin.setPerfil(Perfil.ADMIN);
        usuarioRepository.save(admin);

        tokenOrg1 = tokenService.gerarToken(organizador1);
        tokenOrg2 = tokenService.gerarToken(organizador2);
        tokenAdmin = tokenService.gerarToken(admin);
    }

    @Test
    @DisplayName("Deve criar evento com sucesso para usuário com perfil ORGANIZADOR")
    void criarEvento_ComOrganizador_RetornaCreated() throws Exception {
        CriarEventoDTO dto = new CriarEventoDTO(
                "Casamento Real",
                "Festa de casamento",
                "Espaço Nobre",
                "Rua das Flores, 123",
                "https://maps.google.com/?q=EspacoNobre",
                LocalDateTime.now().plusDays(10).withHour(19).withMinute(0),
                LocalDateTime.now().plusDays(11).withHour(2).withMinute(0),
                60,
                30,
                1
        );

        mockMvc.perform(post("/eventos")
                        .header("Authorization", "Bearer " + tokenOrg1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Casamento Real"))
                .andExpect(jsonPath("$.organizadorNome").value("Organizador 1"))
                .andExpect(jsonPath("$.status").value("ATIVO"));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao tentar criar evento com data de término anterior ao início")
    void criarEvento_DataTerminoInvalida_RetornaBadRequest() throws Exception {
        CriarEventoDTO dto = new CriarEventoDTO(
                "Evento Invalido",
                "Descricao",
                "Local",
                "Endereco",
                null,
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(5), // Termina antes de começar!
                0,
                0,
                0
        );

        mockMvc.perform(post("/eventos")
                        .header("Authorization", "Bearer " + tokenOrg1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden ao tentar criar evento sem autenticação")
    void criarEvento_NaoAutenticado_RetornaForbidden() throws Exception {
        CriarEventoDTO dto = new CriarEventoDTO(
                "Evento Anonimo",
                "Descricao",
                "Local",
                "Endereco",
                null,
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(11),
                0,
                0,
                0
        );

        mockMvc.perform(post("/eventos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve listar apenas os próprios eventos quando autenticado como ORGANIZADOR")
    void listarEventos_ComoOrganizador_RetornaApenasEventosProprios() throws Exception {
        Evento eventoOrg1 = criarEventoMock(organizador1, "Evento do Org 1");
        Evento eventoOrg2 = criarEventoMock(organizador2, "Evento do Org 2");

        eventoRepository.save(eventoOrg1);
        eventoRepository.save(eventoOrg2);

        mockMvc.perform(get("/eventos")
                        .header("Authorization", "Bearer " + tokenOrg1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Evento do Org 1"));
    }

    @Test
    @DisplayName("Deve listar todos os eventos quando autenticado como ADMIN")
    void listarEventos_ComoAdmin_RetornaTodosEventos() throws Exception {
        Evento eventoOrg1 = criarEventoMock(organizador1, "Evento 1");
        Evento eventoOrg2 = criarEventoMock(organizador2, "Evento 2");

        eventoRepository.save(eventoOrg1);
        eventoRepository.save(eventoOrg2);

        mockMvc.perform(get("/eventos")
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden quando organizador tenta acessar evento de outro organizador por ID")
    void buscarPorId_EventoDeOutroOrganizador_RetornaForbidden() throws Exception {
        Evento eventoOrg2 = criarEventoMock(organizador2, "Evento Privado Org 2");
        eventoRepository.save(eventoOrg2);

        mockMvc.perform(get("/eventos/" + eventoOrg2.getId())
                        .header("Authorization", "Bearer " + tokenOrg1))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve atualizar evento com sucesso quando for o próprio organizador")
    void atualizarEvento_ComoDono_RetornaOk() throws Exception {
        Evento evento = criarEventoMock(organizador1, "Nome Antigo");
        eventoRepository.save(evento);

        AtualizarEventoDTO dto = new AtualizarEventoDTO(
                "Nome Novo",
                "Nova Descricao",
                evento.getLocalNome(),
                evento.getEndereco(),
                evento.getLinkMaps(),
                evento.getDataInicio(),
                evento.getDataTermino(),
                evento.getAntecedenciaMinutos(),
                evento.getToleranciaAtrasoMinutos(),
                evento.getAcompanhantesPadrao(),
                StatusEvento.ATIVO
        );

        mockMvc.perform(put("/eventos/" + evento.getId())
                        .header("Authorization", "Bearer " + tokenOrg1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Novo"));
    }

    @Test
    @DisplayName("Deve cancelar evento com sucesso (HTTP 204)")
    void cancelarEvento_ComoDono_RetornaNoContent() throws Exception {
        Evento evento = criarEventoMock(organizador1, "Evento a Cancelar");
        eventoRepository.save(evento);

        mockMvc.perform(delete("/eventos/" + evento.getId())
                        .header("Authorization", "Bearer " + tokenOrg1))
                .andExpect(status().isNoContent());
    }

    private Evento criarEventoMock(Usuario organizador, String nome) {
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
        evento.setStatus(StatusEvento.ATIVO);
        evento.setOrganizador(organizador);
        return evento;
    }
}
