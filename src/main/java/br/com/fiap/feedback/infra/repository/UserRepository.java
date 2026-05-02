package br.com.fiap.feedback.infra.repository;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.feedback.core.domain.User;
import br.com.fiap.feedback.core.gateway.UserGateway;
import br.com.fiap.feedback.infra.repository.entity.UserEntity;
import br.com.fiap.feedback.infra.repository.panache.UserPanacheRepository;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserRepository implements UserGateway {

    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

    private final UserPanacheRepository userPanacheRepository;

    @Inject
    public UserRepository(UserPanacheRepository userPanacheRepository) {
        this.userPanacheRepository = userPanacheRepository;
    }

    @Override
    @Counted(value = "user.repository.find", description = "Quantidade de consultas no repositório de usuários")
    @Timed(value = "user.repository.find.duration", description = "Tempo de consulta no repositório", histogram = true)
    public Optional<User> findUserByUsername(String userName) {
        LOG.info("Buscando usuário pelo username no banco: {}", userName);

        var entity = userPanacheRepository.findByUsername(userName);

        if (entity.isPresent()) {
            LOG.info("Usuário encontrado no banco: {}", userName);
            UserEntity e = entity.get();
            return Optional.of(new User(e.id, e.username, e.password, e.userType));
        } else {
            LOG.warn("Usuário não encontrado no banco: {}", userName);
            return Optional.empty();
        }
    }
}
