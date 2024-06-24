package org.example.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.example.client.requests.CreateOrderRequest;
import org.example.client.requests.LoginRequest;
import org.example.client.requests.RegisterRequest;
import org.example.client.requests.RegisterResponse;
import org.example.client.responses.CreateOrderResponse;
import org.example.client.responses.ErrorResponse;
import org.example.client.responses.LoginResponse;
import org.example.client.responses.OrdersResponse;
import org.example.exceptions.AuthException;
import org.example.exceptions.RestException;

public class HttpClient {
    private OkHttpClient okHttpClient;
    private String baseUrl= System.getenv("API_URL") + "/api"; //"http://localhost:8080/api";
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
    public CreateOrderResponse shareOrder(String orderId, String token) {
        var url = String.format("orders/%s/users", orderId);

        var request = new Request.Builder()
                .url(getUrl(url))
                .header("Authorization", "Bearer " + token)
                .patch(RequestBody.create("", mediaType))
                .build();

        var response = this.okHttpClient.newCall(request).execute();
        return getData(response, CreateOrderResponse.class);
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
        return objectMapper.readValue(response.body().bytes(), type);
    }
}
