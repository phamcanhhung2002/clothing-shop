package com.clothingshop.api.controllers;

import com.clothingshop.api.domain.dtos.StripePaymentDto;
import com.clothingshop.api.services.StripeService;
import com.google.gson.Gson;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
@Log
public class CheckoutController {
    private StripeService stripeService;
    private final Gson gson;

    public CheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
        gson = new Gson();
    }

    @PostMapping(path = "/charge")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> charge(@RequestBody StripePaymentDto paymentDto) {
        try {
            Charge charge = stripeService.charge(paymentDto);
            String json = gson.toJson(charge);
            Map<String, Object> map = gson.fromJson(json, Map.class);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (StripeException e) {
            log.severe(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
