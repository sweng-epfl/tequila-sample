package ch.epfl.sweng.tequila;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Sample for Tequila OAuth2 authentication.
 *
 * NOTE: This is simplified code for demo purposes.
 *       In a real Android app, the redirect URI would be a custom scheme ("myapp://..."),
 *       the app would open the code request URL in the Android browser and wait for the user
 *       to come back automatically via the redirect.
 *       Then, the app would make a request to its server (which holds the client secret,
 *       since a secret cannot be kept on a mobile client as anybody can reverse-engineer it)
 *       to get the OAuth2 tokens.
 *
 * NOTE: The code is not ideal; it does not handle IO exceptions (and associated resource disposal),
 *       does just enough to handle URL parameters, it is not testable, and so on.
 *       Do not copy/paste this and call it a day; understand what it does, and rewrite it cleanly.
 *
 * @author Solal Pirelli
 */
public final class Sample {
    public static void main(String[] args) throws IOException {
        OAuth2Config config = readConfig();
        String codeRequestUrl = AuthClient.createCodeRequestUrl(config);

        System.out.println(codeRequestUrl);
        String redirectUri = read("Go to the above URL, authenticate, then enter the redirect URI");

        String code = AuthClient.extractCode(redirectUri);
        Map<String, String> tokens = AuthServer.fetchTokens(config, code);

        Profile profile = AuthServer.fetchProfile(tokens.get("Tequila.profile"));
        System.out.println(profile);
    }

    private static OAuth2Config readConfig() throws IOException {
        return new OAuth2Config(new String[]{"Tequila.profile"}, read("Client ID"), read("Client secret"), read("Redirect URI"));
    }

    private static String read(String prompt) throws IOException {
        System.out.print(prompt + ": ");
        return new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
    }
}