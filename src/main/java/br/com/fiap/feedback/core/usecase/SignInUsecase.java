package br.com.fiap.feedback.core.usecase;

import br.com.fiap.feedback.core.exception.UserNotFoundException;
import br.com.fiap.feedback.core.gateway.PasswordEncoderGateway;
import br.com.fiap.feedback.core.gateway.UserGateway;
import br.com.fiap.feedback.core.domain.User;

public class SignInUsecase {

    private final UserGateway userGateway;
    private final PasswordEncoderGateway passwordEncoderGateway;

    public SignInUsecase(UserGateway userGateway, PasswordEncoderGateway passwordEncoderGateway) {
        this.userGateway = userGateway;
        this.passwordEncoderGateway = passwordEncoderGateway;
    }

    public User signIn(User user) throws UserNotFoundException {
        var foundUser = userGateway.findUserByUsername(user.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        var matches = passwordEncoderGateway.matches(user.getPassword(), foundUser.getPassword());
        if (!matches) {
            throw new UserNotFoundException("User not found");
        }
        return foundUser;
    }
}
