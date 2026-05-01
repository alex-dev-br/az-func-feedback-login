package br.com.fiap.feedback.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para o Enum UserType")
class UserTypeTest {

    @Test
    @DisplayName("Deve verificar se os valores do enum UserType estão definidos corretamente")
    void deveVerificarValoresDoEnumUserType() {
        // Assert
        assertThat(UserType.ALUNO).isNotNull();
        assertThat(UserType.PROFESSOR).isNotNull();
        assertThat(UserType.ADMIN).isNotNull();

        assertThat(UserType.ALUNO.name()).isEqualTo("ALUNO");
        assertThat(UserType.PROFESSOR.name()).isEqualTo("PROFESSOR");
        assertThat(UserType.ADMIN.name()).isEqualTo("ADMIN");

        assertThat(UserType.values()).hasSize(3);
    }
}
