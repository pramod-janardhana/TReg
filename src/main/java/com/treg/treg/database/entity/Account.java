package com.treg.treg.database.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity(name = "Account")
@Table(
        name = "account",
        uniqueConstraints = {
                @UniqueConstraint(name = "account_name_uk", columnNames = "name")
        },
        indexes = {
                @Index(name = "account_name_idx", columnList = "name", unique = true)
        }
)
public class Account {
    public enum Currency {
        INR, USD;

        public boolean isValid() {
            for(Currency c: Currency.values()) {
                if(c == this) {
                    return true;
                }
            }

            return false;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(64)", updatable = false)
    private String name;

    @Column(name = "currency", nullable = false, updatable = false)
    private Currency currency;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "accountId", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Transaction> transactions;

    public Account() {
    }

    public Account(Long id) {
        this.id = id;
    }

    public Account(String name, Currency currency, BigDecimal balance) {
        this.name = name;
        this.currency = currency;
        this.balance = balance;
    }

    public Account(Long id, String name, Currency currency, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
