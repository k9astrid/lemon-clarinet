package dev.lemon.api.spotify;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import dev.lemon.api.notification.NotificationType;
import dev.lemon.api.utils.player.ChatUtil;
import dev.lemon.client.main.Lemon;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.pkce.AuthorizationCodePKCERequest;

import java.awt.*;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpotifyAPI {
    public final static String CODE_CHALLENGE = "w6iZIj99vHGtEx_NVl9u3sthTN646vvkiP8OMCGfPmo";
    private final static String CODE_VERIFIER = "NlJx4kD4opk4HY7zBM6WfUHxX7HoF8A2TUhOIPGA74w";
    public final static Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();

    public SpotifyApi api;
    public AuthorizationCodeUriRequest authCodeUriRequest;
    public boolean authenticated;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private HttpServer callbackServer;

    public void build(String text) {
        api = new SpotifyApi.Builder().setClientId(text)
                .setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:4030"))
                .build();

        authCodeUriRequest = api.authorizationCodePKCEUri(CODE_CHALLENGE)
                .code_challenge_method("S256")
                .scope("user-read-playback-state user-read-playback-position user-modify-playback-state user-read-currently-playing")
                .build();
    }

    private final SpotifyCallBack callback = code -> {
        Lemon.INSTANCE.getNotificationManager().call("Connecting to spotify...", NotificationType.INFO);
        AuthorizationCodePKCERequest authCodePKCERequest = api.authorizationCodePKCE(code, CODE_VERIFIER).build();
        Lemon.INSTANCE.getNotificationManager().call("Successfully connected!", NotificationType.SUCCESS);
    };

    public void connect() {
        if (!authenticated) {
            try {
                Desktop.getDesktop().browse(authCodeUriRequest.execute());

                executorService.submit(() -> {
                    try {
                        if (callbackServer != null)
                            callbackServer.stop(0);

                        Lemon.INSTANCE.getNotificationManager().call("Please allow access to the application", NotificationType.INFO);
                        callbackServer = HttpServer.create(new InetSocketAddress(4030), 0);
                        callbackServer.createContext("/", context -> {
                            callback.codeCallback(context.getRequestURI().getQuery().split("=")[1]);
                            final String messageSuccess = context.getRequestURI().getQuery().contains("code")
                                    ? "Successfully authorized!\nYou can now close this window."
                                    : "Unable to authorize to client.\n please re-toggle the module.";
                            context.sendResponseHeaders(200, messageSuccess.length());
                            OutputStream out = context.getResponseBody();
                            out.write(messageSuccess.getBytes());
                            out.close();
                            callbackServer.stop(0);
                        });
                        callbackServer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}