package com.transfer.api.util;

import com.transfer.api.controller.request.TransferRequestDTO;
import com.transfer.api.service.integration.account.response.AccountOriginResponse;

public class CheckTransfer {

    /**
     * Checks if the transfer amount exceeds the available daily limit.
     * * @param dailyLimit the maximum allowed for the day
     * @param transferValue the current transfer amount
     * @return true if the value is greater than the limit, false otherwise
     */
    public static boolean checkTransfer(double dailyLimit, double transferValue) {
        return transferValue > dailyLimit;
    }

    /**
     * Logic to decide if the Central Bank (BACEN) should be notified.
     * (e.g., transfers above a specific threshold).
     * * @param request the transfer data
     * @param account the account details
     * @return true if notification is required
     */
    public static boolean isNotificationRequired(TransferRequestDTO request, AccountOriginResponse account) {
        // Example: Notify if the transfer is greater than 1000.0
        return request.valor() > 1000.0;
    }
}