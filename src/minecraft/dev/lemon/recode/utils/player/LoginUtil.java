package dev.lemon.recode.utils.player;

import dev.lemon.recode.utils.Util;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import optifine.Json;

public class LoginUtil implements Util {

private static String clientId = "";
private static String clientSecret = "";

public static URI uriForLogin;

    static {
        try {
            uriForLogin = new URI("https://login.live.com/oauth20_authorize.srf?client_id=" + clientId + "&response_type=code&redirect_uri=localhost:8080/login&scope=XboxLive.signin%20offline_access&state=NOT_NEEDED");
        } catch (URISyntaxException ignored) {
        }
    }

    public static void logIn(String clientId){
        try {
            Desktop.getDesktop().browse(uriForLogin);
        } catch (IOException ignored){
            System.out.println("AAA");
        }


    }
    public static void getAccessToken(String code){
        String params  = "client_id="+clientId+"&client_secret="+clientSecret+"&code="+code+"&grant_type=authorization_code&redirect_uri=http://localhost:8080/part2";

        try {
            URL url = new URL("https://login.live.com/oauth20_token.srf");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Accept", "application/json");

            byte[] data = params.getBytes( StandardCharsets.UTF_8 );

            con.setDoOutput(true);
            con.setRequestProperty("charset", "utf-8");
            con.setRequestProperty("Content-Length", Integer.toString(data.length));
            con.setUseCaches(false);

            try(DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write( data );
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;

                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                signInToXboxLive(response.toString());
            }
        } catch (Exception ignored){

        }
    }

    public static void signInToXboxLive(String response) {

        try {
            JsonParser parser = new JsonParser();
            JsonObject responseParsed = parser.parse(response).getAsJsonObject();
            String jsonInputString = "{ \"Properties\": { \"AuthMethod\": \"RPS\", \"SiteName\": \"user.auth.xboxlive.com\", \"RpsTicket\": \"" + responseParsed.get("access_token").getAsString() + "\"}, \"RelyingParty\": \"http://auth.xboxlive.com\", \"TokenType\": \"JWT\" }";

            URL url = new URL("https://login.live.com/oauth20_token.srf");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");


            byte[] data = jsonInputString.getBytes(StandardCharsets.UTF_8);

            con.setDoOutput(true);
            con.setRequestProperty("charset", "utf-8");

            try (OutputStream os = con.getOutputStream()) {
                os.write(data, 0, data.length);
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder res = new StringBuilder();
                String responseLine = null;

                while ((responseLine = br.readLine()) != null) {
                    res.append(responseLine.trim());
                }

                responseParsed = parser.parse(res.toString()).getAsJsonObject();

            }
            if (responseParsed.get("Token").getAsString() != null) {
                getXSTSToken(responseParsed.get("Token").getAsString(), responseParsed.get("uhs").getAsString());
            }

        } catch (Exception ignored) {

        }
    }

    public static void getXSTSToken(String token, String userHash){
        JsonParser parser = new JsonParser();
        JsonObject responseParsed;
        try {
                String jsonInputString = "{ \"Properties\": { \"SandboxId\": \"RETAIL\", \"UserTokens\": [ "+token+" ] }, \"RelyingParty\": \"rp://api.minecraftservices.com/\", \"TokenType\": \"JWT\" }";

                URL url = new URL("https://login.live.com/oauth20_token.srf");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");


                byte[] data = jsonInputString.getBytes(StandardCharsets.UTF_8);

                con.setDoOutput(true);
                con.setRequestProperty("charset", "utf-8");

                try (OutputStream os = con.getOutputStream()) {
                    os.write(data, 0, data.length);
                }

                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder res = new StringBuilder();
                    String responseLine = null;

                    while ((responseLine = br.readLine()) != null) {
                        res.append(responseLine.trim());
                    }
                    responseParsed = parser.parse(res.toString()).getAsJsonObject();
                }

                if (con.getResponseCode() == 200){
                    System.out.println("User Hash Match? "+responseParsed.get("uhs").getAsString().equals(userHash));
                    getBearerToken(responseParsed.get("Token").getAsString(), responseParsed.get("uhs").getAsString());
                }

            } catch (Exception ignored){

            }
    }

    public static void getBearerToken(String token, String userHash){
            
    }
}

