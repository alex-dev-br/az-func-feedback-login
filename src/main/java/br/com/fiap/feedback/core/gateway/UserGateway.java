package br.com.fiap.feedback.core.gateway;

import br.com.fiap.feedback.core.domain.User;

import java.util.Optional;

public interface UserGateway {
    Optional<User> findUserByUsername(String userName);
}
