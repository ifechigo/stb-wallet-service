package com.suntrustbank.wallet.providers.repository;

import com.suntrustbank.wallet.providers.repository.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByWalletId(String walletId);
    Optional<Wallet> findByMerchantIdAndWalletId(String merchantId, String walletId);
}
