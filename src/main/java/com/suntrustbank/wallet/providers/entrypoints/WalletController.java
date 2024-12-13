package com.suntrustbank.wallet.providers.entrypoints;

import com.suntrustbank.wallet.core.dtos.BaseResponse;
import com.suntrustbank.wallet.core.errorhandling.exceptions.GenericErrorCodeException;
import com.suntrustbank.wallet.providers.dtos.WalletCreationRequest;
import com.suntrustbank.wallet.providers.dtos.WalletTransactionRequest;
import com.suntrustbank.wallet.providers.services.WalletService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("wallet")
public class WalletController {
    private WalletService walletService;

    @PostMapping
    public BaseResponse createWallet(@RequestBody @Valid WalletCreationRequest request) {
        return walletService.create(request);
    }

    @PostMapping("/credit")
    public BaseResponse creditWallet(@RequestBody @Valid WalletTransactionRequest request) throws GenericErrorCodeException {
        return walletService.credit(request);
    }

    @PostMapping("/debit")
    public BaseResponse debitWallet(@RequestBody @Valid WalletTransactionRequest request) throws GenericErrorCodeException {
        return walletService.debit(request);
    }

    @GetMapping("/balance")
    public BaseResponse walletBalance(@RequestParam String walletId) throws GenericErrorCodeException {
        return walletService.balance(walletId);
    }

    @GetMapping("/balance/{merchantId}")
    public BaseResponse merchantWalletBalance(@PathVariable String merchantId, @RequestParam String walletId) throws GenericErrorCodeException {
        return walletService.balanceByMerchantId(merchantId, walletId);
    }
}
