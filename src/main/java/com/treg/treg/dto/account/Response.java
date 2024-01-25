package com.treg.treg.dto.account;


import com.treg.treg.database.entity.Account;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class Response {

    @Getter
    @Setter
    public static class AddApi {
        private boolean ok;

        public AddApi() {
        }

        public AddApi(boolean ok) {
            this.ok = ok;
        }
    }

    @Getter
    @Setter
    public static class ListApi {
        private Long id;
        private String name;
        private Account.Currency currency;
        private BigDecimal balance;

        public ListApi() {
        }

        public ListApi(Long id, String name,
                       Account.Currency currency, BigDecimal balance) {
            this.id = id;
            this.name = name;
            this.currency = currency;
            this.balance = balance;
        }
    }

    @Getter
    @Setter
    public static class GetApi {
        private Long id;
        private String name;
        private Account.Currency currency;
        private BigDecimal balance;

        public GetApi() {
        }

        public GetApi(Long id, String name,
                      Account.Currency currency, BigDecimal balance) {
            this.id = id;
            this.name = name;
            this.currency = currency;
            this.balance = balance;
        }
    }
}
