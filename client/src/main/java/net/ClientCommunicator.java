package net;

import com.google.gson.Gson;
import service.GameService;
import service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ClientCommunicator {


    public <T> T doPost(String urlString, Object requestObject, String endpoint, Class<T> responseClass, String authToken) throws IOException {
        URL url = new URL(urlString + endpoint);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.addRequestProperty("Content-Type", "application/json");

        if (authToken != null && !authToken.isEmpty()) {
            connection.addRequestProperty("Authorization", authToken);
        }

        // Set HTTP request headers, if necessary

        writeBody(requestObject, connection);

        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get HTTP response headers, if necessary
            // Map<String, List<String>> headers = connection.getHeaderFields();

            // OR

            //connection.getHeaderField("Content-Length");

            InputStream responseBody = connection.getInputStream();
            // Read response body from InputStream ...
        }
        else {
            // SERVER RETURNED AN HTTP ERROR

            InputStream responseBody = connection.getErrorStream();
            // Read and process error response body from InputStream ...
        }
        return readBody(connection, responseClass);
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



    public <T> T doGet(String urlString, String authToken, Class<T> responseClass, String output) throws IOException {
        URL url = new URL(urlString + output);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        connection.addRequestProperty("Content-Type", "application/json");

        if (authToken != null && !authToken.isEmpty()) {
            connection.addRequestProperty("Authorization", authToken);
        }

        // Set HTTP request headers, if necessary
        // connection.addRequestProperty("Accept", "text/html");
        // connection.addRequestProperty("Authorization", "fjaklc8sdfjklakl");

        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get HTTP response headers, if necessary
            // Map<String, List<String>> headers = connection.getHeaderFields();

            // OR

            //connection.getHeaderField("Content-Length");

            InputStream responseBody = connection.getInputStream();
            return readBody(connection, responseClass);
            // Read and process response body from InputStream ...
        } else {
            // SERVER RETURNED AN HTTP ERROR

            InputStream responseBody = connection.getErrorStream();
            // Read and process error response body from InputStream ...
        }
        return readBody(connection, responseClass);
    }

    public void doPut(String urlString, Object requestObject, String endpoint, String authToken) throws IOException {
        URL url = new URL(urlString + endpoint);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.addRequestProperty("Content-Type", "application/json");

        if (authToken != null && !authToken.isEmpty()) {
            connection.addRequestProperty("Authorization", authToken);
        }

        // Set HTTP request headers, if necessary

        writeBody(requestObject, connection);

        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get HTTP response headers, if necessary
            // Map<String, List<String>> headers = connection.getHeaderFields();

            // OR

            //connection.getHeaderField("Content-Length");

            InputStream responseBody = connection.getInputStream();
            // Read response body from InputStream ...
        }
        else {
            // SERVER RETURNED AN HTTP ERROR

            InputStream responseBody = connection.getErrorStream();
            // Read and process error response body from InputStream ...
        }
    }


    private void doDelete(String urlString, String endpoint, String authToken) throws IOException {
        URL url = new URL(urlString + endpoint);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("DELETE");
        connection.setDoOutput(true);
        connection.addRequestProperty("Content-Type", "application/json");

        if (authToken != null && !authToken.isEmpty()) {
            connection.addRequestProperty("Authorization", authToken);
        }

        // Set HTTP request headers, if necessary

        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get HTTP response headers, if necessary
            // Map<String, List<String>> headers = connection.getHeaderFields();

            // OR

            //connection.getHeaderField("Content-Length");

            InputStream responseBody = connection.getInputStream();
            // Read response body from InputStream ...
        }
        else {
            // SERVER RETURNED AN HTTP ERROR

            InputStream responseBody = connection.getErrorStream();
            // Read and process error response body from InputStream ...
        }
    }


    public UserService.RegisterResult register(String url, UserService.RegisterRequest request) throws IOException {
        String authToken = null;
        return doPost(url, request, "/user", UserService.RegisterResult.class, authToken);
    }

    public UserService.RegisterResult login(String url, UserService.LoginRequest request) throws IOException {
        String authToken = null;
        return doPost(url,request, "/session", UserService.RegisterResult.class, authToken);
    }

    public GameService.CreateResult createGame(String url, GameService.CreateRequest createRequest, String authToken) throws IOException {
        return doPost(url, createRequest, "/game", GameService.CreateResult.class, authToken);
    }

    public GameService.ListGameResult listGame(String url, String authToken) throws IOException {
        return doGet(url, authToken, GameService.ListGameResult.class, "/game");
    }

    public void joinGame(String url, GameService.JoinGameRequest joinGameRequest, String authToken) throws IOException {
        doPut(url, joinGameRequest, "/game", authToken);
    }

    public void logout(String url, String authToken) throws IOException {
        doDelete(url, "/session", authToken);
    }

    public void clear(String url, String authoken) throws IOException {
        doDelete(url, "/db", authoken);
    }
}
