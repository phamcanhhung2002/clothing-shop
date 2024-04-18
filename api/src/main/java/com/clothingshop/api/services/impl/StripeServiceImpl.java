package com.clothingshop.api.services.impl;

import com.clothingshop.api.domain.dtos.StripePaymentDto;
import com.clothingshop.api.services.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import jakarta.annotation.PostConstruct;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Log
public class StripeServiceImpl implements StripeService {
    @Value("${stripe.secret.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    @Override
    public Charge charge(StripePaymentDto stripePaymentDto) throws StripeException {
        log.info("Stripe api key " + Stripe.apiKey);
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", stripePaymentDto.getAmount());
        chargeParams.put("currency", "USD");
        chargeParams.put("source", stripePaymentDto.getStripeToken());
        return Charge.create(chargeParams);
    }
}
