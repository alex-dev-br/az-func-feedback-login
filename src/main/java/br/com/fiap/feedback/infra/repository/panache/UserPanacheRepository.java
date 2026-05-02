package br.com.fiap.feedback.infra.repository.panache;

import java.util.Optional;
import java.util.UUID;

import br.com.fiap.feedback.infra.repository.entity.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserPanacheRepository implements PanacheRepositoryBase<UserEntity, UUID> {

    public Optional<UserEntity> findByUsername(String username) {
        return find("username", username).firstResultOptional();
    }
}
