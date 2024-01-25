/**
 * service package is responsible for handling the business logic
 *
 * @author Pramod J
 */
package com.treg.treg.service;

import com.treg.treg.database.entity.Account;
import com.treg.treg.database.entity.Category;
import com.treg.treg.database.entity.Transaction;
import com.treg.treg.database.repository.AccountRepository;
import com.treg.treg.database.repository.CategoryRepository;
import com.treg.treg.database.repository.TransactionRepository;
import com.treg.treg.dto.transaction.Request;
import com.treg.treg.dto.transaction.Response;
import com.treg.treg.exception.CustomException;
import com.treg.treg.utils.Error;
import com.treg.treg.utils.Pair;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TransactionService is responsible for handling the business logic related to Transactions
 */
@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public TransactionService(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
    }

    public Pair<Response.GetApi, Error> get(Long id) {
        try {
            logger.debug(String.format("Fetching transaction with id %d", id));

            // fetching transaction by id
            Transaction transaction = transactionRepository.findById(id).orElse(null);
            if (transaction != null) {
                return new Pair<>(
                        new Response.GetApi(
                                transaction.getId(),
                                transaction.getType(),
                                transaction.getAmount(),
                                transaction.getTime(),
                                transaction.getLabel()
                        ),
                        null
                );
            }
        } catch (Exception e) {
            logger.error(String.format("fetching of transaction details failed with err %s", e));
            return new Pair<>(
                    null,
                    new Error(
                            "failed to fetch transaction details, try again later",
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                    ));
        }

        logger.debug(String.format("No transaction with id %d", id));
        return new Pair<>(
                null,
                new Error(String.format("No transaction with id %d", id), HttpServletResponse.SC_BAD_REQUEST)
        );
    }

    @Transactional
    private void credit(Transaction t) throws Exception {
        Account account = accountRepository.findById(t.getAccountId().getId()).orElse(null);
        if (account == null) {
            logger.error(String.format("Provided account-id %d for a transaction is not valid", t.getAccountId().getId()));
            throw new CustomException.InvalidAccount(
                    String.format("account with id %d is not present", t.getAccountId().getId()));
        }

        Category category = categoryRepository.findById(t.getCategoryId().getId()).orElse(null);
        if (category == null) {
            logger.error(String.format("Provided category-id %d for a transaction is not valid", t.getCategoryId().getId()));
            throw new CustomException.InvalidCategory(
                    String.format("category with id %d is not present", t.getCategoryId().getId()));
        }

        // updating the balance of the account
        account.setBalance(account.getBalance().add(t.getAmount()));

        // save the transaction
        transactionRepository.save(t);
    }

    @Transactional
    private void debit(Transaction t) throws Exception {
        Account account = accountRepository.findById(t.getAccountId().getId()).orElse(null);
        if (account == null) {
            logger.error(String.format("Provided account-id %d for a transaction is not valid", t.getAccountId().getId()));
            throw new CustomException.InvalidAccount(
                    String.format("account with id %d is not present", t.getAccountId().getId()));
        }

        Category category = categoryRepository.findById(t.getCategoryId().getId()).orElse(null);
        if (category == null) {
            logger.error(String.format("Provided category-id %d for a transaction is not valid", t.getCategoryId().getId()));
            throw new CustomException.InvalidCategory(
                    String.format("category with id %d is not present", t.getCategoryId().getId()));
        }

        // checking if account has sufficient balance to perform debit operation
        if (account.getBalance().subtract(t.getAmount()).doubleValue() < 0.0) {
            logger.info(String.format("Insufficient balance in the account %d", t.getAccountId().getId()));
            throw new CustomException.InsufficientBalance("insufficient balance");
        }

        // updating the balance of the account
        account.setBalance(account.getBalance().subtract(t.getAmount()));

        // save the transaction
        transactionRepository.save(t);
    }

    /**
     * record method is responsible for adding new transaction
     *
     * @param request contains the details of the account
     * @return returns true if transaction was added successfully
     */
    @CacheEvict(value = "report", allEntries = true)
    public Pair<Response.RecordApi, Error> record(Request.RecordApi request) {
        logger.info("Creating new transaction");
        logger.debug(String.format("Transaction details %s", request));

        // request sanity
        {
            if (request.getAmount() == null){
                request.setAmount(new BigDecimal(0));
            }

            if(request.getAmount().compareTo(new BigDecimal(0)) != 1) {
                logger.error(String.format("Provided amount %f for a transaction is not valid", request.getAmount().doubleValue()));
                return new Pair<>(
                        null,
                        new Error("provide a valid amount", HttpServletResponse.SC_BAD_REQUEST)
                );
            }
        }

        Transaction t = new Transaction(
                request.getType(),
                request.getAmount(),
                LocalDate.now(),
                LocalDateTime.now(),
                request.getLabel(),
                new Account(request.getAccountId()),
                new Category(request.getCategoryId())
        );

        try {
            // checking if transaction a debit or credit operation
            switch (request.getType()) {
                case CREDIT:
                    credit(t);
                    break;
                case DEBIT:
                    debit(t);
                    break;
                default:
                    logger.error(String.format("Provided transaction type %s is not valid", request.getType().toString()));
                    return new Pair<>(
                            null,
                            new Error("provide a valid transaction type", HttpServletResponse.SC_BAD_REQUEST)
                    );
            }
        } catch (CustomException.InvalidAccount e) {
            logger.debug(String.format("Transaction failed with error %s", e));
            return new Pair<>(
                    null,
                    new Error("transaction failed because of invalid account details", HttpServletResponse.SC_BAD_REQUEST)
            );
        } catch (CustomException.InvalidCategory e) {
            logger.debug(String.format("Transaction failed with error %s", e));
            return new Pair<>(
                    null,
                    new Error("transaction failed because of invalid category details", HttpServletResponse.SC_BAD_REQUEST)
            );
        } catch (CustomException.InsufficientBalance e) {
            logger.debug(String.format("Transaction failed with error %s", e));
            return new Pair<>(
                    null,
                    new Error("transaction failed because of insufficient balance", HttpServletResponse.SC_BAD_REQUEST)
            );
        } catch (Exception e) {
            logger.debug(String.format("Transaction failed with error %s", e));
            return new Pair<>(
                    null,
                    new Error("transaction failed, try after sometime", HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
            );
        }

        logger.info("Transaction successful");
        return new Pair<>(
                new Response.RecordApi(true),
                null
        );

    }
}
