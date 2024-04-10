package com.transfer.api.transferapi.integration.transfer;

import com.google.gson.Gson;
import com.transfer.api.transferapi.controller.response.TransferResponseDTO;
import com.transfer.api.transferapi.controller.request.TransferRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
public class TransferClientImpl implements TransferClient {

    @Override
    public String transferClientSend(TransferRequestDTO transferRequestDTO) {

        Gson gson = new Gson();

        final String bodyResultFinal;

        try {

            String json = gson.toJson(transferRequestDTO);

            HttpClient client = HttpClient.newHttpClient();

            String uriTransfer = "http://localhost:8080/transferencia";

            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .uri(URI.create(uriTransfer))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // print status code
            final int statusCode = response.statusCode();

            if (statusCode == 404) {
                log.info("Transfer not carried out");
            }

            // print response body
            bodyResultFinal = response.body();

        } catch (IOException | InterruptedException e) {
            log.trace("Error with integration", e);
            throw new RuntimeException(e);
        }

        TransferResponseDTO transferResponseDTO = gson.fromJson(bodyResultFinal, TransferResponseDTO.class);
        log.info("Transfer completed successfully ID: : " + transferResponseDTO.getIdTransferencia());

        return transferResponseDTO.getIdTransferencia();
    }
}
