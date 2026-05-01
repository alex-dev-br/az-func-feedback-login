package br.com.fiap.feedback.infra.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import br.com.fiap.feedback.core.controller.UserController;
import br.com.fiap.feedback.core.domain.UserType;
import br.com.fiap.feedback.core.exception.UserNotFoundException;
import br.com.fiap.feedback.core.outbound.UserOutput;
import br.com.fiap.feedback.infra.config.IntegrationTestProfile;
import br.com.fiap.feedback.infra.resource.request.SignInRequest;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(IntegrationTestProfile.class)
@DisplayName("Testes para o Recurso de Login (SignInResource)")
class SignInResourceTest {

    @InjectMock
    UserController userController;

    @Nested
    @DisplayName("Cenários de Login")
    class LoginScenarios {

        @Test
        @DisplayName("Deve realizar login com sucesso e retornar 200 com o token")
        void deveRealizarLoginComSucesso() throws UserNotFoundException {
            // Arrange
            var request = new SignInRequest("usuario.valido", "senha123");
            var userOutput = new UserOutput(UUID.randomUUID(), "usuario.valido", UserType.ALUNO);

            given(userController.signIn(any())).willReturn(userOutput);

            // Act & Assert
            given()
                    .contentType("application/json")
                    .body(request)
                    .when()
                    .post("/sign-in")
                    .then()
                    .statusCode(200)
                    .body("token", notNullValue())
                    .body("type", equalTo("Bearer"))
                    .body("issuedAt", notNullValue())
                    .body("expiresAt", notNullValue());
        }

        @Test
        @DisplayName("Deve retornar 401 quando o usuário não for encontrado")
        void deveRetornar401QuandoUsuarioNaoEncontrado() throws UserNotFoundException {
            // Arrange
            var request = new SignInRequest("usuario.inexistente", "senha123");

            given(userController.signIn(any())).willThrow(new UserNotFoundException("Usuário não encontrado"));

            // Act & Assert
            given()
                    .contentType("application/json")
                    .body(request)
                    .when()
                    .post("/sign-in")
                    .then()
                    .statusCode(401);
        }
    }

    @Nested
    @DisplayName("Cenários de Validação")
    class ValidationScenarios {

        @Test
        @DisplayName("Deve retornar 400 quando o username for inválido (muito curto)")
        void deveRetornar400QuandoUsernameCurto() {
            // Arrange
            var request = new SignInRequest("us", "senha123");

            // Act & Assert
            given()
                    .contentType("application/json")
                    .body(request)
                    .when()
                    .post("/sign-in")
                    .then()
                    .statusCode(400)
                    .body("title", equalTo("Erro na validação dos campos"))
                    .body("details", notNullValue());
        }

        @Test
        @DisplayName("Deve retornar 400 quando o password for nulo")
        void deveRetornar400QuandoPasswordNulo() {
            // Arrange
            var body = "{\"username\":\"usuario.valido\"}";

            // Act & Assert
            given()
                    .contentType("application/json")
                    .body(body)
                    .when()
                    .post("/sign-in")
                    .then()
                    .statusCode(400);
        }
    }
}
