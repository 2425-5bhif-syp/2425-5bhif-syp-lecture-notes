package at.htl.features.login;

import at.htl.auth.Base64AuthenticationParser;
import io.quarkus.logging.Log;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static at.htl.auth.Base64AuthenticationParser.*;

@PermitAll
@Path("login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    @POST
    public Response login(Credentials credentials) {
        Log.info("Login - I was here!");
        return Response.ok().build();
    }

}
