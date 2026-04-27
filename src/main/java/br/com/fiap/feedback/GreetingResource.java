package br.com.fiap.feedback;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;

public class GreetingResource {

    @FunctionName("func-feedback-login")
    public HttpResponseMessage signIn (
            @HttpTrigger(name = "authentication", route = "sign-in", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext executionContext) {
        executionContext.getLogger().info("Java HTTP trigger processed a request.");
        return request.createResponseBuilder(HttpStatus.OK).body("{\"message\": \"Hello, World!\"}").build();
    }

    @FunctionName("func-feedback-sign-up")
    public HttpResponseMessage signUp (
            @HttpTrigger(name = "authentication", route = "sign-up", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext executionContext) {
        executionContext.getLogger().info("Java HTTP trigger processed a request.");
        return request.createResponseBuilder(HttpStatus.OK).body("{\"message\": \"Hello, World!\"}").build();
    }
}
