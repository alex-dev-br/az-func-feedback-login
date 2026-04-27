package br.com.fiap.feedback;

import com.microsoft.azure.functions.annotation.FunctionName;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import reactor.core.publisher.Mono;

@Path("/authetication")
public class GreetingResource {

    @FunctionName("func-feedback-login")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Mono<String> hello() {
        return Mono.just("{\"message\": \"hello\"}");
    }
}
