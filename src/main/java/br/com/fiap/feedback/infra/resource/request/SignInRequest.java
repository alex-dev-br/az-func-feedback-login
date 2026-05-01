package br.com.fiap.feedback.infra.resource.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInRequest(
    @NotBlank @Size(min = 3, max = 20) String username,
    @NotBlank @Size(min = 6, max = 30) String password
) {}
