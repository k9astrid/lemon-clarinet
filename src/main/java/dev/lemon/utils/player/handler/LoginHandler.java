package dev.lemon.utils.player.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.lemon.utils.player.LoginUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class LoginHandler implements HttpHandler {

    public static Map<String, String> queryToMap(String query){
        Map<String, String> result = new HashMap<String, String>();

        for (String param : query.split("&")) {
            String pair[] = param.split("=");

            if (pair.length>1) {
                result.put(pair[0], pair[1]);

            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }

    @Override
    public void handle(HttpExchange he) {
        try {
            String response = "";

            Map<String,String> params = this.queryToMap(he.getRequestURI().getQuery());
            LoginUtil.getAccessToken(params.get("code"));
            he.sendResponseHeaders(200, 0);
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (IOException ignored){ }
    }
}
