package br.com.fiap.feedback.infra.repository.entity;

import br.com.fiap.feedback.core.domain.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para a entidade UserEntity")
class UserEntityTest {

    @Nested
    @DisplayName("Construtores e Acessores")
    class ConstructorsAndAccessors {

        @Test
        @DisplayName("Deve criar a entidade usando o construtor principal com sucesso")
        void deveCriarEntidadeComConstrutorPrincipal() {
            // Arrange
            var id = UUID.randomUUID();
            var username = "teste";
            var password = "123";
            var userType = UserType.ALUNO;

            // Act
            var entity = new UserEntity(id, username, password, userType);

            // Assert
            assertThat(entity.getId()).isEqualTo(id);
            assertThat(entity.getUsername()).isEqualTo(username);
            assertThat(entity.getPassword()).isEqualTo(password);
            assertThat(entity.getUserType()).isEqualTo(userType);
        }

        @Test
        @DisplayName("Deve criar a entidade vazia e usar os setters com sucesso")
        void deveCriarEntidadeVaziaEUsarSetters() {
            // Arrange
            var entity = new UserEntity();
            var id = UUID.randomUUID();
            
            // Act
            entity.setId(id);
            entity.setUsername("admin");
            entity.setPassword("senha");
            entity.setUserType(UserType.ADMIN);

            // Assert
            assertThat(entity.getId()).isEqualTo(id);
            assertThat(entity.getUsername()).isEqualTo("admin");
            assertThat(entity.getPassword()).isEqualTo("senha");
            assertThat(entity.getUserType()).isEqualTo(UserType.ADMIN);
        }
    }
}
