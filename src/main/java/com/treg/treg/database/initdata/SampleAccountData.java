package com.treg.treg.database.initdata;

import com.treg.treg.database.entity.Account;
import com.treg.treg.database.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SampleAccountData implements CommandLineRunner {
    private AccountRepository accountRepository;

    private static final Logger logger = LoggerFactory.getLogger(SampleAccountData.class);

    public SampleAccountData(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.debug("started loading sample account data");

        accountRepository.save(new Account(
                "Savings",
                Account.Currency.INR,
                new BigDecimal(0)
        ));

        logger.debug("loaded sample account data");
    }
}
