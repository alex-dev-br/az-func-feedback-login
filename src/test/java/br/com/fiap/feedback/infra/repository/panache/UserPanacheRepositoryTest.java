package br.com.fiap.feedback.infra.repository.panache;

import br.com.fiap.feedback.infra.repository.entity.UserEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@DisplayName("Testes para o repositório UserPanacheRepository")
class UserPanacheRepositoryTest {

    @Inject
    UserPanacheRepository repository;

    @Nested
    @DisplayName("Cenários de Busca (findByUsername)")
    class FindByUsernameScenarios {

        @Test
        @DisplayName("Deve retornar um usuário presente no banco pelo username")
        void deveRetornarUsuarioPresenteNoBanco() {
            // Arrange
            var username = "aluno"; // Usuário pré-inserido pelo Flyway V1

            // Act
            Optional<UserEntity> result = repository.findByUsername(username);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getUsername()).isEqualTo(username);
            assertThat(result.get().getId()).isNotNull();
            assertThat(result.get().getPassword()).isEqualTo("senha123");
            assertThat(result.get().getUserType().name()).isEqualTo("ALUNO");
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando o usuário não existir no banco")
        void deveRetornarOptionalVazioQuandoUsuarioNaoExistir() {
            // Arrange
            var username = "usuario_inexistente_123";

            // Act
            Optional<UserEntity> result = repository.findByUsername(username);

            // Assert
            assertThat(result).isEmpty();
        }
    }
}
