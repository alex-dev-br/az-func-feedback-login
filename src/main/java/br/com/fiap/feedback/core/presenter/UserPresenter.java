package br.com.fiap.feedback.core.presenter;

import br.com.fiap.feedback.core.domain.User;
import br.com.fiap.feedback.core.outbound.UserOutput;

public class UserPresenter {

    private UserPresenter() {}

    public static UserOutput toOutput(User user) {
        return user == null ? null : new UserOutput(user.getUuid(), user.getUsername(), user.getUserType());
    }
}
