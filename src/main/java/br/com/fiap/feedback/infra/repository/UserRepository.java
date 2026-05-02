package br.com.fiap.feedback.infra.repository;

import br.com.fiap.feedback.core.domain.User;
import br.com.fiap.feedback.core.domain.UserType;
import br.com.fiap.feedback.core.gateway.UserGateway;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepository implements UserGateway {

    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

    private final List<User> users = List.of(
        new User(UUID.randomUUID(), "aluno", "senha123", UserType.ALUNO),
        new User(UUID.randomUUID(), "admin", "senha123", UserType.ADMIN),
        new User(UUID.randomUUID(), "professor", "senha123", UserType.PROFESSOR)
    );

    @Override
    @Counted(value = "user.repository.find", description = "Quantidade de consultas no repositório de usuários")
    @Timed(value = "user.repository.find.duration", description = "Tempo de consulta no repositório", histogram = true)
    public Optional<User> findUserByUsername(String userName) {
        LOG.info("Buscando usuário pelo username: {}", userName);
        
        var user = users.stream().filter(u -> u.getUsername().equals(userName)).findFirst();
        
        if (user.isPresent()) {
            LOG.info("Usuário encontrado no repositório: {}", userName);
        } else {
            LOG.warn("Usuário não encontrado no repositório: {}", userName);
        }
        
        return user;
    }
}
