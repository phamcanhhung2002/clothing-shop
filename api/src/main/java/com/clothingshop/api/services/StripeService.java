package com.clothingshop.api.services;

import com.clothingshop.api.domain.dtos.StripePaymentDto;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

public interface StripeService {
    Charge charge(StripePaymentDto stripePaymentDto) throws StripeException;
}
