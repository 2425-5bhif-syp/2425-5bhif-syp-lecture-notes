package at.htl.auth;

import io.quarkus.logging.Log;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

import static at.htl.auth.Base64AuthenticationParser.Credentials;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        Log.info("Container Request Filter for authorization - Was darf ich?");

        var credentials = (Credentials) ctx.getProperty(AuthenticationFilter.CREDENTIALS);
        Log.infof("Was darf ich\ncredentials.username=%s, credentials.password=%s"
                , credentials.username()
                , credentials.password()
        );

    }
}
