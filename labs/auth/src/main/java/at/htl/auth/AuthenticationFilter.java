package at.htl.auth;

import io.quarkus.logging.Log;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    Base64AuthenticationParser base64AuthenticationParser;
    public static final String CREDENTIALS = AuthenticationFilter.class.getSimpleName() + "_CREDENTIALS";

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        Log.info("Container Request Filter for authentication - Wer bin ich?");

        //Log.info(ctx.getHeaderString("Authorization"));
        var credentials = base64AuthenticationParser.parseAuthenticationHeader(ctx.getHeaderString("Authorization"));
        if (credentials != null) {

            Log.infof("credentials.username=%s, credentials.password=%s"
                    , credentials.username()
                    , credentials.password()
            );
            ctx.setProperty(CREDENTIALS, credentials);
        } else {
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());

        }
    }
}
