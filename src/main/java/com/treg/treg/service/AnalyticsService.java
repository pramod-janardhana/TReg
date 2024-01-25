/**
 * service package is responsible for handling the business logic
 *
 * @author Pramod J
 */
package com.treg.treg.service;

import com.treg.treg.database.dao.Report;
import com.treg.treg.database.entity.Account;
import com.treg.treg.database.entity.Transaction;
import com.treg.treg.database.repository.AccountRepository;
import com.treg.treg.database.repository.TransactionRepository;
import com.treg.treg.dto.analytics.Response;
import com.treg.treg.exception.CustomException;
import com.treg.treg.utils.Error;
import com.treg.treg.utils.Pair;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Timer;

import static java.lang.Thread.sleep;

/**
 * AnalyticsService is responsible for handling the business logic related to Analytics
 */
@Service
public class AnalyticsService {
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);

    @Autowired
    public AnalyticsService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    // getStartTime return either start-time of the day or week or month
    // depending on the "of" argument
    private LocalDateTime getStartTime(LocalDateTime now, int of) {
        switch (of) {
            case 1:
                return LocalDateTime.of(now.toLocalDate(), LocalTime.MIN);
            case 2:
                LocalDate date = now.toLocalDate();
                LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
                return LocalDateTime.of(startOfWeek, LocalTime.MIN);
            case 3:
                return LocalDateTime.of(now.toLocalDate().withDayOfMonth(1), LocalTime.MIN);
        }

        return null;
    }

    /**
     * report method is responsible for providing transaction summary
     * @param of indicates timeframe, possible values of are {@code 1, 2, 3}
     * @param accountId is the id of the account
     * @return returns the transaction summary as report
     */
    @Cacheable(value = "report")
    public Pair<Response.ReportApi, Error> report(Long accountId, int of) {
        try {
            sleep(1000);
            Account account = null;
            Response.ReportApi.Report report = null;
            Response.ReportApi.Report.LineItem incomeLineItem = new Response.ReportApi.Report.LineItem();
            Response.ReportApi.Report.LineItem expenseLineItem = new Response.ReportApi.Report.LineItem();

            LocalDateTime end = LocalDateTime.now();
            LocalDateTime start = getStartTime(end, of);

            // request sanity
            {
                // start will be null only when invalid value is passed for "of" argument
                if (start == null) {
                    logger.error(String.format("Invalid value for of(%d)", of));
                    return new Pair<>(
                            null,
                            new Error("provide valid value for of", HttpServletResponse.SC_BAD_REQUEST)
                    );
                }

                // checking if the account with the given id is present
                account = accountRepository.findById(accountId).orElse(null);
                if (account == null) {
                    logger.error(String.format("Provided account-id %d for a transaction is not valid", accountId));
                    return new Pair<>(
                            null,
                            new Error(
                                    String.format("account with id %d is not present", accountId),
                                    HttpServletResponse.SC_BAD_REQUEST
                            ));
                }
            }

            // fetching transaction summary as report
            for (Report r : transactionRepository.getReportByType(
                    start,
                    end,
                    new Account(accountId))) {

                switch (r.getTnxType()) {
                    case DEBIT:
                        logger.debug("fetched report for debit transactions");
                        expenseLineItem = new Response.ReportApi.Report.LineItem(r.getTnxCount(), r.getTotalTnx());
                        break;
                    case CREDIT:
                        logger.debug("fetched report for credit transactions");
                        incomeLineItem = new Response.ReportApi.Report.LineItem(r.getTnxCount(), r.getTotalTnx());
                        break;
                }
            }

            logger.info("fetched report for of " + of);
            report = new Response.ReportApi.Report(
                    incomeLineItem,
                    expenseLineItem,
                    account.getBalance(),
                    account.getCurrency().toString()
            );

            return new Pair<>(new Response.ReportApi(report), null);

        } catch (Exception e) {
            logger.error(String.format("fetching of report failed with error: %s", e));
            return new Pair<>(
                    null,
                    new Error(
                            "failed to fetch report, try after sometime",
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                    ));
        }
    }
}
