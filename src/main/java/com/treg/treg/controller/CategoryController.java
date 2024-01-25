/**
 * Controller package is responsible for registering all http/s routes
 *
 * @author Pramod J
 */
package com.treg.treg.controller;

import com.treg.treg.service.CategoryService;
import com.treg.treg.dto.category.Request;
import com.treg.treg.dto.category.Response;
import com.treg.treg.interceptor.ServerResponse;
import com.treg.treg.utils.Error;
import com.treg.treg.utils.Pair;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CategoryController is responsible for registering all category related route
 * {@code base url: /api/v1/category}
 */
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * list method is responsible for accepting the request to fetch all available categories
     * @param page is the page number
     * @param size is max number of records fetched (value of size is in the range 0 to 10)
     * @param response is http response class
     * @return returns all the available categories from database
     */
    @GetMapping
    public ServerResponse<List<Response.ListApi>> list(@RequestParam int page, @RequestParam int size, HttpServletResponse response) {
        Pair<List<Response.ListApi>, Error> res = categoryService.list(page, size);
        if(res.getSecond() != null) {
            response.setStatus(res.getSecond().getStatusCode());
            return new ServerResponse<>(false, null, res.getSecond().getErrMsg());
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return new ServerResponse<>(true, res.getFirst(), "");
    }

    /**
     * get method is responsible for accepting the request to fetch the category for the give id
     * @param id is the id of the category
     * @param response is http response class
     * @return if present then returns the category details for the given id
     */
    @GetMapping("/{id}")
    public ServerResponse<Response.GetApi> get(@PathVariable Long id, HttpServletResponse response) {
        Pair<Response.GetApi, Error> res = categoryService.get(id);
        if(res.getSecond() != null) {
            response.setStatus(res.getSecond().getStatusCode());
            return new ServerResponse<>(false, null, res.getSecond().getErrMsg());
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return new ServerResponse<>(true, res.getFirst(), "");
    }

    /**
     * add method is responsible for accepting the request to add new category
     * @param request contains the details of the category
     * @param response is http response class
     * @return returns true if category was added successfully
     */
    @PostMapping
    public ServerResponse<Response.AddApi> add(@RequestBody Request.AddApi request, HttpServletResponse response) {
        Pair<Response.AddApi, Error> res = categoryService.add(request);
        if(res.getSecond() != null) {
            response.setStatus(res.getSecond().getStatusCode());
            return new ServerResponse<>(false, null, res.getSecond().getErrMsg());
        }

        response.setStatus(HttpServletResponse.SC_CREATED);
        return new ServerResponse<>(true, res.getFirst(), "");
    }
}
