package com.rupp.spring.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rupp.spring.service.CountryService;

@Controller
@RequestMapping("countries")
public class CountryController {
    @Autowired
    private CountryService service;
    
    @Secured(value = {"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(value = "/v1/all", method = RequestMethod.GET)
    public ResponseEntity<Collection<String>> getAllCountries() {
     final Collection<String> countries = service.getAll();
     return new ResponseEntity<>(countries, HttpStatus.OK);

    }
}
