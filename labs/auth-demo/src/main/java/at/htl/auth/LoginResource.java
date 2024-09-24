package at.htl.auth;

import at.htl.auth.auth.Credentials;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Path("/login")
@PermitAll
public class LoginResource {
    public static record LoginResponse(String token) { }

    @POST
    public Response login(Credentials credentials) {
        var cred = String.format("%s:%s", credentials.username(), credentials.password());
        var encoded = Base64.getEncoder().encodeToString(cred.getBytes());
        var response = new LoginResponse(encoded);

        return Response.ok(response).build();
    }
}
