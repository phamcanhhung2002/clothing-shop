package com.clothingshop.api.domain.enums;

import lombok.Getter;

@Getter
public enum TimeZoneSign {
    POSITIVE("+"),
    NEGATIVE("-");

    final String label;

    private TimeZoneSign(String label) {
        this.label = label;
    }
}
