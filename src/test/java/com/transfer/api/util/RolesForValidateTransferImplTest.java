package com.transfer.api.util;

import com.transfer.api.controller.request.ContaRequestDto;
import com.transfer.api.controller.request.TransferRequestDTO;
import com.transfer.api.service.RolesForValidateTransfer;
import com.transfer.api.service.RolesForValidateTransferImpl;
import com.transfer.api.service.integration.account.Account;
import com.transfer.api.service.integration.account.response.AccountOriginResponse;
import com.transfer.api.service.integration.balance.NotificationBacen;
import com.transfer.api.service.integration.client.Client;
import com.transfer.api.service.integration.client.response.ClientResponse;
import com.transfer.api.service.integration.transfer.TransferClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.transfer.api.util.template.Templates.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolesForValidateTransferImplTest {

    @Mock
    private Client client;

    @Mock
    private Account account;

    @Mock
    private TransferClient transferClient;

    @Mock
    private NotificationBacen notificationBacen;

    @InjectMocks
    private RolesForValidateTransferImpl rolesForValidateTransfer;

    @Test
    @DisplayName("Should complete transfer successfully when all validations pass")
    void makeTransferCompletedTest() {
        // Arrange
        ContaRequestDto contaRequestDto = getContaRequestDto();
        TransferRequestDTO transferRequestDTO = getTransferRequestDTO(contaRequestDto);

        ClientResponse clientResponse = getClientResponse();
        AccountOriginResponse accountOriginResponse = getAccountOriginResponse();

        // Note: use .originId() (the new record accessor)
        when(client.validateIfTheClientExists(transferRequestDTO.getIdCliente()))
                .thenReturn(clientResponse);

        when(account.searchSourceAccountData(transferRequestDTO.getConta().originId()))
                .thenReturn(accountOriginResponse);

        when(transferClient.transferSend(transferRequestDTO))
                .thenReturn("e36ddaa0-df91-40d6-abc0-b8bb24732629");

        // Act
        rolesForValidateTransfer.rolesForValidateTransfer(transferRequestDTO);

        // Assert
        verify(client, times(1)).validateIfTheClientExists(anyString());
        verify(account, times(1)).searchSourceAccountData(anyString());
        verify(transferClient, times(1)).transferSend(transferRequestDTO);
    }

    @Test
    @DisplayName("Should abort transfer when client does not exist")
    void validateIfTheClientNotExistsTest() {
        // Arrange
        ContaRequestDto contaRequestDto = getContaRequestDto();
        TransferRequestDTO transferRequestDTO = getTransferRequestDTO(contaRequestDto);

        // Record is immutable: use the builder to create a version with null ID
        ClientResponse clientNotFound = ClientResponse.builder()
                .id(null)
                .build();

        when(client.validateIfTheClientExists(transferRequestDTO.getIdCliente()))
                .thenReturn(clientNotFound);

        // Act
        rolesForValidateTransfer.rolesForValidateTransfer(transferRequestDTO);

        // Assert
        verify(client, times(1)).validateIfTheClientExists(anyString());
        verify(account, never()).searchSourceAccountData(anyString());
        verify(transferClient, never()).transferSend(any());
    }

    @Test
    @DisplayName("Should abort transfer when daily limit is exceeded")
    void dailyLimitTestExceededTest() {
        // Arrange
        ContaRequestDto contaRequestDto = getContaRequestDto();
        TransferRequestDTO transferRequestDTO = getTransferRequestDTO(contaRequestDto);
        transferRequestDTO.setValor(10000.0); // Assuming limit is lower than this

        AccountOriginResponse accountOriginResponse = getAccountOriginResponse();
        ClientResponse clientResponse = getClientResponse();

        when(client.validateIfTheClientExists(transferRequestDTO.getIdCliente()))
                .thenReturn(clientResponse);

        when(account.searchSourceAccountData(transferRequestDTO.getConta().originId()))
                .thenReturn(accountOriginResponse);

        // Act
        rolesForValidateTransfer.rolesForValidateTransfer(transferRequestDTO);

        // Assert
        verify(client, times(1)).validateIfTheClientExists(anyString());
        verify(account, times(1)).searchSourceAccountData(anyString());
        verify(transferClient, never()).transferSend(any());
    }
}