package com.treg.treg.database.dao;

import com.treg.treg.database.entity.Transaction;

import java.math.BigDecimal;

public class Report {
    private Long tnxCount;
    private BigDecimal totalTnx;
    private Transaction.Type tnxType;

    public Report(Long tnxCount, BigDecimal totalTnx, Transaction.Type tnxType) {
        this.tnxCount = tnxCount;
        this.totalTnx = totalTnx;
        this.tnxType = tnxType;
    }

    public Long getTnxCount() {
        return tnxCount;
    }

    public BigDecimal getTotalTnx() {
        return totalTnx;
    }

    public Transaction.Type getTnxType() {
        return tnxType;
    }
}
