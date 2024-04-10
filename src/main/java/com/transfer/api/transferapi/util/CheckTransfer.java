package com.transfer.api.transferapi.util;

public class CheckTransfer {
    public static boolean checkTransfer(double dailyCustomerLimit, double transferValue) {
        return !(dailyCustomerLimit <= 0) && !(dailyCustomerLimit < transferValue);
    }
}
