package br.com.fiap.feedback.infra.repository;

import br.com.fiap.feedback.core.domain.User;
import br.com.fiap.feedback.core.domain.UserType;
import br.com.fiap.feedback.infra.repository.entity.UserEntity;
import br.com.fiap.feedback.infra.repository.panache.UserPanacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para a classe UserRepository")
class UserRepositoryTest {

    @Mock
    private UserPanacheRepository userPanacheRepository;

    @InjectMocks
    private UserRepository userRepository;

    @Nested
    @DisplayName("Cenários de busca de usuário")
    class FindUserScenarios {

        @Test
        @DisplayName("Deve retornar um User de domínio com sucesso quando a entidade for encontrada")
        void deveRetornarDomainUserQuandoEncontrado() {
            // Given
            var id = UUID.randomUUID();
            var username = "tester";
            var entity = new UserEntity(id, username, "senha", UserType.PROFESSOR);
            
            given(userPanacheRepository.findByUsername(username)).willReturn(Optional.of(entity));

            // When
            Optional<User> result = userRepository.findUserByUsername(username);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getUuid()).isEqualTo(id);
            assertThat(result.get().getUsername()).isEqualTo(username);
            assertThat(result.get().getPassword()).isEqualTo("senha");
            assertThat(result.get().getUserType()).isEqualTo(UserType.PROFESSOR);
            
            then(userPanacheRepository).should().findByUsername(username);
        }

        @Test
        @DisplayName("Deve retornar vazio quando a entidade não for encontrada no banco")
        void deveRetornarVazioQuandoNaoEncontrado() {
            // Given
            var username = "tester_invalido";
            given(userPanacheRepository.findByUsername(anyString())).willReturn(Optional.empty());

            // When
            Optional<User> result = userRepository.findUserByUsername(username);

            // Then
            assertThat(result).isEmpty();
            then(userPanacheRepository).should().findByUsername(username);
        }
    }
}
