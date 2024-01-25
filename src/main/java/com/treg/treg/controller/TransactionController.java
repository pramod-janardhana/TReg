/**
 * Controller package is responsible for registering all http/s routes
 *
 * @author Pramod J
 */
package com.treg.treg.controller;

import com.treg.treg.database.entity.Transaction;
import com.treg.treg.dto.transaction.Request;
import com.treg.treg.dto.transaction.Response;
import com.treg.treg.interceptor.ServerResponse;
import com.treg.treg.service.TransactionService;
import com.treg.treg.utils.Error;
import com.treg.treg.utils.Pair;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

/**
 * TransactionController is responsible for registering all transaction related route
 *
 * {@code base url: /api/v1/transaction}
 */
@RestController
@RequestMapping("/api/v1/transaction")
@EnableTransactionManagement
public class TransactionController {
    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * get method is responsible for accepting the request to fetch the transaction for the give id
     * @param id is the id of the transaction
     * @param response is http response class
     * @return if present then returns the transaction details for the given id
     */
    @GetMapping("/{id}")
    public ServerResponse<Response.GetApi> get(@PathVariable Long id, HttpServletResponse response) {
        Pair<Response.GetApi, Error> res = transactionService.get(id);
        if(res.getSecond() != null) {
            response.setStatus(res.getSecond().getStatusCode());
            return new ServerResponse<>(false, null, res.getSecond().getErrMsg());
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return new ServerResponse<>(true, res.getFirst(), "");
    }

    /**
     * record method is responsible for accepting the request to add new transaction
     * @param request contains the details of the transaction
     * @param response is http response class
     * @return returns true if transaction was added successfully
     */
    @PostMapping
    public ServerResponse<Response.RecordApi> record(@RequestBody Request.RecordApi request, HttpServletResponse response) {
        Pair<Response.RecordApi, Error> res = transactionService.record(request);

        if(res.getSecond() != null) {
            response.setStatus(res.getSecond().getStatusCode());
            return new ServerResponse<>(false, null, res.getSecond().getErrMsg());
        }

        response.setStatus(HttpServletResponse.SC_CREATED);
        return new ServerResponse<>(true, res.getFirst(), "");
    }
}
