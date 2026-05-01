package br.com.fiap.feedback.core.presenter;

import br.com.fiap.feedback.core.domain.User;
import br.com.fiap.feedback.core.domain.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para a classe UserPresenter")
class UserPresenterTest {

    @Nested
    @DisplayName("Conversões")
    class Conversions {

        @Test
        @DisplayName("Deve converter um usuário para UserOutput com sucesso")
        void deveConverterUserParaOutputComSucesso() {
            // Arrange
            var uuid = UUID.randomUUID();
            var username = "usuario123";
            var password = "password123";
            var userType = UserType.ALUNO;
            var user = new User(uuid, username, password, userType);

            // Act
            var output = UserPresenter.toOutput(user);

            // Assert
            assertThat(output).isNotNull();
            assertThat(output.uuid()).isEqualTo(uuid);
            assertThat(output.username()).isEqualTo(username);
            assertThat(output.userType()).isEqualTo(userType);
        }

        @Test
        @DisplayName("Deve retornar nulo quando o usuário for nulo")
        void deveRetornarNuloQuandoUserForNulo() {
            // Act
            var output = UserPresenter.toOutput(null);

            // Assert
            assertThat(output).isNull();
        }
        
        @Test
        @DisplayName("Deve instanciar a classe mesmo com construtor privado (Apenas para cobertura)")
        void deveInstanciarClasse() throws Exception {
            var constructor = UserPresenter.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            var instance = constructor.newInstance();
            assertThat(instance).isNotNull();
        }
    }
}
