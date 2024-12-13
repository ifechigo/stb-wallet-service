package com.suntrustbank.wallet.providers.repository;

import com.suntrustbank.wallet.providers.repository.model.WalletTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletTransactionRepository extends JpaRepository<WalletTransactions, Long>  {
    Optional<WalletTransactions> findByTransactionReference(String transactionReference);
}
