package br.com.passos.api_convite.domain.convite.service;

import br.com.passos.api_convite.domain.convite.repository.ConviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class GeradorCodigoConviteService {

    private static final String ALFABETO = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static final int TAMANHO_PADRAO = 8;
    private static final int MAX_TENTATIVAS = 5;

    private final SecureRandom secureRandom = new SecureRandom();
    private final ConviteRepository conviteRepository;

    public String gerarCodigo() {
        return gerarCodigo(TAMANHO_PADRAO);
    }

    public String gerarCodigo(int tamanho) {
        StringBuilder sb = new StringBuilder(tamanho);
        for (int i = 0; i < tamanho; i++) {
            int index = secureRandom.nextInt(ALFABETO.length());
            sb.append(ALFABETO.charAt(index));
        }
        return sb.toString();
    }

    public String gerarCodigoUnico() {
        for (int tentativa = 0; tentativa < MAX_TENTATIVAS; tentativa++) {
            String codigo = gerarCodigo();
            if (!conviteRepository.existsByCodigo(codigo)) {
                return codigo;
            }
        }
        throw new IllegalStateException("Não foi possível gerar um código único após " + MAX_TENTATIVAS + " tentativas.");
    }
}
