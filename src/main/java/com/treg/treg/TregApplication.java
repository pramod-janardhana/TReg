package com.treg.treg;

import com.treg.treg.database.entity.Account;
import com.treg.treg.database.entity.Transaction;
import com.treg.treg.database.repository.AccountRepository;
import com.treg.treg.database.entity.Category;
import com.treg.treg.database.repository.CategoryRepository;
import com.treg.treg.database.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

@SpringBootApplication
@EnableCaching
public class TregApplication {

	public static void main(String[] args) {
		SpringApplication.run(TregApplication.class, args);
	}

}
