package br.com.fiap.feedback.infra.service;

import br.com.fiap.feedback.core.domain.UserType;
import br.com.fiap.feedback.core.outbound.UserOutput;
import br.com.fiap.feedback.infra.resource.response.JwtBearerToken;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para a classe JwtService")
class JwtServiceTest {

    private JwtService jwtService;
    private final Long lifespan = 3600L;

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();
        // Injeta o valor do lifespan via reflexão, simulando a injeção do @ConfigProperty
        Field field = JwtService.class.getDeclaredField("lifespan");
        field.setAccessible(true);
        field.set(jwtService, lifespan);
    }

    @Nested
    @DisplayName("Geração de Token")
    class TokenGeneration {

        @Test
        @DisplayName("Deve gerar um JwtBearerToken válido com os dados do usuário")
        void deveGerarTokenComSucesso() {
            // Given
            var user = new UserOutput(UUID.randomUUID(), "joao.silva", UserType.ALUNO);
            String tokenMock = "mocked-jwt-token";

            try (MockedStatic<Jwt> jwtMockedStatic = mockStatic(Jwt.class)) {
                JwtClaimsBuilder claimsBuilder = mock(JwtClaimsBuilder.class);
                
                jwtMockedStatic.when(() -> Jwt.subject(anyString())).thenReturn(claimsBuilder);
                given(claimsBuilder.groups(anyString())).willReturn(claimsBuilder);
                given(claimsBuilder.issuedAt(any(Instant.class))).willReturn(claimsBuilder);
                given(claimsBuilder.expiresAt(any(Instant.class))).willReturn(claimsBuilder);
                given(claimsBuilder.sign()).willReturn(tokenMock);

                // When
                JwtBearerToken result = jwtService.generateJwtBearerToken(user);

                // Then
                assertThat(result).isNotNull();
                assertThat(result.token()).isEqualTo(tokenMock);
                assertThat(result.type()).isEqualTo("Bearer");
                assertThat(result.expiresAt()).isEqualTo(result.issuedAt() + lifespan);

                jwtMockedStatic.verify(() -> Jwt.subject("joao.silva"));
                verify(claimsBuilder).groups(UserType.ALUNO.name());
                verify(claimsBuilder).issuedAt(any(Instant.class));
                verify(claimsBuilder).expiresAt(any(Instant.class));
                verify(claimsBuilder).sign();
            }
        }
    }
}
