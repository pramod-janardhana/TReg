/**
 * service package is responsible for handling the business logic
 *
 * @author Pramod J
 */
package com.treg.treg.service;

import com.treg.treg.database.entity.Account;
import com.treg.treg.database.repository.AccountRepository;
import com.treg.treg.dto.account.Request;
import com.treg.treg.dto.account.Response;
import com.treg.treg.utils.Error;
import com.treg.treg.utils.Pair;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * AccountService is responsible for handling the business logic related to Accounts
 */
@Service
public class AccountService {
    private AccountRepository accountRepository;

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * list method is responsible for fetching the available accounts with pagination
     * @param page is the page number
     * @param size is max number of records fetched (value of size is in the range 0 to 10)
     * @return returns the available account from database
     */
    public Pair<List<Response.ListApi>, Error> list(int page, int size) {
        List<Response.ListApi> response = new LinkedList<>();

        try {
            if(size < 0 || size > 10) {
                logger.debug("setting size to 10");
                size = 10;
            }

            // fetching accounts from database
            PageRequest pr = PageRequest.of(page, size);
            for (Account e : accountRepository.findAll(pr).getContent()) {
                response.add(new Response.ListApi(
                        e.getId(),
                        e.getName(),
                        e.getCurrency(),
                        e.getBalance()
                ));
            }
        } catch (Exception e) {
            logger.error(String.format("fetching of accounts failed with err %s", e));
            return new Pair<>(
                    null,
                    new Error(
                            "failed to fetch accounts, try again later",
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                    ));
        }

        return new Pair<>(response, null);
    }

    /**
     * get method is responsible for fetching the account for the give id
     * @param id is the id of the account
     * @return if present then returns the account details for the given id
     */
    public Pair<Response.GetApi, Error> get(Long id) {
        try {
            // fetching account by id
            Account account = accountRepository.findById(id).orElse(null);
            if (account != null) {
                return new Pair<>(
                        new Response.GetApi(
                                account.getId(),
                                account.getName(),
                                account.getCurrency(),
                                account.getBalance()
                        ),
                        null
                );
            }
        } catch (Exception e) {
            logger.error(String.format("fetching of account details failed with err %s", e));
            return new Pair<>(
                    null,
                    new Error(
                            "failed to fetch account details, try again later",
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                    ));
        }

        logger.debug(String.format("No account with id %d", id));
        return new Pair<>(
                null,
                new Error(
                        String.format("No account with id %d", id),
                        HttpServletResponse.SC_BAD_REQUEST
                )
        );
    }

    /**
     * add method is responsible for adding the new account
     * @param request contains the details of the account
     * @return returns true if account was added successfully
     */
    public Pair<Response.AddApi, Error> add(Request.AddApi request) {
        logger.info("Creating new account");
        logger.debug(String.format("Account details %s", request));

        try {
            // checking if account name is empty
            if (request.getName().isEmpty()) {
                logger.error("account name is empty");
                return new Pair<>(
                        null,
                        new Error(
                                "account name can not be empty",
                                HttpServletResponse.SC_BAD_REQUEST
                        )
                );
            }

            // checking if initial balance of the account is valid
            if(request.getBalance() == null || request.getBalance().doubleValue() < 0.0) {
                logger.error("account initial balance is negative");
                return new Pair<>(
                        null,
                        new Error(
                                "account balance can not be negative",
                                HttpServletResponse.SC_BAD_REQUEST
                        )
                );
            }

            if(!request.getCurrency().isValid()) {
                logger.error("provided currency is not supported: "+ request.getCurrency());
                return new Pair<>(
                        null,
                        new Error(
                                "provided currency is not supported",
                                HttpServletResponse.SC_BAD_REQUEST
                        )
                );
            }

            // adding account
            Account account = accountRepository.save(new Account(
                    request.getName(),
                    request.getCurrency(),
                    request.getBalance()
            ));

            logger.info("added new account with id " + account.getId());
        }catch (DataIntegrityViolationException e) {
            logger.error(String.format("account of category failed with err %s", e));
            return new Pair<>(
                    null,
                    new Error(
                            "failed to create account, try with different name",
                            HttpServletResponse.SC_BAD_REQUEST
                    ));
        } catch (Exception e) {
            logger.error(String.format("creation of account failed with err %s", e));
            return new Pair<>(
                    null,
                    new Error(
                            "failed to create account, try again later",
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                    ));
        }

        return new Pair<>(
                new Response.AddApi(true),
                null
        );
    }
}
