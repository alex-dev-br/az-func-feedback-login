package br.com.fiap.feedback.infra.config;

import java.util.List;

public record ErrorResponse(String title, List<String> details) {}