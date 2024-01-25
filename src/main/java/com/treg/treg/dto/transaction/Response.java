package com.treg.treg.dto.transaction;


import com.treg.treg.database.entity.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Response {

    @Getter
    @Setter
    public static class RecordApi {
        private boolean ok;

        public RecordApi() {
        }

        public RecordApi(boolean ok) {
            this.ok = ok;
        }
    }

    @Getter
    @Setter
    public static class ListApi {
        private Long id;
        private Transaction.Type type;
        private BigDecimal amount;
        private LocalDateTime timestamp;
        private String label;

        public ListApi() {
        }

        public ListApi(Long id, Transaction.Type type,
                       BigDecimal amount,
                       LocalDateTime timestamp, String label) {
            this.id = id;
            this.type = type;
            this.amount = amount;
            this.timestamp = timestamp;
            this.label = label;
        }
    }

    @Getter
    @Setter
    public static class GetApi {
        private Long id;
        private Transaction.Type type;
        private BigDecimal amount;
        private LocalDateTime timestamp;
        private String label;

        public GetApi() {
        }

        public GetApi(Long id, Transaction.Type type,
                       BigDecimal amount,
                       LocalDateTime timestamp, String label) {
            this.id = id;
            this.type = type;
            this.amount = amount;
            this.timestamp = timestamp;
            this.label = label;
        }
    }
}
