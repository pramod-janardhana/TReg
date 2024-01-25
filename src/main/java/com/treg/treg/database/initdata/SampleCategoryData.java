package com.treg.treg.database.initdata;

import com.treg.treg.database.entity.Account;
import com.treg.treg.database.entity.Category;
import com.treg.treg.database.repository.AccountRepository;
import com.treg.treg.database.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SampleCategoryData implements CommandLineRunner {
    private CategoryRepository categoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(SampleCategoryData.class);

    public SampleCategoryData(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.debug("started loading sample category data");

        categoryRepository.save(new Category("Income"));
        categoryRepository.save(new Category("Salary"));
        categoryRepository.save(new Category("Inventory"));

        logger.debug("loaded sample category data");
    }
}
