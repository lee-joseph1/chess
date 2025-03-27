package server;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.HashMap;

public class ServerFacade {
    private final String serverUrl;
    public ServerFacade(String url) {
        this.serverUrl = url;
    }
    public AuthData register(String[] args) throws Exception {
        UserData user = new UserData(args[0], args[1], args[2]);
        return this.makeRequest("POST", "/user", user, AuthData.class, null);
    }

    public AuthData login(String[] args) throws Exception {
        UserData user = new UserData(args[0], args[1], null);
        return makeRequest("POST", "/session", user, AuthData.class, null);
    }

    public void logout(AuthData auth) throws Exception {//filler for now
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("authorization", auth.authToken());
        makeRequest("DELETE", "/session", null, null, hashMap);
    }

    public GameData create(AuthData auth, String gameName) throws Exception {
        HashMap<String, String> games = new HashMap<>();
        games.put("authorization", auth.authToken());
        GameData game = new GameData(0, null, null, gameName, new ChessGame());
        var path = "/game";
        return makeRequest("POST", path, game, GameData.class, games);
    }

    public ListResponse2 list(AuthData auth) throws Exception {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("authorization", auth.authToken());
        return makeRequest("GET", "/game", null, ListResponse2.class, hashMap);
    }

    public void join(AuthData auth, String[] args, HashMap<Integer, GameData> chessGames) throws Exception {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("authorization", auth.authToken());
        int number = Integer.parseInt(args[0]);
        GameData game = chessGames.get(number);
        HashMap<String, Object> request = new HashMap<>();
        request.put("playerColor", args[1]);
        request.put("gameID", game.gameID());
        makeRequest("PUT", "/game", request, null, hashMap);
    }

    public AuthData clear() throws Exception {
        return makeRequest("DELETE", "/db", null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, HashMap<String, String> hashMap) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (hashMap != null) {
                for (HashMap.Entry<String, String> arg : hashMap.entrySet()) {
                    http.setRequestProperty(arg.getKey(), arg.getValue());
                }
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            //throw new Exception("Error code: 500" + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws Exception {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            statusCases(http, status);
        }
    }

    private static void statusCases(HttpURLConnection http, int status) throws Exception {
        switch(status) {
            case 400 -> {
                throw new Exception("Error: bad request");
            }
            case 401 -> {
                throw new Exception("Error: unauthorized");
            }
            case 403 -> {
                throw new Exception("Error: already taken");
            }
            default -> {
                try (InputStream respErr = http.getErrorStream()) {
                    if (respErr != null) {
                        throw new Exception(String.valueOf(respErr));
                    }
                }
                throw new Exception("other failure: " + status);
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
