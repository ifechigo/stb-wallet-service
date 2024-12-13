package com.suntrustbank.wallet.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR("An error occurred, try again!"),
    SERVICE_UNAVAILABLE("Service unavailable"),
    BAD_REQUEST("Bad request"),
    ACCOUNT_NOT_FOUND("Wallet does not exist"),
    DUPLICATE_REFERENCE("reference already exist"),
    LIMIT_BREACHED("Amount specified will breach your limit"),
    INSUFFICIENT_BALANCE("Insufficient Balance"),
    DEBIT_BLOCKED("Debits blocked on integration"),
    ACCOUNT_NOT_FROZEN("Wallet is not frozen. Cannot unfreeze a integration that's not frozen"),
    ACCOUNT_FROZEN("Operation not allowed on blocked integration"),
    DEBIT_NOT_BLOCKED("Debits to Wallet is not blocked. Cannot unblock what's not blocked"),
    LIEN_ALREADY_REMOVED("Lien already removed"),
    LIEN_ID_INVALID("LienId is invalid or does not exist"),
    BALANCE_NOT_EMPTY("Wallet balance is not zero. Cannot close a integration that has funds in it. Please remove all funds first."),
    ACCOUNT_NOT_ACTIVE("Wallet is not active. Account may already be closed"),
    CLOSURE_REASON_NOT_FOUND("Closure Reason id not found"),
    TRANSACTION_NOT_FOUND("Transaction does not exist"),
    WALLET_ACCOUNT_NOT_FOUND("Wallet account does not exist"),
    ACCOUNT_DEPOSIT_LIMIT_EXCEEDED("Deposit Limit for this account has been exceeded"),
    CURRENCY_MISMATCH("Wallet cannot receive this Currency. please confirm and try again");

    private final String description;
}
