package br.com.fiap.feedback.infra.resource.response;

public record JwtBearerToken(String type, String token, Long issuedAt, Long expiresAt) {
    public JwtBearerToken(String token, Long issuedAt, Long expiresIn) {
        this("Bearer", token, issuedAt, expiresIn);
    }
}
