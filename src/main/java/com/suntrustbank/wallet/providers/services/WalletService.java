package com.suntrustbank.wallet.providers.services;

import com.suntrustbank.wallet.core.dtos.BaseResponse;
import com.suntrustbank.wallet.core.errorhandling.exceptions.GenericErrorCodeException;
import com.suntrustbank.wallet.providers.dtos.WalletCreationRequest;
import com.suntrustbank.wallet.providers.dtos.WalletTransactionRequest;

public interface WalletService {
    BaseResponse create(WalletCreationRequest request);
    BaseResponse credit(WalletTransactionRequest request) throws GenericErrorCodeException;
    BaseResponse debit(WalletTransactionRequest request) throws GenericErrorCodeException;
    BaseResponse balance(String walletId) throws GenericErrorCodeException;
    BaseResponse balanceByMerchantId(String merchantId, String walletId) throws GenericErrorCodeException;
}
