package com.cycle.rental.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cycle.rental.entity.Cycles;
import com.cycle.rental.service.CycleService;


@RestController
@RequestMapping("/api")
public class CycleRestController {
    
    @Autowired
    private CycleService cycleService;

    @GetMapping("/health")
    public String checkhealth() {
        return "healthy";
    }

    @GetMapping("/cycle/list")
    public List<Cycles> all(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        System.out.println(jwt.getClaimAsString("scope"));
        return cycleService.listAvailableCycles();
    }
}