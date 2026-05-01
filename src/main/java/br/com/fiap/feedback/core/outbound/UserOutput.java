package br.com.fiap.feedback.core.outbound;

import br.com.fiap.feedback.core.domain.UserType;

import java.util.UUID;

public record UserOutput(UUID uuid, String username, UserType userType) {}
