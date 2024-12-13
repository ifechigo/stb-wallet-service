package com.suntrustbank.wallet.core.errorhandling;

public class TestData {

    public static final String error_duplicateReferenceResponse() {
        return """
                {
                    "status": "ERROR",
                    "errors": [
                        "DUPLICATE_REFERENCE"
                    ],
                    "messages": [
                        "reference already exist"
                    ]
                }
            """;
    }

    public static final String error_walletClosedResponse() {
        return """
                {
                    "status": "ERROR",
                    "errors": [
                        "ACCOUNT_NOT_ACTIVE"
                    ],
                    "messages": [
                        "Wallet is not active. Account may already be closed"
                    ]
                }
            """;
    }

    public static final String error_currencyMismatchResponse() {
        return """
                {
                    "status": "ERROR",
                    "errors": [
                        "CURRENCY_MISMATCH"
                    ],
                    "messages": [
                        "Wallet cannot receive this Currency. please confirm and try again"
                    ]
                }
            """;
    }

    public static final String error_insufficientBalanceResponse() {
        return """
                {
                    "status": "ERROR",
                    "errors": [
                        "INSUFFICIENT_BALANCE"
                    ],
                    "messages": [
                        "Insufficient Balance"
                    ]
                }
            """;
    }
}
