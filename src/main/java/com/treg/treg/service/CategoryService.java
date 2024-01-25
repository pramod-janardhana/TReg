/**
 * service package is responsible for handling the business logic
 *
 * @author Pramod J
 */
package com.treg.treg.service;

import com.treg.treg.database.entity.Category;
import com.treg.treg.database.repository.CategoryRepository;
import com.treg.treg.dto.category.Request;
import com.treg.treg.dto.category.Response;
import com.treg.treg.utils.Error;
import com.treg.treg.utils.Pair;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * CategoryService is responsible for handling the business logic related to Category
 */
@Service
public class CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * list method is responsible for fetching the available categories with pagination
     * @param page is the page number
     * @param size is max number of records fetched (value of size is in the range 0 to 10)
     * @return returns the available categories from database
     */
    public Pair<List<Response.ListApi>, Error> list(int page, int size) {
        List<Response.ListApi> response = new LinkedList<>();
        try {
            if (size < 0 || size > 10) {
                logger.debug("setting size to 10");
                size = 10;
            }

            // fetching categories from database
            PageRequest pr = PageRequest.of(page, size);

            for (Category e : categoryRepository.findAll(pr).getContent()) {
                response.add(new Response.ListApi(
                        e.getId(),
                        e.getName()
                ));
            }
        } catch (Exception e) {
            logger.error(String.format("fetching of categories failed with err %s", e));
            return new Pair<>(
                    null,
                    new Error(
                            "failed to fetch categories, try again later",
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                    ));
        }

        return new Pair<>(response, null);
    }

    /**
     * get method is responsible for fetching the category for the give id
     *
     * @param id is the id of the category
     * @return if present then returns the category details for the given id
     */
    public Pair<Response.GetApi, Error> get(Long id) {
        try {
            // fetching category by id
            Category category = categoryRepository.findById(id).orElse(null);
            if (category != null) {
                return new Pair<>(
                        new Response.GetApi(
                                category.getId(),
                                category.getName()
                        ),
                        null
                );
            }
        } catch (Exception e) {
            logger.error(String.format("fetching of category details failed with err %s", e));
            return new Pair<>(
                    null,
                    new Error(
                            "failed to fetch category details, try again later",
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                    ));
        }

        logger.debug(String.format("No category with id %d", id));
        return new Pair<>(
                null,
                new Error(String.format("No category with id %d", id), HttpServletResponse.SC_BAD_REQUEST)
        );
    }

    /**
     * add method is responsible for adding the new category
     *
     * @param request contains the details of the category
     * @return returns true if category was added successfully
     */
    public Pair<Response.AddApi, Error> add(Request.AddApi request) {
        logger.info("Creating new category");
        logger.debug(String.format("Category details %s", request));

        try {
            // checking if category name is empty
            if (request.getName().isEmpty()) {
                return new Pair<>(
                        null,
                        new Error("category name can not be empty", HttpServletResponse.SC_BAD_REQUEST)
                );
            }

            // adding category
            Category category = categoryRepository.save(new Category(request.getName()));

            logger.info("added new category with id " + category.getId());
        } catch (DataIntegrityViolationException e) {
            logger.error(String.format("creation of category failed with err %s", e));
            return new Pair<>(
                    null,
                    new Error(
                            "failed to create category, try with different name",
                            HttpServletResponse.SC_BAD_REQUEST
                    ));
        } catch (Exception e) {
            logger.error(String.format("creation of category failed with err %s", e));
            return new Pair<>(
                    null,
                    new Error(
                            "failed to create category, try again later",
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                    ));
        }

        return new Pair<>(
                new Response.AddApi(true),
                null
        );
    }
}
