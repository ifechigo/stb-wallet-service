package com.suntrustbank.wallet.core.errorhandling;

import com.suntrustbank.wallet.core.dtos.BaseResponse;
import com.suntrustbank.wallet.core.enums.BaseResponseStatus;
import com.suntrustbank.wallet.providers.dtos.*;
import com.suntrustbank.wallet.providers.dtos.enums.Currency;
import com.suntrustbank.wallet.providers.dtos.enums.TransactionStatus;
import com.suntrustbank.wallet.providers.dtos.enums.WalletStatus;
import com.suntrustbank.wallet.providers.entrypoints.WalletController;
import com.suntrustbank.wallet.providers.repository.WalletRepository;
import com.suntrustbank.wallet.providers.repository.WalletTransactionRepository;
import com.suntrustbank.wallet.providers.repository.model.Wallet;
import com.suntrustbank.wallet.providers.repository.model.WalletTransactions;
import com.suntrustbank.wallet.providers.services.WalletService;
import com.suntrustbank.wallet.providers.services.impl.WalletServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class WalletTests {

    @Autowired
    private MockMvc mockMvc;

    private Wallet wallet1;
    private Wallet wallet2;
    private com.suntrustbank.wallet.providers.dtos.WalletCreationRequest creationRequest;
    private WalletTransactions walletTransactions;
    private WalletTransactionRequest transactionRequest;

    @BeforeEach
    public void setUp() {
        creationRequest = new WalletCreationRequest();
        creationRequest.setFirstName("John");
        creationRequest.setLastName("Doe");
        creationRequest.setMerchantId("123456789");

        transactionRequest = new WalletTransactionRequest();
        transactionRequest.setWalletId("0000000011");
        transactionRequest.setAmount(new BigDecimal("1000.00"));
        transactionRequest.setTransactionReference("transaction-ref-001");

        wallet1 = new Wallet();
        wallet1.setWalletId("0000000011");
        wallet1.setCurrency(Currency.NGN);
        wallet1.setStatus(WalletStatus.ACTIVE);
        wallet1.setBalance(BigDecimal.ZERO.setScale(2));


        wallet2 = new Wallet();
        wallet2.setWalletId("0000000011");
        wallet2.setCurrency(Currency.USD);
        wallet2.setStatus(WalletStatus.ACTIVE);
        wallet2.setBalance(BigDecimal.ZERO.setScale(2));


        walletTransactions = new WalletTransactions();
        walletTransactions.setWalletId("0000000011");
        walletTransactions.setTransactionReference("transaction-ref-001");
        walletTransactions.setReference("ref-001");
        walletTransactions.setCurrency(Currency.NGN);
        walletTransactions.setStatus(TransactionStatus.SUCCESSFUL);
        walletTransactions.setAmount(BigDecimal.valueOf(500));
    }

    @DisplayName("Create Wallet - SUCCESSFUL")
    @Test
    void successfullyCreateWallet() throws Exception {
        WalletRepository walletRepository = Mockito.mock(WalletRepository.class);
        WalletTransactionRepository walletTransactionRepository = Mockito.mock(WalletTransactionRepository.class);
        WalletService walletService = new WalletServiceImpl(walletRepository, walletTransactionRepository);
        WalletController walletController = new WalletController(walletService);

        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(creationRequest);

        MvcResult result = mockMvc.perform(post("/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        objectMapper.findAndRegisterModules();
        BaseResponse<WalletCreationResponse> baseResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<BaseResponse<WalletCreationResponse>>() {});
        WalletCreationResponse creationResponse = baseResponse.getData();

        assertThat(baseResponse).isNotNull();
        assertThat(baseResponse.getStatus()).isEqualTo(BaseResponseStatus.SUCCESS);
        assertThat(baseResponse.getMessage()).isEqualTo("success");
        assertThat(creationResponse.getMerchantId()).isEqualTo(creationRequest.getMerchantId());
        assertThat(creationResponse.getCurrency()).isEqualTo(creationRequest.getCurrency());
        assertThat(creationResponse.getWalletId()).isNotNull();
        assertThat(creationResponse.getWalletId()).hasSize(10).matches("\\d+");;
    }

    @DisplayName("Wallet Balance - SUCCESSFUL")
    @Test
    void testWalletBalance_success() throws Exception {
        WalletRepository walletRepository = Mockito.mock(WalletRepository.class);
        WalletTransactionRepository walletTransactionRepository = Mockito.mock(WalletTransactionRepository.class);
        WalletService walletService = new WalletServiceImpl(walletRepository, walletTransactionRepository);
        WalletController walletController = new WalletController(walletService);

        wallet2.setBalance(BigDecimal.valueOf(5));
        Mockito.when(walletRepository.findByWalletId(transactionRequest.getWalletId())).thenReturn(Optional.of(wallet2));
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();


        MvcResult result = mockMvc.perform(get("/wallet/balance")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("walletId", "0000000011"))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        BaseResponse<WalletBalanceResponse> baseResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<BaseResponse<WalletBalanceResponse>>() {});
        WalletBalanceResponse balanceResponse = baseResponse.getData();

        assertThat(baseResponse).isNotNull();
        assertThat(baseResponse.getStatus()).isEqualTo(BaseResponseStatus.SUCCESS);
        assertThat(baseResponse.getMessage()).isEqualTo("success");
        assertThat(balanceResponse.getWalletId()).isEqualTo(wallet2.getWalletId());
        assertThat(balanceResponse.getCurrency()).isEqualTo(wallet2.getCurrency().name());
        assertThat(balanceResponse.getBalance()).isEqualTo(wallet2.getBalance());
    }

    @DisplayName("Credit Wallet - SUCCESSFUL")
    @Test
    void testCreditWallet_success() throws Exception {
        WalletRepository walletRepository = Mockito.mock(WalletRepository.class);
        WalletTransactionRepository walletTransactionRepository = Mockito.mock(WalletTransactionRepository.class);
        WalletService walletService = new WalletServiceImpl(walletRepository, walletTransactionRepository);
        WalletController walletController = new WalletController(walletService);

        BigDecimal initialBalance = wallet1.getBalance();
        Mockito.when(walletRepository.findByWalletId(transactionRequest.getWalletId())).thenReturn(Optional.of(wallet1));
        Mockito.when(walletTransactionRepository.findByTransactionReference(transactionRequest.getTransactionReference())).thenReturn(Optional.empty());
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(transactionRequest);

        MvcResult result = mockMvc.perform(post("/wallet/credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        objectMapper.registerModule(new JavaTimeModule());
        BaseResponse<WalletTransactions> baseResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<BaseResponse<WalletTransactions>>() {
        });
        WalletTransactions transaction = baseResponse.getData();

        assertThat(baseResponse).isNotNull();
        assertThat(baseResponse.getStatus()).isEqualTo(BaseResponseStatus.SUCCESS);
        assertThat(baseResponse.getMessage()).isEqualTo("success");
        assertThat(transaction.getTransactionReference()).isEqualTo(transactionRequest.getTransactionReference());
        assertThat(transaction.getAmount()).isEqualTo(transactionRequest.getAmount());
        assertThat(wallet1.getBalance()).isEqualTo(initialBalance.add(transaction.getAmount()));
        assertThat(transaction.getReference()).isNotNull();
    }

    @DisplayName("Debit Wallet - SUCCESSFUL")
    @Test
    void testDebitWallet_success() throws Exception {
        WalletRepository walletRepository = Mockito.mock(WalletRepository.class);
        WalletTransactionRepository walletTransactionRepository = Mockito.mock(WalletTransactionRepository.class);
        WalletService walletService = new WalletServiceImpl(walletRepository, walletTransactionRepository);
        WalletController walletController = new WalletController(walletService);

        wallet1.setBalance(BigDecimal.valueOf(4000));
        BigDecimal initialBalance = wallet1.getBalance();
        Mockito.when(walletRepository.findByWalletId(transactionRequest.getWalletId())).thenReturn(Optional.of(wallet1));
        Mockito.when(walletTransactionRepository.findByTransactionReference(transactionRequest.getTransactionReference())).thenReturn(Optional.empty());
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(transactionRequest);

        MvcResult result = mockMvc.perform(post("/wallet/debit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        objectMapper.registerModule(new JavaTimeModule());
        BaseResponse<WalletTransactions> baseResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<BaseResponse<WalletTransactions>>() {});
        WalletTransactions transaction = baseResponse.getData();

        assertThat(baseResponse).isNotNull();
        assertThat(baseResponse.getStatus()).isEqualTo(BaseResponseStatus.SUCCESS);
        assertThat(baseResponse.getMessage()).isEqualTo("success");
        assertThat(transaction.getTransactionReference()).isEqualTo(transactionRequest.getTransactionReference());
        assertThat(transaction.getAmount()).isEqualTo(transactionRequest.getAmount());
        assertThat(wallet1.getBalance()).isEqualTo(initialBalance.subtract(transaction.getAmount()));
        assertThat(transaction.getReference()).isNotNull();
    }

    @DisplayName("Debit Wallet - INSUFFICIENT BALANCE")
    @Test
    void testDebitWallet_insufficient_balance() throws Exception {
        WalletRepository walletRepository = Mockito.mock(WalletRepository.class);
        WalletTransactionRepository walletTransactionRepository = Mockito.mock(WalletTransactionRepository.class);
        WalletService walletService = new WalletServiceImpl(walletRepository, walletTransactionRepository);
        WalletController walletController = new WalletController(walletService);

        Mockito.when(walletRepository.findByWalletId(transactionRequest.getWalletId())).thenReturn(Optional.of(wallet2));
        mockMvc = MockMvcBuilders.standaloneSetup(walletController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        transactionRequest.setCurrency(Currency.USD.name());
        transactionRequest.setAmount(BigDecimal.valueOf(50));
        String requestJson = objectMapper.writeValueAsString(transactionRequest);

        mockMvc.perform(post("/wallet/debit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(TestData.error_insufficientBalanceResponse()))
                .andReturn();
    }

    @DisplayName("Credit/Debit Wallet - CURRENCY MISMATCH")
    @Test
    void testWallet_currency_mismatch() throws Exception {
        WalletRepository walletRepository = Mockito.mock(WalletRepository.class);
        WalletTransactionRepository walletTransactionRepository = Mockito.mock(WalletTransactionRepository.class);
        WalletService walletService = new WalletServiceImpl(walletRepository, walletTransactionRepository);
        WalletController walletController = new WalletController(walletService);

        Mockito.when(walletRepository.findByWalletId(transactionRequest.getWalletId())).thenReturn(Optional.of(wallet2));
        mockMvc = MockMvcBuilders.standaloneSetup(walletController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(transactionRequest);

        mockMvc.perform(post("/wallet/credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(TestData.error_currencyMismatchResponse()))
                .andReturn();

        mockMvc.perform(post("/wallet/debit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(TestData.error_currencyMismatchResponse()))
                .andReturn();
    }

    @DisplayName("Credit/Debit Wallet - DUPLICATE REFERENCE")
    @Test
    void testWallet_duplicate_reference() throws Exception {
        WalletRepository walletRepository = Mockito.mock(WalletRepository.class);
        WalletTransactionRepository walletTransactionRepository = Mockito.mock(WalletTransactionRepository.class);
        WalletService walletService = new WalletServiceImpl(walletRepository, walletTransactionRepository);
        WalletController walletController = new WalletController(walletService);

        wallet1.setBalance(BigDecimal.valueOf(5000));
        Mockito.when(walletRepository.findByWalletId(transactionRequest.getWalletId())).thenReturn(Optional.of(wallet1));
        Mockito.when(walletTransactionRepository.findByTransactionReference(transactionRequest.getTransactionReference())).thenReturn(Optional.of(walletTransactions));
        mockMvc = MockMvcBuilders.standaloneSetup(walletController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();


        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(transactionRequest);

        mockMvc.perform(post("/wallet/credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(TestData.error_duplicateReferenceResponse()))
                .andReturn();

        mockMvc.perform(post("/wallet/debit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(TestData.error_duplicateReferenceResponse()))
                .andReturn();
    }

    @DisplayName("Credit/Debit Wallet - WALLET CLOSED")
    @Test
    void testWallet_wallet_closed() throws Exception {
        WalletRepository walletRepository = Mockito.mock(WalletRepository.class);
        WalletTransactionRepository walletTransactionRepository = Mockito.mock(WalletTransactionRepository.class);
        WalletService walletService = new WalletServiceImpl(walletRepository, walletTransactionRepository);
        WalletController walletController = new WalletController(walletService);

        wallet1.setStatus(WalletStatus.CLOSED);
        Mockito.when(walletRepository.findByWalletId(transactionRequest.getWalletId())).thenReturn(Optional.of(wallet1));
        mockMvc = MockMvcBuilders.standaloneSetup(walletController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(transactionRequest);

        mockMvc.perform(post("/wallet/credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(TestData.error_walletClosedResponse()))
                .andReturn();

        mockMvc.perform(post("/wallet/debit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(TestData.error_walletClosedResponse()))
                .andReturn();

        mockMvc.perform(get("/wallet/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("walletId", "0000000011"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(TestData.error_walletClosedResponse()))
                .andReturn();

    }
}
