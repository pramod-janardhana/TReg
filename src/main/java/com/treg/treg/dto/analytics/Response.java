package com.treg.treg.dto.analytics;


import com.treg.treg.database.entity.Account;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class Response {

    @Getter
    @Setter
    public static class ReportApi {

        private Report report;

        public ReportApi() {
            this.report = null;
        }

        public ReportApi(Report report) {
            this.report = report;
        }

        public Report getReport() {
            return report;
        }

        @Getter
        @Setter
        public static class Report {

            @Getter
            @Setter
            public static class LineItem {
                Long count;
                BigDecimal total;

                public LineItem() {
                    this.count = 0L;
                    this.total = new BigDecimal(0);
                }

                public LineItem(Long count, BigDecimal total) {
                    this.count = count;
                    this.total = total;
                }
            }

            private LineItem income;
            private LineItem expenses;
            private BigDecimal cashFlow;
            private BigDecimal balance;
            private String currency;

            public Report() {
            }

            public Report(LineItem income, LineItem expenses, BigDecimal balance, String currency) {
                this.income = income;
                this.expenses = expenses;
                this.cashFlow = income.total.subtract(expenses.total);
                this.balance = balance;
                this.currency = currency.toString();
            }

            public LineItem NewLineItem(Long count, BigDecimal total) {
                return new LineItem(count, total);
            }

            public LineItem getIncome() {
                return income;
            }

            public LineItem getExpenses() {
                return expenses;
            }

            public BigDecimal getCashFlow() {
                return cashFlow;
            }

            public BigDecimal getBalance() {
                return balance;
            }

            public String getCurrency() {
                return currency;
            }
        }
    }
}