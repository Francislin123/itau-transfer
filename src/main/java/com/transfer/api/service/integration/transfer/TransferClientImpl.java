package com.transfer.api.service.integration.transfer;

import com.google.gson.Gson;
import com.transfer.api.controller.response.TransferResponseDTO;
import com.transfer.api.controller.request.TransferRequestDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
@Slf4j
public class TransferClientImpl implements TransferClient {

    private final Gson gson = new Gson();
    private static final String TRANSFER_SEND_SERVICE = "transferSendService";

    // Reusing HttpClient instance for connection pooling efficiency
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    @Override
    @CircuitBreaker(name = TRANSFER_SEND_SERVICE, fallbackMethod = "fallbackTransferSend")
    public String transferSend(TransferRequestDTO transferRequestDTO) {
        log.info("Initiating transfer request to external provider.");

        String json = gson.toJson(transferRequestDTO);
        String uriTransfer = "http://localhost:8080/transferencia";

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create(uriTransfer))
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();

            if (statusCode >= 400) {
                log.error("Transfer service failed with status: {}", statusCode);
                throw new RuntimeException("Transfer service error: " + statusCode);
            }

            TransferResponseDTO transferResponseDTO = gson.fromJson(response.body(), TransferResponseDTO.class);
            log.info("Transfer completed successfully. ID: {}", transferResponseDTO.getIdTransferencia());

            return transferResponseDTO.getIdTransferencia();

        } catch (IOException | InterruptedException e) {
            log.error("Communication failure during transfer execution: {}", e.getMessage());
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            throw new RuntimeException("Transfer integration I/O failure", e);
        }
    }

    /**
     * Fallback for Transfer Send
     * Returns a null or specific value to indicate the transfer was not processed
     */
    public String fallbackTransferSend(TransferRequestDTO request, Throwable e) {
        log.error("Circuit Breaker OPEN for transferSend. Reason: {}", e.getMessage());
        // Returning null allows the Orchestrator to handle the failure response
        return null;
    }
}