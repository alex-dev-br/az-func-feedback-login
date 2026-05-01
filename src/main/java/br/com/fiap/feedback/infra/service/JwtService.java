package br.com.fiap.feedback.infra.service;

import br.com.fiap.feedback.core.outbound.UserOutput;
import br.com.fiap.feedback.infra.resource.response.JwtBearerToken;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;

@ApplicationScoped
public class JwtService {

    @ConfigProperty(name = "smallrye.jwt.new-token.lifespan")
    private Long lifespan;

    public JwtBearerToken generateJwtBearerToken(UserOutput user) {
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
