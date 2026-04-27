package br.com.fiap.feedback;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;

public class GreetingResource {

    @FunctionName("func-feedback-sign-in")
    public HttpResponseMessage signIn (
            @HttpTrigger(name = "sign-in", route = "sign-in", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext executionContext) {
        executionContext.getLogger().info("Fazendo login ...");
        return request.createResponseBuilder(HttpStatus.OK).body("{\"message\": \"sign-in\"}").build();
    }

    @FunctionName("func-feedback-sign-up")
    public HttpResponseMessage signUp (
            @HttpTrigger(name = "sign-up", route = "sign-up", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext executionContext) {
        executionContext.getLogger().info("Criando usuário ...");
        return request.createResponseBuilder(HttpStatus.OK).body("{\"message\": \"sign-up\"}").build();
    }
}
