package com.treg.treg.database.entity;

import com.treg.treg.database.entity.Account;
import com.treg.treg.database.entity.Category;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.uuid.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Transaction")
@Table(name = "transaction")
public class Transaction {
    public enum Type {
        CREDIT,
        DEBIT,
    }

    @Id
    @GeneratedValue(generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_seq", allocationSize = 1)
    private Long id;

    @Column(name = "type", nullable = false, updatable = false)
    private Type type;

    @Column(name = "amount", nullable = false, updatable = false)
    private BigDecimal amount;

    @Column(name = "date", nullable = false, updatable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false, updatable = false)
    private LocalDateTime time;

    @Column(name = "label", nullable = false)
    private String label;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.REMOVE}
    )
    @JoinColumn(
            name = "account_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "account_id_fk")
    )
    private Account accountId;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.REMOVE}
    )
    @JoinColumn(
            name = "category_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "category_id_fk")
    )
    private Category categoryId;

    public Transaction() {
    }

    public Transaction(Type type, BigDecimal amount, LocalDate date,
                       LocalDateTime time, String label, Account accountId, Category categoryId) {
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.label = label;
        this.accountId = accountId;
        this.categoryId = categoryId;
    }

    public Transaction(Long id, Type type, BigDecimal amount,
                       LocalDate date, LocalDateTime time, String label,
                       Account accountId, Category categoryId) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.label = label;
        this.accountId = accountId;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Account getAccountId() {
        return accountId;
    }

    public void setAccountId(Account accountId) {
        this.accountId = accountId;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", date=" + date +
                ", time=" + time +
                ", label='" + label + '\'' +
                ", accountId=" + accountId +
                ", categoryId=" + categoryId +
                '}';
    }
}
