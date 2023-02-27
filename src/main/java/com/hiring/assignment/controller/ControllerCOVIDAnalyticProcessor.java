package com.hiring.assignment.controller;

import com.hiring.assignment.dto.DtoResponse;
import com.hiring.assignment.service.ServiceCOVIDAnalyticProcessor;
import com.hiring.assignment.utils.Utils;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/analytics")
public class ControllerCOVIDAnalyticProcessor {

    @Autowired
    ServiceCOVIDAnalyticProcessor serviceCOVIDAnalyticProcessor;

    @GetMapping(value = "/getCurrentLoggedInUsers")
    public DtoResponse getCurrentLoggedInUsers() throws URISyntaxException, ParseException {
        DtoResponse response = new DtoResponse();
        List<String> loggedInUsers = serviceCOVIDAnalyticProcessor.getCurrentLoggedInUsers();
        if (loggedInUsers != null && !loggedInUsers.isEmpty()) {
            response.setStatus(HttpStatus.OK.name());
            response.setMessage("Current Loggedin Users Get Success");
            response.setBody(loggedInUsers);
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.name());
            response.setMessage("Current Loggedin Users Get Failed");
        }
        return response;
    }

    @GetMapping(value = "/getAllCasesReportedToday")
    public DtoResponse getAllCasesReportedToday() throws IllegalStateException, IOException {
        DtoResponse response = new DtoResponse();
        Long count = serviceCOVIDAnalyticProcessor.getAllCasesReportedToday();
        if (count != null) {
            response.setStatus(HttpStatus.OK.name());
            response.setMessage("Result Get Success");
            response.setBody("No of Cases Reported Today = " + count);
        }
        return response;
    }

    @GetMapping(value = "/getAllCasesReportedTodayCountryWise")
    public DtoResponse getAllCasesReportedTodayCountryWise() throws IllegalStateException, IOException {
        DtoResponse response = new DtoResponse();
        Map<String, Long> count = serviceCOVIDAnalyticProcessor.getAllCasesReportedTodayCountryWise();
        if (count != null) {
            response.setStatus(HttpStatus.OK.name());
            response.setMessage("Result Get Success");
            response.setBody(count);
        }
        return response;
    }

    @GetMapping(value = "/getReportedCasesByCountryName")
    public DtoResponse getReportedCasesByCountryName(@RequestParam String countryName)
            throws IllegalStateException, IOException {
        DtoResponse response = new DtoResponse();
        Map<String, Long> count = serviceCOVIDAnalyticProcessor.getReportedCasesByCountryName(countryName);
        if (count != null) {
            response.setStatus(HttpStatus.OK.name());
            response.setMessage("Result Get Success");
            response.setBody(count);
        }
        return response;
    }

    @GetMapping(value = "/getTopNReportedCases")
    public DtoResponse getTopNReportedCases(@RequestParam Long limit) throws IllegalStateException, IOException {
        DtoResponse response = new DtoResponse();
        Map<String, Long> count = serviceCOVIDAnalyticProcessor.getTopNReportedCases(limit);
        if (count != null) {
            response.setStatus(HttpStatus.OK.name());
            response.setMessage("Result Get Success");
            response.setBody(count);
        }
        return response;
    }

    @GetMapping(value = "/getCasesCountByCountryAndDate")
    public DtoResponse getCasesCountByCountryAndDate(@RequestParam(name = "countryName") String countryName,
                                                     @RequestParam(name = "date") String date) throws IllegalStateException, IOException {
        DtoResponse response = new DtoResponse();
        Map<String, Long> count = serviceCOVIDAnalyticProcessor.getCasesCountByCountryAndDate(countryName,
                Utils.stringToLocalDate(date), date);
        if (count != null) {
            response.setStatus(HttpStatus.OK.name());
            response.setMessage("Result Get Success");
            response.setBody(count);
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.name());
            response.setMessage("Result Get Failed");
        }
        return response;
    }

}
