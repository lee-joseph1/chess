package ui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServerFacade {
    private String serverUrl;
    public ServerFacade(String url) {
        this.serverUrl = url;
    }
    public AuthData register(String username, String password, String email) throws IOException {
//        UserData userData = new UserData(username, password, email);
        try {
            String url = serverUrl + "/user";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            String json = "{\"username\":\"%s\",\"password\":\"%s\"," +
                    "\"email\":\"%s\"}" + username + "," + password + "," + email;
            try (OutputStream oStream = connection.getOutputStream()) {
                oStream.write(json.getBytes());
                oStream.flush();
            }
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Registered user: " + username + "!");
                return Login(username, password);
            } else {
                try (InputStream iStream = connection.getErrorStream()) {
                    String error = new String(iStream.readAllBytes());
                    JsonObject jsonObject = JsonParser.parseString(error).getAsJsonObject();
                    System.out.println(jsonObject.get("message").getAsString());
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Error registering: " + ex.getMessage());
        }
        return new AuthData(null, null);
    }

    public AuthData Login(String username, String password) throws IOException {
        try {
            String url = serverUrl + "/user";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            String json = "{\"username\":\"%s\",\"password\":\"%s\"}" + username + "," + password;
            try (OutputStream oStream = connection.getOutputStream()) {
                oStream.write(json.getBytes());
                oStream.flush();
            }
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Logged in user: " + username + "!");
                InputStream iStream = connection.getInputStream();
                String response = new String(iStream.readAllBytes());
                JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
                return new AuthData(jsonObject.get("authToken").getAsString(), username);
            } else {
                InputStream iStream = connection.getErrorStream();
                String error = new String(iStream.readAllBytes());
                JsonObject jsonObject = JsonParser.parseString(error).getAsJsonObject();
                System.out.println(jsonObject.get("message").getAsString());
            }
        } catch (Exception ex) {
            System.out.println("Error logging in: " + ex.getMessage());
        }
        return new AuthData(null, null);
    }

    //logout, createGame, listGames, joinGame
}
