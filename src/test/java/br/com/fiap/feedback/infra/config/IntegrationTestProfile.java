package br.com.fiap.feedback.infra.config;

import java.util.Map;

import io.quarkus.test.junit.QuarkusTestProfile;

public class IntegrationTestProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of("smallrye.jwt.sign.key.location", "rsaPrivateKey.pem.example",
                "smallrye.jwt.encrypt.key.location", "publicKey.pem.example");
    }
}
