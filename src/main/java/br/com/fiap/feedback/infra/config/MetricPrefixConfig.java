package br.com.fiap.feedback.infra.config;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
public class MetricPrefixConfig {

    @ConfigProperty(name = "feedback.metrics.prefix", defaultValue = "feedback.login.")
    String metricPrefix;

    @Produces
    @Singleton
    public MeterFilter addPrefixToMetrics() {
        return new MeterFilter() {
            @Override
            public Meter.Id map(Meter.Id id) {
                // Evita duplicar o prefixo caso já tenha sido adicionado
                if (id.getName().startsWith(metricPrefix)) {
                    return id;
                }
                return id.withName(metricPrefix + id.getName());
            }
        };
    }
}
