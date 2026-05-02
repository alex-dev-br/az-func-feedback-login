package br.com.fiap.feedback.infra.config;

import br.com.fiap.feedback.core.controller.UserController;
import br.com.fiap.feedback.core.gateway.PasswordEncoderGateway;
import br.com.fiap.feedback.core.gateway.UserGateway;
import br.com.fiap.feedback.core.usecase.SignInUsecase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class Producers {

    @Produces
    @ApplicationScoped
    public PasswordEncoderGateway passwordEncoderGatewayProduce() {
        return String::equals;
    }

    @Produces
    @ApplicationScoped
    public SignInUsecase signInUsecaseProduce(UserGateway userGateway, PasswordEncoderGateway passwordEncoderGateway) {
        return new SignInUsecase(userGateway, passwordEncoderGateway);
    }

    @Produces
    @ApplicationScoped
    public UserController userControllerProduce(SignInUsecase signInUsecase) {
        return new UserController(signInUsecase);
    }
}
