package at.htl.auth;

import at.htl.features.user.UserRepository;
import io.quarkus.logging.Log;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

import static at.htl.auth.Base64AuthenticationParser.Credentials;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Inject
    UserRepository userRepository;

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        Log.info("Container Request Filter for authorization - Was darf ich?");

        var userId = (Long) ctx.getProperty(AuthenticationFilter.USER_ID);

        if (userId != null) {

            var user = userRepository.findById(userId);

            Log.infof("Der User ist %s %s"
                    , user.getName()
                    , user.getPassword()
            );

        }
    }
}
