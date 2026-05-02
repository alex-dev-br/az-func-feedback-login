package br.com.fiap.feedback.infra.service;

import br.com.fiap.feedback.core.outbound.UserOutput;
import br.com.fiap.feedback.infra.resource.response.JwtBearerToken;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

@ApplicationScoped
public class JwtService {

    private static final Logger LOG = LoggerFactory.getLogger(JwtService.class);

    @ConfigProperty(name = "smallrye.jwt.new-token.lifespan")
    private Long lifespan;

    @Counted(value = "jwt.generate", description = "Quantidade de tokens JWT gerados")
    @Timed(value = "jwt.generate.duration", description = "Tempo de geração do token JWT", histogram = true)
    public JwtBearerToken generateJwtBearerToken(UserOutput user) {
        LOG.info("Gerando token JWT para o usuário: {}", user.username());
        
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(lifespan);

        String token = Jwt.subject(user.username())
                .groups(user.userType().name())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .sign();
        return new JwtBearerToken(token, now.getEpochSecond(), expiresAt.getEpochSecond());
    }
}
