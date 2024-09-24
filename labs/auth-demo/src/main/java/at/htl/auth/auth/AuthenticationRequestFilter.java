package at.htl.auth.auth;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@Provider
public class AuthenticationRequestFilter implements ContainerRequestFilter {
    @Inject
    Logger log;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        var annotation = Arrays.stream(requestContext.getRequest().getClass().getAnnotations())
                .filter(a -> a.getClass().equals(PermitAll.class))
                .findFirst()
                .orElse(null);

        requestContext.getHeaders().forEach((key, value) -> {
            log.infof("header: %s -> %s", key, value);
        });

        var authHeader = requestContext.getHeaders().getFirst("Authorization");

        if (authHeader == null || !isValidAuthToken(authHeader)) {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        }

        log.info("HIER BIN ICH");
    }

    public boolean isValidAuthToken(String header) {
        String base64Token = header.replaceFirst("Basic ", "");
        String plainToken = new String(Base64.getDecoder().decode(base64Token));
        String[] credentials = plainToken.split(":");

        return header.startsWith("Basic ")
                && credentials.length == 2
                && credentials[0].equals("admin")
                && credentials[1].equals("password");
    }
}
