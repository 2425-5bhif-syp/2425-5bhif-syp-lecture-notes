package at.htl.auth;

import io.quarkus.logging.Log;
import jakarta.annotation.Priority;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.Arrays;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    Base64AuthenticationParser base64AuthenticationParser;

    @Context
    ResourceInfo resourceInfo;


    public static final String CREDENTIALS = AuthenticationFilter.class.getSimpleName() + "_CREDENTIALS";


    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {

        var annotation = resourceInfo.getResourceClass().getAnnotation(AllowAll.class);


        Log.info("Container Request Filter for authentication - Wer bin ich?");

        if (annotation == null) {

            Log.info("Authorization=" + ctx.getHeaderString("Authorization"));
            Log.info("Cookie="+ctx.getCookies().get("Session"));
//            var credentials = base64AuthenticationParser.parseAuthenticationHeader(ctx.getHeaderString("Authorization"));
//            if (credentials != null) {
//
//                Log.infof("credentials.username=%s, credentials.password=%s"
//                        , credentials.username()
//                        , credentials.password()
//                );
//                ctx.setProperty(CREDENTIALS, credentials);
//            } else {
//                ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
//            }
        } else {
            Log.info("@AllowAll detected");
        }
    }
}
