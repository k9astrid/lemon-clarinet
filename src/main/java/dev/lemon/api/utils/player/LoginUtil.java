package dev.lemon.api.utils.player;

import com.sun.net.httpserver.HttpServer;
import dev.lemon.api.utils.IMethods;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.lemon.api.utils.player.handler.LoginHandler;
import net.minecraft.util.Session;

public class LoginUtil implements IMethods {

    private static String clientId = "526b3e37-6aa9-45ef-989f-ed84bfb47f18";
    private static String clientSecret = "aY78Q~1zman1vukdI.ZzirYvGsWkxY0pjBOLFcEB";

    public static URI uriForLogin;

    static {
        try {
            uriForLogin = new URI("https://login.live.com/oauth20_authorize.srf?client_id=" + clientId + "&response_type=code&redirect_uri=http://localhost:8080/login&scope=XboxLive.signin%20offline_access&state=NOT_NEEDED");
        } catch (URISyntaxException ignored) { }
    }

    public static void login(){
        try {
            Desktop.getDesktop().browse(uriForLogin);

            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            System.out.println("server started at " + 8080);
            server.createContext("/login", new LoginHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException e){
            System.out.println(e.getStackTrace());
        }
    }

    public static void getAccessToken(String code){
        String params  = "client_id="+clientId+"&client_secret="+clientSecret+"&code="+code+"&grant_type=authorization_code&redirect_uri=http://localhost:8080/login";
        System.out.println("Getting access token...");
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
            String jsonInputString = "{ \"Properties\": { \"AuthMethod\": \"RPS\", \"SiteName\": \"user.auth.xboxlive.com\", \"RpsTicket\": \"d=" +responseParsed.get("access_token").getAsString() + "\"}, \"RelyingParty\": \"http://auth.xboxlive.com\", \"TokenType\": \"JWT\" }";
            System.out.println("Signing in to Xbox Live... ");

            URL url = new URL("https://user.auth.xboxlive.com/user/authenticate");

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

            } catch (Exception e){
                e.printStackTrace();
            }
            getXSTSToken(responseParsed.get("Token").getAsString(), responseParsed.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getXSTSToken(String token, String userHash){
        System.out.println("Getting XSTS token...");
        JsonParser parser = new JsonParser();
        JsonObject responseParsed;

        try {
                String jsonInputString = " {\n" +
                        " \t\"Properties\": {\n" +
                        " \t\t\"SandboxId\": \"RETAIL\",\n" +
                        " \t\t\"UserTokens\": [\""+token+"\"]\n" +
                        " \t},\n" +
                        " \t\"RelyingParty\": \"rp://api.minecraftservices.com/\",\n" +
                        " \t\"TokenType\": \"JWT\"\n" +
                        " }";

                URL url = new URL("https://xsts.auth.xboxlive.com/xsts/authorize");

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
                    System.out.println("User Hash Match? "+responseParsed.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString().equals(userHash));

                    getBearerToken(responseParsed.get("Token").getAsString(), responseParsed.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString());
                } else {
                    System.out.println(responseParsed.getAsString());
                }

            } catch (Exception e){
            System.out.println("AAAAAAA");
            e.printStackTrace();
            }
    }

    public static void getBearerToken(String token, String userHash){
        System.out.println("Getting bearer token...");
        try {
            JsonParser parser = new JsonParser();
            String jsonInputString = "{ \"identityToken\" : \"XBL3.0 x="+userHash+";"+token+"\" }";
            System.out.println(jsonInputString);
            URL url = new URL("https://api.minecraftservices.com/authentication/login_with_xbox");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");


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

                JsonObject responseParsed = parser.parse(res.toString()).getAsJsonObject();
                System.out.println("Bearer token: "+responseParsed.get("access_token").getAsString());
                createNewSession(responseParsed.get("access_token").getAsString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e){

        }
    }

    public static void createNewSession(String accessToken){
        JsonParser parser = new JsonParser();
        JsonObject responseParsed;
        try {
            URL url = new URL("https://api.minecraftservices.com/minecraft/profile");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer "+accessToken);
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder res = new StringBuilder();
                String responseLine = null;

                while ((responseLine = br.readLine()) != null) {
                    res.append(responseLine.trim());
                }

                responseParsed = parser.parse(res.toString()).getAsJsonObject();
                if (con.getResponseCode() == 200){
                    mc.session = new Session(responseParsed.get("name").getAsString(), responseParsed.get("id").getAsString(), accessToken, "legacy");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e){

        }
    }
}

