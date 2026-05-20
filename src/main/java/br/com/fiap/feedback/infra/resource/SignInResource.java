package br.com.fiap.feedback.infra.resource;

import br.com.fiap.feedback.core.controller.UserController;
import br.com.fiap.feedback.core.inbound.SignInInput;
import br.com.fiap.feedback.infra.resource.request.SignInRequest;
import br.com.fiap.feedback.infra.service.JwtService;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(SignInResource.class);

    @Inject
    public SignInResource(UserController userController, JwtService jwtService) {
        this.userController = userController;
        this.jwtService = jwtService;
    }

    @POST
    @PermitAll
    @Path("sign-in")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(value = "signin.attempted", description = "Quantidade de tentativas de login (com e sem sucesso)")
    @Timed(value = "signin.duration", description = "Tempo de processamento do login", histogram = true)
    public Response signIn(@Valid SignInRequest request) {
        LOG.info("Iniciando tentativa de login para o usuário: {}", request.username());

        var userOutput = userController.signIn(new SignInInput(request.username(), request.password()));

        LOG.info("Login efetuado com sucesso para o usuário: {}", request.username());

        var jwtBearerToken = jwtService.generateJwtBearerToken(userOutput);
        return Response.ok().entity(jwtBearerToken).build();
    }

}
