/**
 * Controller package is responsible for registering all http/s routes
 *
 * @author Pramod J
 */
package com.treg.treg.controller;

import com.treg.treg.dto.analytics.Response;
import com.treg.treg.interceptor.ServerResponse;
import com.treg.treg.service.AnalyticsService;
import com.treg.treg.utils.Error;
import com.treg.treg.utils.Pair;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * AnalyticsController is responsible for registering all account analytics related route
 * {@code base url: /api/v1/analytics}
 */
@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @Autowired
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /**
     * report method is responsible for accepting the request to provide transaction summary
     * @param of indicates timeframe, possible values of are {@code 1, 2, 3}
     * @param accountId is the id of the account
     * @return returns the transaction summary as report
     */
    @GetMapping(path = "/report")
    public ServerResponse<Response.ReportApi> report(@RequestParam int of, @RequestParam Long accountId, HttpServletResponse response) {
        Pair<Response.ReportApi, Error> res = analyticsService.report(accountId, of);
        if (res.getSecond() != null) {
            response.setStatus(res.getSecond().getStatusCode());
            return new ServerResponse<>(false, null, res.getSecond().getErrMsg());
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return new ServerResponse<>(true, res.getFirst(), "");
    }
}
