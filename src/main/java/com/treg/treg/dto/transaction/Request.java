package com.treg.treg.dto.transaction;

import com.treg.treg.database.entity.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class Request {
    public static class RecordApi {
        private Transaction.Type type;
        private String label;
        private BigDecimal amount;

        private Long AccountId;
        private Long CategoryId;

        public RecordApi() {
        }

        public RecordApi(Transaction.Type type, String label,
                         BigDecimal amount, Long accountId, Long categoryId) {
            this.type = type;
            this.label = label;
            this.amount = amount;
            AccountId = accountId;
            CategoryId = categoryId;
        }

        public Transaction.Type getType() {
            return type;
        }

        public void setType(Transaction.Type type) {
            this.type = type;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public Long getAccountId() {
            return AccountId;
        }

        public void setAccountId(Long accountId) {
            AccountId = accountId;
        }

        public Long getCategoryId() {
            return CategoryId;
        }

        public void setCategoryId(Long categoryId) {
            CategoryId = categoryId;
        }
    }
}
