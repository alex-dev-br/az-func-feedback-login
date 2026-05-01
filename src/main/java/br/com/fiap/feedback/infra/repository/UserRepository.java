package br.com.fiap.feedback.infra.repository;

import br.com.fiap.feedback.core.domain.User;
import br.com.fiap.feedback.core.domain.UserType;
import br.com.fiap.feedback.core.gateway.UserGateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepository implements UserGateway {

    private final List<User> users = List.of(
        new User(UUID.randomUUID(), "aluno", "senha123", UserType.ALUNO),
        new User(UUID.randomUUID(), "admin", "senha123", UserType.ADMIN),
        new User(UUID.randomUUID(), "professor", "senha123", UserType.PROFESSOR)
    );

    @Override
    public Optional<User> findUserByUsername(String userName) {
        return users.stream().filter(u -> u.getUsername().equals(userName)).findFirst();
    }
}
