package org.example.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import okhttp3.*;
import org.example.client.requests.CreateOrderRequest;
import org.example.client.requests.LoginRequest;
import org.example.client.requests.RegisterRequest;
import org.example.client.requests.RegisterResponse;
import org.example.client.responses.*;
import org.example.entities.Tomography;
import org.example.exceptions.AuthException;
import org.example.exceptions.RestException;

public class HttpClient {
    private OkHttpClient okHttpClient;
    private String baseUrl = "http://d-iagnostic.com.ar:8080/api"; //System.getenv("API_URL") + "/api";
    private MediaType mediaType;
    private ObjectMapper objectMapper;
    public HttpClient() {
        this.okHttpClient = new OkHttpClient();
        this.mediaType = MediaType.parse("application/json; charset=utf-8");
        this.objectMapper = CustomObjectMapper.getObjectMapper();
    }

    @SneakyThrows
    public LoginResponse login(LoginRequest payload) {
        var body = RequestBody.create(getBody(payload), mediaType);

        var request = new Request.Builder()
                .url(getUrl("users/login"))
                .post(body)
                .build();

        var response = this.okHttpClient.newCall(request).execute();
        System.out.println("Login response: " + response);
        return getData(response, LoginResponse.class);
    }

    @SneakyThrows
    public RegisterResponse register(RegisterRequest payload) {
        var body = RequestBody.create(getBody(payload), mediaType);

        var request = new Request.Builder()
                .url(getUrl("users"))
                .post(body)
                .build();

        var response = this.okHttpClient.newCall(request).execute();
        return getData(response, RegisterResponse.class);
    }

    @SneakyThrows
    public CreateOrderResponse createOrder(CreateOrderRequest payload, String token) {
        var body = RequestBody.create(getBody(payload), mediaType);

        var request = new Request.Builder()
                .url(getUrl("orders"))
                .header("Authorization", "Bearer " + token)
                .post(body)
                .build();

        var response = this.okHttpClient.newCall(request).execute();
        return getData(response, CreateOrderResponse.class);
    }

    @SneakyThrows
    public Tomography searchReport(String codeReport,String token) {
        var url = String.format("tomographies/report/%s", codeReport);

        var request = new Request.Builder()
                .url(getUrl(url))
                .header("Authorization", "Bearer " + token)
                .get()
                .build();

        var response = this.okHttpClient.newCall(request).execute();
        return getData(response, Tomography.class);
    }


    @SneakyThrows
    public OrdersResponse getOrders(String token) {
        var request = new Request.Builder()
                .url(getUrl("orders"))
                .header("Authorization", "Bearer " + token)
                .get()
                .build();

        var response = this.okHttpClient.newCall(request).execute();
        return getData(response, OrdersResponse.class);
    }


    private String getUrl(String uri) {
        return baseUrl + "/" + uri;
    }

    @SneakyThrows
    private byte[] getBody(Object o) {
        return objectMapper.writeValueAsBytes(o);
    }

    @SneakyThrows
    private <T>T getData(Response response, Class<T> type) {
        if (response.code() == 401) {
            throw new AuthException("Hay un problema con tu sesión");
        }
        if (response.code() >= 400) {
            var error = objectMapper.readValue(response.body().bytes(), ErrorResponse.class);
            throw new RestException(error.getMessage());
        }
        //T responseString = objectMapper.readValue(response.body().bytes(), type);
        T responseString = objectMapper.readValue(response.body().bytes(), type);
        System.out.println("Response: "+ responseString);
        return responseString;
    }

    @SneakyThrows
    public TomograpiesResponse getTomographies(String token) {
        var request = new Request.Builder()
                .url(getUrl("tomographies"))
                .header("Authorization", "Bearer " + token)
                .get()
                .build();

        var response = this.okHttpClient.newCall(request).execute();
        return getData(response, TomograpiesResponse.class);

        //return tomograpiesResponse;
    }

    @SneakyThrows
    public Tomography getTomographyReport(String token, String codeReport) {
        var request = new Request.Builder()
                .url(getUrl("tomographies/report/"+codeReport))
                .header("Authorization", "Bearer " + token)
                .get()
                .build();

        var response = this.okHttpClient.newCall(request).execute();
        System.out.println("Busqueda de tomografria con codigo de reporte "+codeReport+": " + response);
        return getData(response, Tomography.class);

    }

    @SneakyThrows
    public String saveTomography(String token, String title, byte[] tac) {
        //var body = RequestBody.create(getBody(new Tomography(title, tac)), mediaType);
        // Crea un RequestBody para el archivo (tomografía)
        RequestBody fileBody = RequestBody.create(tac, MediaType.parse("multipart/form-data"));

        // Crea el cuerpo multipart donde se incluyen tanto el archivo como el título
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("tomography", "tomography.bin", fileBody)
                .build();

        HttpUrl url = HttpUrl.parse(getUrl("tomographies"))
                .newBuilder()
                .addQueryParameter("title", title)
                .addQueryParameter("lastImage", Boolean.TRUE.toString())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + token)
                .post(requestBody)
                .build();

        var response = this.okHttpClient.newCall(request).execute();
        return getData(response, JsonObject.class).getAsJsonObject("codeReport").getAsString();

    }
}
