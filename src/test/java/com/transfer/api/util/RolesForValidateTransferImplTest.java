package com.transfer.api.util;

import com.transfer.api.controller.request.ContaRequestDto;
import com.transfer.api.controller.request.TransferRequestDTO;
import com.transfer.api.controller.response.TransferResponseDTO;
import com.transfer.api.service.RolesForValidateTransferImpl;
import com.transfer.api.service.integration.account.Account;
import com.transfer.api.service.integration.account.response.AccountOriginResponse;
import com.transfer.api.service.integration.balance.NotificationBacen;
import com.transfer.api.service.integration.client.Client;
import com.transfer.api.service.integration.client.response.ClientResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.transfer.api.util.template.Templates.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolesForValidateTransferImplTest {

    @Mock
    private Client client;

    @Mock
    private Account account;

    @Mock
    private NotificationBacen notificationBacen;

    @InjectMocks
    private RolesForValidateTransferImpl rolesForValidateTransfer;

    @Test
    @DisplayName("Should complete transfer successfully when all validations pass")
    void shouldCompleteTransferSuccessfullyTest() {
        // Arrange
        ContaRequestDto contaRequestDto = getContaRequestDto();
        TransferRequestDTO transferRequestDTO = getTransferRequestDTO(contaRequestDto);

        ClientResponse clientResponse = getClientResponse();

        // Creating responses for both accounts (Origin and Destination)
        AccountOriginResponse sourceResponse = getAccountOriginResponseBuilder();
        AccountOriginResponse destinationResponse = getAccountOriginResponseBuilder();

        when(client.validateIfTheClientExists(anyString())).thenReturn(clientResponse);

        // Mocking the two calls that searchAllAccountsData performs
        when(account.searchSourceAccountData(transferRequestDTO.conta().idOrigem())).thenReturn(sourceResponse);
        when(account.searchSourceAccountData(transferRequestDTO.conta().idDestino())).thenReturn(destinationResponse);

        // Act
        rolesForValidateTransfer.rolesForValidateTransfer(transferRequestDTO);

        // Assert
        verify(client, times(1)).validateIfTheClientExists(anyString());
        verify(account, times(2)).searchSourceAccountData(anyString()); // Verifies the 2 parallel calls
    }

    @Test
    @DisplayName("Should abort transfer when client does not exist even though parallel lookup is performed")
    void shouldAbortTransferWhenClientDoesNotExistTest() {
        // Arrange
        ContaRequestDto contaRequestDto = getContaRequestDto();
        TransferRequestDTO request = getTransferRequestDTO(contaRequestDto);
        ClientResponse clientNotFound = ClientResponse.builder().id(null).build();

        // For performance reasons, the service calls accounts in parallel before validating the client
        when(client.validateIfTheClientExists(anyString())).thenReturn(clientNotFound);

        // Mocking the account response since they WILL be triggered in the async flow
        when(account.searchSourceAccountData(anyString()))
                .thenReturn(AccountOriginResponse.builder().build());

        // Act
        TransferResponseDTO response = rolesForValidateTransfer.rolesForValidateTransfer(request);

        // Assert
        assertEquals("Client not found", response.msg());

        // Verify that it was called 2 times (origin and destination) due to parallelism
        verify(account, times(2)).searchSourceAccountData(anyString());

        // Verify that the final step (Bacen Notification) NEVER occurred
        verify(notificationBacen, never()).notificationBacen(any());
    }

    @Test
    @DisplayName("Should abort transfer when daily limit is exceeded")
    void shouldAbortTransferWhenDailyLimitIsExceededTest() {
        // Arrange
        ContaRequestDto contaRequestDto = getContaRequestDto();

        // Creating a DTO with a high value to exceed the limit
        TransferRequestDTO transferRequestDTO = TransferRequestDTO.builder()
                .idCliente("client-123")
                .valor(10000.0)
                .conta(contaRequestDto)
                .build();

        ClientResponse clientResponse = getClientResponse();

        AccountOriginResponse sourceWithLowLimit = getAccountOriginResponseBuilder();
        AccountOriginResponse destinationResponse = getAccountOriginResponseBuilder();

        when(client.validateIfTheClientExists(anyString())).thenReturn(clientResponse);
        when(account.searchSourceAccountData(transferRequestDTO.conta().idOrigem())).thenReturn(sourceWithLowLimit);
        when(account.searchSourceAccountData(transferRequestDTO.conta().idDestino())).thenReturn(destinationResponse);

        // Act
        rolesForValidateTransfer.rolesForValidateTransfer(transferRequestDTO);

        // Assert
        verify(client, times(1)).validateIfTheClientExists(anyString());
        verify(notificationBacen, never()).notificationBacen(any());
    }
}