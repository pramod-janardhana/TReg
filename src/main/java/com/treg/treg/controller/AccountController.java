/**
 * Controller package is responsible for registering all http/s routes
 *
 * @author Pramod J
 */
package com.treg.treg.controller;

import com.treg.treg.dto.account.Request;
import com.treg.treg.dto.account.Response;
import com.treg.treg.interceptor.ServerResponse;
import com.treg.treg.service.AccountService;
import com.treg.treg.utils.Error;
import com.treg.treg.utils.Pair;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AccountController is responsible for registering all account related route
 * {@code base url: /api/v1/account}
 */
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * list method is responsible for accepting the request to fetch all available account
     * @param page is the page number
     * @param size is max number of records fetched (value of size is in the range 0 to 10)
     * @param response is http response class
     * @return returns all the available account from database
     */
    @GetMapping
    public ServerResponse<List<Response.ListApi>> list(@RequestParam int page, @RequestParam int size,HttpServletResponse response) {
        Pair<List<Response.ListApi>, Error> res = accountService.list(page, size);
        if (res.getSecond() != null) {
            response.setStatus(res.getSecond().getStatusCode());
            return new ServerResponse<>(false, null, res.getSecond().getErrMsg());
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return new ServerResponse<>(true, res.getFirst(), "");
    }

    /**
     * get method is responsible for accepting the request to fetch the account for the give id
     * @param id is the id of the account
     * @param response is http response class
     * @return if present then returns the account details for the given id
     */
    @GetMapping("/{id}")
    public ServerResponse<Response.GetApi> get(@PathVariable Long id, HttpServletResponse response) {
        Pair<Response.GetApi, Error> res = accountService.get(id);
        if (res.getSecond() != null) {
            response.setStatus(res.getSecond().getStatusCode());
            return new ServerResponse<>(false, null, res.getSecond().getErrMsg());
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return new ServerResponse<>(true, res.getFirst(), "");
    }

    /**
     * add method is responsible for accepting the request to add new account
     * @param request contains the details of the account
     * @param response is http response class
     * @return returns true if account was added successfully
     */
    @PostMapping
    public ServerResponse<Response.AddApi> add(@RequestBody Request.AddApi request, HttpServletResponse response) {
        Pair<Response.AddApi, Error> res = accountService.add(request);
        if (res.getSecond() != null) {
            response.setStatus(res.getSecond().getStatusCode());
            return new ServerResponse<>(false, null, res.getSecond().getErrMsg());
        }

        response.setStatus(HttpServletResponse.SC_CREATED);
        return new ServerResponse<>(true, res.getFirst(), "");
    }
}
