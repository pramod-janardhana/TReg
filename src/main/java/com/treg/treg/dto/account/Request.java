package com.treg.treg.dto.account;

import com.treg.treg.database.entity.Account;

import java.math.BigDecimal;

public class Request {
    public static class AddApi {
        private String name;
        private Account.Currency currency;
        private BigDecimal balance;

        public AddApi() {
        }

        public AddApi(String name, Account.Currency currency, BigDecimal balance) {
            this.name = name;
            this.currency = currency;
            this.balance = balance;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public Account.Currency getCurrency() {
            return currency;
        }

        public void setCurrency(Account.Currency currency) {
            this.currency = currency;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }
    }
}
