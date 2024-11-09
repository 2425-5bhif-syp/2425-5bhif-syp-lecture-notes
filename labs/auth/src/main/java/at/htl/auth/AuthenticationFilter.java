package at.htl.auth;

import at.htl.features.user.SessionRepository;
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
import java.util.UUID;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    SessionRepository sessionRepository;

    @Inject
    Base64AuthenticationParser base64AuthenticationParser;

    @Context
    ResourceInfo resourceInfo;


    //public static final String CREDENTIALS = AuthenticationFilter.class.getSimpleName() + "_CREDENTIALS";
    public static final String USER_ID = AuthenticationFilter.class.getSimpleName() + "_USER_ID";


    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {

        var annotation = resourceInfo.getResourceClass().getAnnotation(AllowAll.class);
        // Mit der ResourceInfo kann auch auf MEthodenAnnotationen zugegriffen werden
        var annotation2 = resourceInfo.getResourceMethod().getAnnotation(AllowAll.class);

        Log.info("Container Request Filter for authentication - Wer bin ich?");

        // Wenn nicht public ...
        if (annotation == null) {

            var cookie = ctx.getCookies().get("Session");
            Log.infof("Session: %s", cookie);

            var sessionId = cookie.getValue();
            Log.infof("Cookie-getValue: %s", sessionId);

            var session = sessionRepository.findByIdOptional(UUID.fromString(sessionId));
            Log.infof("Session: %s", session.isPresent() ? session.get().getId() : "NIX");

            // Nicht verwenden, da der User detached wird -> besser Id oder Name usw auslesen
//            session.ifPresent(s -> {
//                ctx.setProperty(CREDENTIALS, s.getUser());
//            });

            session.ifPresent(
                    s -> {
                        // weil not null in JoinColumn of userid in Session, kann man sicher sein, dass User nie null ist
                        ctx.setProperty(USER_ID, s.getUser().getId());
                    }
            );

            //region Code für BasicAuth
            //            Log.info("Authorization=" + ctx.getHeaderString("Authorization"));
//            Log.info("Cookie="+ctx.getCookies().get("Session"));
//            var credentials = base64AuthenticationParser.parseAuthenticationHeader(ctx.getHeaderString("Authorization"));
//            if (credentials != null) {
//
//                Log.infof("credentials.username=%s, credentials.password=%s"
//                        , credentials.username()
//                        , credentials.password()
//                );
//                ctx.setProperty(CREDENTIALS, credentials);
//            } else {
            // Hier dürfte kein abort() sein, sondern nichts machen und an den AuthorizationFilter delegieren (weiterleiten)
//                ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
//            }
            //endregion

        } else {
            Log.info("@AllowAll detected");
        }
    }
}
