package br.com.fiap.feedback.infra.resource;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.azure.security.keyvault.secrets.SecretClient;

import br.com.fiap.feedback.core.controller.UserController;
import br.com.fiap.feedback.core.inbound.SignInInput;
import br.com.fiap.feedback.infra.resource.request.SignInRequest;
import br.com.fiap.feedback.infra.service.JwtService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class SignInResource {

    private final UserController userController;
    private final JwtService jwtService;

    @Inject
    public SignInResource(UserController userController, JwtService jwtService) {
        this.userController = userController;
        this.jwtService = jwtService;
    }

    @POST
    @PermitAll
    @Path("sign-in")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signIn(@Valid SignInRequest request) {
        var userOutput = userController.signIn(new SignInInput(request.username(), request.password()));
        var jwtBearerToken = jwtService.generateJwtBearerToken(userOutput);
        return Response.ok().entity(jwtBearerToken).build();
    }

}
