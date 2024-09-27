package at.htl.features.user;

import at.htl.auth.AllowAll;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Objects;

import static at.htl.auth.Base64AuthenticationParser.*;

@AllowAll
@Path("login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    @Inject
    UserRepository userRepository;

    @Inject
    SessionRepository sessionRepository;


    @Transactional
    @POST
    public Response login(Credentials credentials) {
        Log.info("Login - I was here!");

        var users = userRepository
                .list("name",credentials.username());

        if(users.isEmpty() || !Objects.equals(users.getFirst().getPassword(), credentials.password()))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        Session newSession = new Session(users.getFirst());

        sessionRepository.persist(newSession);

        return Response
                .ok()
                .header("Set-Cookie", String.format("Session=%s", newSession.getId()))
                .build();

    }

}
