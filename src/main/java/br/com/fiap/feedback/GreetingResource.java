package br.com.fiap.feedback;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import reactor.core.publisher.Mono;

@Path("/")
public class GreetingResource {

    @GET
    @Path("sign-in")
    @Produces(MediaType.APPLICATION_JSON)
    public String signIn() {
        return "{\"message\": \"sign-in\"}";
    }

//    @GET
//    @Path("async/sign-in")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Mono<String> signInAsync() {
//        return Mono.just("{\"message\": \"sign-in\"}");
//    }
//
//    @GET
//    @Path("sign-up")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String signUp() {
//        return "{\"message\": \"sign-up\"}";
//    }
//
//    @GET
//    @Path("async/sign-up")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Mono<String> signUpAsync() {
//        return Mono.just("{\"message\": \"sign-up\"}");
//    }
}
