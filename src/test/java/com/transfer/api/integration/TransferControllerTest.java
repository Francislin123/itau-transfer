package com.transfer.api.integration;

import com.google.gson.Gson;
import com.transfer.api.controller.request.ContaRequestDto;
import com.transfer.api.controller.request.TransferRequestDTO;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.*;
@SpringBootTest
public class TransferControllerTest {

    @BeforeClass
    public static void setUp() {
        baseURI = "http://localhost";
        port = 8077;
        basePath = "/api/v1";
    }

    @Test
    public void makeTransferOk200Test() {

        String client = getClientOK();

        given().
                contentType(ContentType.JSON).
                body(client).
                when().
                post("/transfer").
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void makeTransferOk200ReturnCustomerDailyLimitTest() {

        String client = makeTransferOk200ReturnCustomerDailyLimit();

        given().
                contentType(ContentType.JSON).
                body(client).
                when().
                post("/transfer").
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void makeTransferTestNotFoundClient() {

        String client = getClientNotFoundClient();

        given().
                contentType(ContentType.JSON).
                body(client).
                when().
                post("/transfer").
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);

    }
    private static String getClientOK() {

        ContaRequestDto contaRequestDto = ContaRequestDto.builder()
                .idOrigem("d0d32142-74b7-4aca-9c68-838aeacef96b")
                .idDestino("41313d7b-bd75-4c75-9dea-1f4be434007f")
                .build();

        TransferRequestDTO transferRequestDTO = TransferRequestDTO.builder()
                .idCliente("bcdd1048-a501-4608-bc82-66d7b4db3600")
                .valor(500.00)
                .conta(contaRequestDto)
                .build();

        return getString(transferRequestDTO);
    }

    private static String makeTransferOk200ReturnCustomerDailyLimit() {

        ContaRequestDto contaRequestDto = ContaRequestDto.builder()
                .idOrigem("d0d32142-74b7-4aca-9c68-838aeacef96b")
                .idDestino("41313d7b-bd75-4c75-9dea-1f4be434007f")
                .build();

        TransferRequestDTO transferRequestDTO = TransferRequestDTO.builder()
                .idCliente("bcdd1048-a501-4608-bc82-66d7b4db3600")
                .valor(5900.00)
                .conta(contaRequestDto)
                .build();

        return getString(transferRequestDTO);
    }

    private static String getClientNotFoundClient() {
        ContaRequestDto contaRequestDto = ContaRequestDto.builder()
                .idOrigem("d0d32142-74b7-4aca-9c68-838aeacef96")
                .idDestino("41313d7b-bd75-4c75-9dea-1f4be434007f")
                .build();

        TransferRequestDTO transferRequestDTO = TransferRequestDTO.builder()
                .idCliente("bcdd1048-a501-4608-bc82-66d7b4db3600")
                .valor(500.00)
                .conta(contaRequestDto)
                .build();

        return getString(transferRequestDTO);
    }

    private static String getString(TransferRequestDTO transferRequestDTO) {
        Gson gson = new Gson();

        return gson.toJson(transferRequestDTO);
    }
}
