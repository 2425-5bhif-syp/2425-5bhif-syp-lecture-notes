package at.htl.auth;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Base64;
import java.util.regex.Pattern;

@ApplicationScoped
public class Base64AuthenticationParser {
public static final Pattern basicAuthenticationPattern = Pattern.compile("Basic (.*)");
    public static record Credentials(String username, String password) {

    }

    //  Basic dXNlcjpwYXNzd2Q=
    public Credentials parseAuthenticationHeader(String header) {
        Credentials credentials = null;
        var matcher = basicAuthenticationPattern.matcher(header);
        boolean matchFound = matcher.find();
        if (matchFound) {
            var encodedCredentials = matcher.group(1);
            var decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials));
            Log.info(decodedCredentials);
            var usernameAndPassword = decodedCredentials.split(":");
            credentials = new Credentials(usernameAndPassword[0], usernameAndPassword[1]);
        }
        return credentials;
    }

}
