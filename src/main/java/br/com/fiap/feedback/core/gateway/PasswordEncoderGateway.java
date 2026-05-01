package br.com.fiap.feedback.core.gateway;

public interface PasswordEncoderGateway {
    boolean matches(String rawPassword, String encodedPassword);
}
