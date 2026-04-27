package com.transfer.api.integration;

import com.google.gson.Gson;
import com.transfer.api.controller.request.ContaRequestDto;
import com.transfer.api.controller.request.TransferRequestDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransferControllerTest {

    @LocalServerPort
    int port;

    private final Gson gson = new Gson();

    @BeforeAll
    static void setupBase() {
        baseURI = "http://localhost";
    }

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Should return 200 OK when transfer is successful")
    void shouldReturn200WhenTransferIsSuccessful() {
        String body = getValidTransferRequest();

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/transfer")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("idTransferencia", notNullValue())
                .body("msg", containsStringIgnoringCase("successfully"));
    }

    @Test
    @DisplayName("Should return 404 NOT FOUND when client or account does not exist")
    void shouldReturn404WhenResourceIsNotFound() {
        // Factory returns 404 if idTransferencia is null
        String body = getNonExistentClientRequest();

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/transfer")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("idTransferencia", nullValue());
    }

    @Test
    @DisplayName("Should return 422 UNPROCESSABLE ENTITY when daily limit is zero or exceeded")
    void shouldReturn422WhenDailyLimitIsInvalid() {
        // Factory returns 422 if limiteDiario <= 0
        String body = getExceededLimitRequest();

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/transfer")
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("limiteDiario", lessThanOrEqualTo(500.0f));
    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when body is null or malformed")
    void shouldReturn400WhenRequestIsInvalid() {
        given()
                .contentType(ContentType.JSON)
                .body("") // Empty body
                .when()
                .post("/transfer")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Should return 422 when transfer amount is zero")
    void shouldReturn422WhenAmountIsZero() {
        // 1. Crie o objeto Java primeiro
        String request = getExceededLimit0();

        given()
                .contentType(ContentType.JSON)
                // 2. O .body() deve receber o objeto (que o RestAssured converte)
                // ou o resultado do gson.toJson() diretamente.
                .body(request)
                .when()
                .post("/transfer")
                .then()
                .statusCode(422) // Agora ele deve chegar na sua Service
                .body("msg", is("Invalid transfer amount"));
    }

    // --- Helper Methods (Mock Data) ---

    private String getValidTransferRequest() {
        ContaRequestDto conta = ContaRequestDto.builder()
                .idOrigem("d0d32142-74b7-4aca-9c68-838aeacef96b")
                .idDestino("41313d7b-bd75-4c75-9dea-1f4be434007f")
                .build();

        return gson.toJson(TransferRequestDTO.builder()
                .idCliente("2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f")
                .valor(500.00)
                .conta(conta)
                .build());
    }

    private String getNonExistentClientRequest() {
        // Trigger logic that results in idTransferencia = null
        ContaRequestDto conta = ContaRequestDto.builder()
                .idOrigem("invalid-id")
                .idDestino("41313d7b-bd75-4c75-9dea-1f4be434007f")
                .build();

        return gson.toJson(TransferRequestDTO.builder()
                .idCliente("non-existent-uuid")
                .valor(500.00)
                .conta(conta)
                .build());
    }

    private String getExceededLimitRequest() {
        // Trigger logic that results in daily limit <= 0
        ContaRequestDto conta = ContaRequestDto.builder()
                .idOrigem("d0d32142-74b7-4aca-9c68-838aeacef96b")
                .idDestino("41313d7b-bd75-4c75-9dea-1f4be434007f")
                .build();

        return gson.toJson(TransferRequestDTO.builder()
                .idCliente("bcdd1048-a501-4608-bc82-66d7b4db3600")
                .valor(9999999.00) // Huge value to force business failure
                .conta(conta)
                .build());
    }

    private String getExceededLimit0() {
        // Trigger logic that results in daily limit <= 0
        ContaRequestDto conta = ContaRequestDto.builder()
                .idOrigem("d0d32142-74b7-4aca-9c68-838aeacef96b")
                .idDestino("41313d7b-bd75-4c75-9dea-1f4be434007f")
                .build();

        return gson.toJson(TransferRequestDTO.builder()
                .idCliente("bcdd1048-a501-4608-bc82-66d7b4db3600")
                .valor(0.00) // Huge value to force business failure
                .conta(conta)
                .build());
    }
}