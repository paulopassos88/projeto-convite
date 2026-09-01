package br.com.passos.api_convite.domain.usuario.controller;

import br.com.passos.api_convite.AbstractIntegrationTest;
import br.com.passos.api_convite.domain.usuario.dto.CadastroUsuarioDTO;
import br.com.passos.api_convite.domain.usuario.model.Perfil;
import br.com.passos.api_convite.domain.usuario.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsuarioControllerTest extends AbstractIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private org.springframework.web.context.WebApplicationContext context;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private br.com.passos.api_convite.domain.evento.repository.EventoRepository eventoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity())
                .build();

        eventoRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve cadastrar usuário e retornar 201 quando o chamador for ADMIN")
    @WithMockUser(roles = "ADMIN")
    void cadastrar_SendoAdmin_RetornaCriado() throws Exception {
        CadastroUsuarioDTO dto = new CadastroUsuarioDTO(
                "Organizador Teste",
                "organizador@teste.com",
                "123456",
                Perfil.ORGANIZADOR
        );

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Organizador Teste"))
                .andExpect(jsonPath("$.email").value("organizador@teste.com"))
                .andExpect(jsonPath("$.perfil").value("ORGANIZADOR"));
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden quando o chamador não for ADMIN")
    @WithMockUser(roles = "ORGANIZADOR")
    void cadastrar_NaoSendoAdmin_RetornaForbidden() throws Exception {
        CadastroUsuarioDTO dto = new CadastroUsuarioDTO(
                "Invasor Teste",
                "invasor@teste.com",
                "123456",
                Perfil.CONTROLADOR
        );

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden quando não houver usuário autenticado")
    void cadastrar_NaoAutenticado_RetornaForbidden() throws Exception {
        CadastroUsuarioDTO dto = new CadastroUsuarioDTO(
                "Anonimo Teste",
                "anonimo@teste.com",
                "123456",
                Perfil.CONTROLADOR
        );

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }
}
