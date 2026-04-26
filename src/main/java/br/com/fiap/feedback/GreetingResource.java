package br.com.fiap.feedback;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import reactor.core.publisher.Mono;

@Path("/authetication")
public class GreetingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Mono<String> hello() {
        return Mono.just("{\"message\": \"hello\"}");
    }

}
