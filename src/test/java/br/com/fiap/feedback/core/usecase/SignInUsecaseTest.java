package br.com.fiap.feedback.core.usecase;

import br.com.fiap.feedback.core.domain.User;
import br.com.fiap.feedback.core.domain.UserType;
import br.com.fiap.feedback.core.exception.UserNotFoundException;
import br.com.fiap.feedback.core.gateway.PasswordEncoderGateway;
import br.com.fiap.feedback.core.gateway.UserGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para o Caso de Uso de Login (SignInUsecase)")
class SignInUsecaseTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private PasswordEncoderGateway passwordEncoderGateway;

    @InjectMocks
    private SignInUsecase signInUsecase;

    @Test
    @DisplayName("Deve realizar o login com sucesso quando as credenciais estiverem corretas")
    void deveRealizarLoginComSucessoQuandoCredenciaisCorretas() throws UserNotFoundException {
        // Arrange
        String username = "testuser";
        String rawPassword = "rawpassword";
        String encodedPassword = "encodedpassword";
        UUID userId = UUID.randomUUID();

        User inputUser = new User(null, username, rawPassword, UserType.ALUNO);
        User foundUser = new User(userId, username, encodedPassword, UserType.ALUNO);

        when(userGateway.findUserByUsername(username)).thenReturn(Optional.of(foundUser));
        when(passwordEncoderGateway.matches(rawPassword, encodedPassword)).thenReturn(true);

        // Act
        User result = signInUsecase.signIn(inputUser);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getUuid()).isEqualTo(userId);
        verify(userGateway, times(1)).findUserByUsername(username);
        verify(passwordEncoderGateway, times(1)).matches(rawPassword, encodedPassword);
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando o usuário não existir")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Arrange
        String username = "nonexistentuser";
        String rawPassword = "password";

        User inputUser = new User(null, username, rawPassword, UserType.ALUNO);

        when(userGateway.findUserByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> signInUsecase.signIn(inputUser))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");

        verify(userGateway, times(1)).findUserByUsername(username);
        verify(passwordEncoderGateway, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando a senha estiver incorreta")
    void deveLancarExcecaoQuandoSenhaIncorreta() {
        // Arrange
        String username = "testuser";
        String rawPassword = "wrongpassword";
        String encodedPassword = "correctencodedpassword";
        UUID userId = UUID.randomUUID();

        User inputUser = new User(null, username, rawPassword, UserType.ALUNO);
        User foundUser = new User(userId, username, encodedPassword, UserType.ALUNO);

        when(userGateway.findUserByUsername(username)).thenReturn(Optional.of(foundUser));
        when(passwordEncoderGateway.matches(rawPassword, encodedPassword)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> signInUsecase.signIn(inputUser))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");

        verify(userGateway, times(1)).findUserByUsername(username);
        verify(passwordEncoderGateway, times(1)).matches(rawPassword, encodedPassword);
    }
}
