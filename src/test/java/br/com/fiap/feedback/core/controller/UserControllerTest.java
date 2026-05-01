package br.com.fiap.feedback.core.controller;

import br.com.fiap.feedback.core.domain.User;
import br.com.fiap.feedback.core.domain.UserType;
import br.com.fiap.feedback.core.exception.UserNotFoundException;
import br.com.fiap.feedback.core.inbound.SignInInput;
import br.com.fiap.feedback.core.usecase.SignInUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para a classe UserController")
class UserControllerTest {

    @Mock
    private SignInUsecase signInUsecase;

    private UserController userController;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @BeforeEach
    void setUp() {
        userController = new UserController(signInUsecase);
    }

    @Nested
    @DisplayName("Cenários de Login (SignIn)")
    class SignInScenarios {

        @Test
        @DisplayName("Deve realizar login com sucesso e retornar UserOutput")
        void deveRealizarLoginComSucesso() throws UserNotFoundException {
            // Arrange
            var input = new SignInInput("joao.silva", "senha123");
            var expectedUuid = UUID.randomUUID();
            var mockedUser = new User(expectedUuid, "joao.silva", "senha123", UserType.ALUNO);
            
            given(signInUsecase.signIn(any(User.class))).willReturn(mockedUser);

            // Act
            var output = userController.signIn(input);

            // Assert
            then(signInUsecase).should().signIn(userCaptor.capture());
            var capturedUser = userCaptor.getValue();
            
            assertThat(capturedUser.getUsername()).isEqualTo("joao.silva");
            assertThat(capturedUser.getPassword()).isEqualTo("senha123");
            
            assertThat(output).isNotNull();
            assertThat(output.uuid()).isEqualTo(expectedUuid);
            assertThat(output.username()).isEqualTo("joao.silva");
            assertThat(output.userType()).isEqualTo(UserType.ALUNO);
        }

        @Test
        @DisplayName("Deve propagar UserNotFoundException quando o login falhar")
        void devePropagarExcecaoQuandoLoginFalhar() throws UserNotFoundException {
            // Arrange
            var input = new SignInInput("usuario_invalido", "senha_errada");
            
            given(signInUsecase.signIn(any(User.class))).willThrow(new UserNotFoundException("Usuário não encontrado"));

            // Act & Assert
            assertThatThrownBy(() -> userController.signIn(input))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("Usuário não encontrado");
                    
            then(signInUsecase).should().signIn(any(User.class));
        }
    }
}
