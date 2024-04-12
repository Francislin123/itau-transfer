package com.transfer.api.transferapi.util;

import com.transfer.api.transferapi.controller.request.TransferRequestDTO;
import com.transfer.api.transferapi.integration.account.response.AccountOriginResponse;

public class CheckTransfer {
    public static boolean checkTransfer(double dailyCustomerLimit, double transferValue) {
        return !(dailyCustomerLimit <= 0) && !(dailyCustomerLimit < transferValue);
    }

    public static boolean isaBoolean(final TransferRequestDTO transferRequestDTO, final boolean checkTransfer,
                                      final AccountOriginResponse accountOriginResponse) {
        return checkTransfer
                && accountOriginResponse.isAtivo()
                && accountOriginResponse.getSaldo() > transferRequestDTO.getValor();
    }
}
