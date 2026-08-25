package br.com.passos.api_convite.domain.usuario.controller;

import br.com.passos.api_convite.AbstractIntegrationTest;
import br.com.passos.api_convite.domain.usuario.dto.LoginDTO;
import br.com.passos.api_convite.domain.usuario.model.Perfil;
import br.com.passos.api_convite.domain.usuario.model.Usuario;
import br.com.passos.api_convite.domain.usuario.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AutenticacaoControllerTest extends AbstractIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private org.springframework.web.context.WebApplicationContext context;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity())
                .build();

        usuarioRepository.deleteAll();

        Usuario admin = new Usuario();
        admin.setNome("Admin Teste");
        admin.setEmail("admin@teste.com");
        admin.setSenhaHash(passwordEncoder.encode("123456"));
        admin.setPerfil(Perfil.ADMIN);

        usuarioRepository.save(admin);
    }

    @Test
    @DisplayName("Deve retornar 200 e o token JWT ao efetuar login com credenciais válidas")
    void efetuarLogin_CredenciaisValidas_RetornaToken() throws Exception {
        LoginDTO loginDTO = new LoginDTO("admin@teste.com", "123456");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.tipo").value("Bearer"));
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden ao efetuar login com senha incorreta")
    void efetuarLogin_SenhaIncorreta_RetornaForbidden() throws Exception {
        LoginDTO loginDTO = new LoginDTO("admin@teste.com", "senhaerrada");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isForbidden());
    }
}
