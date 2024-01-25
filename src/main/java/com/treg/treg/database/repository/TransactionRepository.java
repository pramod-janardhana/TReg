package com.treg.treg.database.repository;

import com.treg.treg.database.entity.Account;
import com.treg.treg.database.dao.Report;
import com.treg.treg.database.entity.Category;
import com.treg.treg.database.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT new com.treg.treg.database.dao.Report(COUNT(*), SUM(t.amount), t.type) " +
            "FROM Transaction t " +
            "WHERE t.accountId = ?3 AND t.time between ?1 and ?2 " +
            "GROUP BY t.type")
    List<Report> getReportByType(LocalDateTime start, LocalDateTime end, Account account);
}
