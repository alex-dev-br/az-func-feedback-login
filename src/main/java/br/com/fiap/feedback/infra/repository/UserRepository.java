package br.com.fiap.feedback.infra.repository;

import br.com.fiap.feedback.core.domain.User;
import br.com.fiap.feedback.core.gateway.UserGateway;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements UserGateway, PanacheRepositoryBase<UserEntity, UUID> {

    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

    @Override
    @Counted(value = "user.repository.find", description = "Quantidade de consultas no repositório de usuários")
    @Timed(value = "user.repository.find.duration", description = "Tempo de consulta no repositório", histogram = true)
    public Optional<User> findUserByUsername(String userName) {
        LOG.info("Buscando usuário pelo username no banco: {}", userName);
        
        Optional<UserEntity> entity = find("username", userName).firstResultOptional();
        
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
