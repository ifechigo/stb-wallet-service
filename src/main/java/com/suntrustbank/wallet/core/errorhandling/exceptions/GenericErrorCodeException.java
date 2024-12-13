package com.suntrustbank.wallet.core.errorhandling.exceptions;

import com.suntrustbank.wallet.core.enums.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@RequiredArgsConstructor
public class GenericErrorCodeException extends RuntimeException {

    private final String message;
    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;

    public GenericErrorCodeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getDescription();
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public static GenericErrorCodeException accountNotFound() {
        return new GenericErrorCodeException(ErrorCode.ACCOUNT_NOT_FOUND);
    }

    public static GenericErrorCodeException serverError() {
        return new GenericErrorCodeException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public static GenericErrorCodeException duplicateReference() {
        return new GenericErrorCodeException(ErrorCode.DUPLICATE_REFERENCE);
    }

    public static GenericErrorCodeException amountBeyondTransactionLimit() {
        return new GenericErrorCodeException(ErrorCode.LIMIT_BREACHED);
    }

    public static GenericErrorCodeException insufficientBalance() {
        return new GenericErrorCodeException(ErrorCode.INSUFFICIENT_BALANCE);
    }

    public static GenericErrorCodeException pnd() {
        return new GenericErrorCodeException(ErrorCode.DEBIT_BLOCKED);
    }

    public static GenericErrorCodeException accountNotFrozen() {
        return new GenericErrorCodeException(ErrorCode.ACCOUNT_NOT_FROZEN);
    }

    public static GenericErrorCodeException accountFrozen() {
        return new GenericErrorCodeException(ErrorCode.ACCOUNT_FROZEN);
    }

    public static GenericErrorCodeException debitNotBlocked() {
        return new GenericErrorCodeException(ErrorCode.DEBIT_NOT_BLOCKED);
    }

    public static GenericErrorCodeException lienRemoved() {
        return new GenericErrorCodeException(ErrorCode.LIEN_ALREADY_REMOVED);
    }

    public static GenericErrorCodeException invalidLien() {
        return new GenericErrorCodeException(ErrorCode.LIEN_ID_INVALID);
    }

    public static GenericErrorCodeException balanceNotEmpty() {
        return new GenericErrorCodeException(ErrorCode.BALANCE_NOT_EMPTY);
    }

    public static GenericErrorCodeException accountNotActive() {
        return new GenericErrorCodeException(ErrorCode.ACCOUNT_NOT_ACTIVE);
    }

    public static GenericErrorCodeException walletAccountNotFound() {
        return new GenericErrorCodeException(ErrorCode.WALLET_ACCOUNT_NOT_FOUND);
    }

    public static GenericErrorCodeException accountDepositLimitExceeded() {
        return new GenericErrorCodeException(ErrorCode.ACCOUNT_DEPOSIT_LIMIT_EXCEEDED);
    }

    public static GenericErrorCodeException transactionNotFound() {
        return new GenericErrorCodeException(ErrorCode.TRANSACTION_NOT_FOUND);
    }

    public static GenericErrorCodeException closureIdNotFound() {
        return new GenericErrorCodeException(ErrorCode.CLOSURE_REASON_NOT_FOUND);
    }

    public static GenericErrorCodeException currencyMismatch() {
        return new GenericErrorCodeException(ErrorCode.CURRENCY_MISMATCH);
    }
}
