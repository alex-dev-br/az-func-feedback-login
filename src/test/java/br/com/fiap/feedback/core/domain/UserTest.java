package br.com.fiap.feedback.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para a Entidade User")
class UserTest {

    @Test
    @DisplayName("Deve criar um usuário com todos os campos usando o construtor completo")
    void deveCriarUsuarioComConstrutorCompleto() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        String username = "usuario.teste";
        String password = "senhaSegura123";
        UserType userType = UserType.ALUNO;

        // Act
        User user = new User(uuid, username, password, userType);

        // Assert
        assertThat(user.getUuid()).isEqualTo(uuid);
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getUserType()).isEqualTo(userType);
    }

    @Test
    @DisplayName("Deve criar um usuário com o construtor simplificado")
    void deveCriarUsuarioComConstrutorSimplificado() {
        // Arrange
        String username = "usuario.aluno";
        UserType userType = UserType.ALUNO;

        // Act
        User user = new User(username, userType);

        // Assert
        assertThat(user.getUuid()).isNull();
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isNull();
        assertThat(user.getUserType()).isEqualTo(userType);
    }

    @Test
    @DisplayName("Deve criar um usuário com o construtor de username e password")
    void deveCriarUsuarioComConstrutorDeUsernameEPassword() {
        // Arrange
        String username = "usuario.novo";
        String password = "outraSenha456";

        // Act
        User user = new User(username, password);

        // Assert
        assertThat(user.getUuid()).isNull();
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getUserType()).isNull();
    }
}
