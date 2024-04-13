package com.transfer.api.util;

import com.transfer.api.integration.account.response.AccountOriginResponse;
import com.transfer.api.controller.request.TransferRequestDTO;

public class CheckTransfer {
    public static boolean checkTransfer(double dailyCustomerLimit, double transferValue) {
        return !(dailyCustomerLimit <= 0) && !(dailyCustomerLimit < transferValue);
    }

    public static boolean isaBoolean(final TransferRequestDTO transferRequestDTO, final boolean checkTransfer,
                                      final AccountOriginResponse accountOriginResponse) {
        return checkTransfer
                && accountOriginResponse.isAtivo()
                && accountOriginResponse.getSaldo() >= transferRequestDTO.getValor();
    }
}
