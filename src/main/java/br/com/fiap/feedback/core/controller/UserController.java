package br.com.fiap.feedback.core.controller;

import br.com.fiap.feedback.core.domain.User;
import br.com.fiap.feedback.core.exception.UserNotFoundException;
import br.com.fiap.feedback.core.inbound.SignInInput;
import br.com.fiap.feedback.core.outbound.UserOutput;
import br.com.fiap.feedback.core.presenter.UserPresenter;
import br.com.fiap.feedback.core.usecase.SignInUsecase;

public class UserController {

    private final SignInUsecase signInUsecase;

    public UserController(SignInUsecase signInUsecase) {
        this.signInUsecase = signInUsecase;
    }

    public UserOutput signIn(SignInInput signInInput) throws UserNotFoundException {
        var user = new User(signInInput.username(), signInInput.password());
        var foundUser = signInUsecase.signIn(user);
        return UserPresenter.toOutput(foundUser);
    }
}
