package com.suntrustbank.wallet.providers.services.impl;

import com.suntrustbank.wallet.core.dtos.BaseResponse;
import com.suntrustbank.wallet.core.enums.BaseResponseMessage;
import com.suntrustbank.wallet.core.errorhandling.exceptions.GenericErrorCodeException;
import com.suntrustbank.wallet.providers.dtos.enums.WalletStatus;
import com.suntrustbank.wallet.providers.dtos.enums.WalletAction;
import com.suntrustbank.wallet.providers.dtos.enums.Currency;
import com.suntrustbank.wallet.providers.dtos.enums.TransactionStatus;
import com.suntrustbank.wallet.providers.dtos.WalletBalanceResponse;
import com.suntrustbank.wallet.providers.dtos.WalletCreationRequest;
import com.suntrustbank.wallet.providers.dtos.WalletTransactionRequest;
import com.suntrustbank.wallet.providers.dtos.WalletCreationResponse;
import com.suntrustbank.wallet.providers.repository.WalletRepository;
import com.suntrustbank.wallet.providers.repository.WalletTransactionRepository;
import com.suntrustbank.wallet.providers.repository.model.Wallet;
import com.suntrustbank.wallet.providers.repository.model.WalletTransactions;
import com.suntrustbank.wallet.providers.services.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    @Override
    public BaseResponse create(WalletCreationRequest request) {

        UUID uuid = UUID.randomUUID();
        long number = Math.abs(uuid.getLeastSignificantBits()) % 10000000000L;

        Wallet wallet = new Wallet();
        wallet.setMerchantId(request.getMerchantId());
        wallet.setFirstName(request.getFirstName());
        wallet.setLastName(request.getLastName());
        wallet.setWalletId(String.format("%010d", number));
        wallet.setCurrency(Currency.valueOf(request.getCurrency()));
        wallet.setBalance(BigDecimal.ZERO.setScale(2));
        wallet.setStatus(WalletStatus.ACTIVE);
        walletRepository.save(wallet);

        return BaseResponse.success(WalletCreationResponse.builder()
            .firstName(wallet.getFirstName()).lastName(wallet.getLastName()).merchantId(wallet.getMerchantId())
            .currency(wallet.getCurrency().name()).walletId(wallet.getWalletId()).createdAt(wallet.getCreatedAt())
            .build(), BaseResponseMessage.SUCCESSFUL);
    }

    @Override
    public BaseResponse credit(WalletTransactionRequest request) throws GenericErrorCodeException {
        Wallet wallet = walletRepository.findByWalletId(request.getWalletId())
            .orElseThrow(GenericErrorCodeException::accountNotFound);

        if (WalletStatus.CLOSED.equals(wallet.getStatus())) {
            throw GenericErrorCodeException.accountNotActive();
        }

        if (!wallet.getCurrency().equals(Currency.valueOf(request.getCurrency()))) {
            throw GenericErrorCodeException.currencyMismatch();
        }

        walletTransactionRepository.findByTransactionReference(request.getTransactionReference())
            .ifPresent(transaction -> {throw GenericErrorCodeException.duplicateReference();});

        WalletTransactions walletTransactions = new WalletTransactions();
        walletTransactions.setAmount(request.getAmount());
        walletTransactions.setWalletId(request.getWalletId());
        walletTransactions.setStatus(TransactionStatus.PENDING);
        walletTransactions.setWalletAction(WalletAction.CREDIT);
        walletTransactions.setCurrency(Currency.valueOf(request.getCurrency()));
        walletTransactions.setTransactionReference(request.getTransactionReference());
        walletTransactions.setReference(UUID.randomUUID().toString().replace("-", ""));
        walletTransactionRepository.save(walletTransactions);

        wallet.setBalance(wallet.getBalance().add(request.getAmount()).setScale(2));
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        walletTransactions.setStatus(TransactionStatus.SUCCESSFUL);
        walletTransactions.setUpdatedAt(LocalDateTime.now());
        walletTransactionRepository.save(walletTransactions);

        return BaseResponse.success(walletTransactions, BaseResponseMessage.SUCCESSFUL);
    }

    @Override
    public BaseResponse debit(WalletTransactionRequest request) throws GenericErrorCodeException {
        Wallet wallet = walletRepository.findByWalletId(request.getWalletId())
            .orElseThrow(GenericErrorCodeException::accountNotFound);

        if (WalletStatus.CLOSED.equals(wallet.getStatus())) {
            throw GenericErrorCodeException.accountNotActive();
        }

        if (!wallet.getCurrency().equals(Currency.valueOf(request.getCurrency()))) {
            throw GenericErrorCodeException.currencyMismatch();
        }

        walletTransactionRepository.findByTransactionReference(request.getTransactionReference())
            .ifPresent(transaction -> {throw GenericErrorCodeException.duplicateReference();});

        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw GenericErrorCodeException.insufficientBalance();
        }

        WalletTransactions walletTransactions = new WalletTransactions();
        walletTransactions.setAmount(request.getAmount());
        walletTransactions.setWalletId(request.getWalletId());
        walletTransactions.setStatus(TransactionStatus.PENDING);
        walletTransactions.setWalletAction(WalletAction.DEBIT);
        walletTransactions.setCurrency(Currency.valueOf(request.getCurrency()));
        walletTransactions.setTransactionReference(request.getTransactionReference());
        walletTransactions.setReference(UUID.randomUUID().toString().replace("-", ""));
        walletTransactionRepository.save(walletTransactions);

        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()).setScale(2));
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        walletTransactions.setStatus(TransactionStatus.SUCCESSFUL);
        walletTransactions.setUpdatedAt(LocalDateTime.now());
        walletTransactionRepository.save(walletTransactions);

        return BaseResponse.success(walletTransactions, BaseResponseMessage.SUCCESSFUL);
    }

    @Override
    public BaseResponse balance(String walletId) throws GenericErrorCodeException {
        Wallet wallet = walletRepository.findByWalletId(walletId)
            .orElseThrow(GenericErrorCodeException::accountNotFound);

        if (WalletStatus.CLOSED.equals(wallet.getStatus())) {
            throw GenericErrorCodeException.accountNotActive();
        }

        return BaseResponse.success(WalletBalanceResponse.builder()
            .walletId(wallet.getWalletId()).currency(wallet.getCurrency().name()).balance(wallet.getBalance())
            .build(), BaseResponseMessage.SUCCESSFUL);
    }

    @Override
    public BaseResponse balanceByMerchantId(String merchantId, String walletId) throws GenericErrorCodeException {
        Wallet wallet = walletRepository.findByMerchantIdAndWalletId(merchantId, walletId)
                .orElseThrow(GenericErrorCodeException::accountNotFound);

        if (WalletStatus.CLOSED.equals(wallet.getStatus())) {
            throw GenericErrorCodeException.accountNotActive();
        }

        return BaseResponse.success(WalletBalanceResponse.builder()
                .walletId(wallet.getWalletId()).currency(wallet.getCurrency().name()).balance(wallet.getBalance())
                .build(), BaseResponseMessage.SUCCESSFUL);
    }
}
