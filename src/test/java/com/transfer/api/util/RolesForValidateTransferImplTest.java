package com.transfer.api.util;

import com.transfer.api.controller.request.ContaRequestDto;
import com.transfer.api.controller.request.TransferRequestDTO;
import com.transfer.api.facade.RolesForValidateTransfer;
import com.transfer.api.facade.RolesForValidateTransferImpl;
import com.transfer.api.integration.account.Account;
import com.transfer.api.integration.account.response.AccountOriginResponse;
import com.transfer.api.integration.balance.NotificationBacen;
import com.transfer.api.integration.client.Client;
import com.transfer.api.integration.client.response.ClientResponse;
import com.transfer.api.integration.transfer.TransferClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static com.transfer.api.util.template.Templates.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class RolesForValidateTransferImplTest {

    @Mock
    private Client client;

    @Mock
    private Account account;

    @Mock
    private TransferClient transferClient;

    @Mock
    private NotificationBacen notificationBacen;

    @InjectMocks
    private RolesForValidateTransfer rolesForValidateTransfer = new RolesForValidateTransferImpl();

    @Test
    public void makeTransferCompletedTest() {

        ContaRequestDto contaRequestDto = getContaRequestDto();

        TransferRequestDTO transferRequestDTO = getTransferRequestDTO(contaRequestDto);

        ClientResponse clientResponse = getClientResponse();

        AccountOriginResponse accountOriginResponse = getAccountOriginResponse();

        when(client.validateIfTheClientExists(transferRequestDTO.getIdCliente()))
                .thenReturn(clientResponse);

        when(account.searchSourceAccountData(transferRequestDTO.getConta().getIdOrigem()))
                .thenReturn(accountOriginResponse);

        when(transferClient.transferSend(transferRequestDTO)).thenReturn("e36ddaa0-df91-40d6-abc0-b8bb24732629");

        this.rolesForValidateTransfer.rolesForValidateTransfer(transferRequestDTO);

        verify(client, times(1)).validateIfTheClientExists(anyString());
        verify(account, times(1)).searchSourceAccountData(anyString());
        verify(transferClient, times(1)).transferSend(transferRequestDTO);
    }

    @Test
    public void validateIfTheClientNotExistsTest() {

        ContaRequestDto contaRequestDto = getContaRequestDto();

        TransferRequestDTO transferRequestDTO = getTransferRequestDTO(contaRequestDto);

        ClientResponse clientResponse = getClientResponse();
        clientResponse.setId(null);

        when(client.validateIfTheClientExists(transferRequestDTO.getIdCliente()))
                .thenReturn(clientResponse);

        this.rolesForValidateTransfer.rolesForValidateTransfer(transferRequestDTO);

        verify(client, times(1)).validateIfTheClientExists(anyString());
        verify(account, times(0)).searchSourceAccountData(anyString());
        verify(transferClient, times(0)).transferSend(transferRequestDTO);
    }

    @Test
    public void dailyLimitTestExceededTest() {

        ContaRequestDto contaRequestDto = getContaRequestDto();

        TransferRequestDTO transferRequestDTO = getTransferRequestDTO(contaRequestDto);
        transferRequestDTO.setValor(10000.0);

        AccountOriginResponse accountOriginResponse = getAccountOriginResponse();

        ClientResponse clientResponse = getClientResponse();

        when(client.validateIfTheClientExists(transferRequestDTO.getIdCliente()))
                .thenReturn(clientResponse);

        when(account.searchSourceAccountData(transferRequestDTO.getConta().getIdOrigem()))
                .thenReturn(accountOriginResponse);

        this.rolesForValidateTransfer.rolesForValidateTransfer(transferRequestDTO);

        verify(client, times(1)).validateIfTheClientExists(anyString());
        verify(account, times(1)).searchSourceAccountData(anyString());
        verify(transferClient, times(0)).transferSend(transferRequestDTO);
    }
}