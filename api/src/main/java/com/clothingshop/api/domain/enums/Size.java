package com.clothingshop.api.domain.enums;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Size {
    XS((short) 0),
    S((short) 1),
    M((short) 2),
    L((short) 3),
    XL((short) 4);

    private final short enumValue;

    Size(short enumValue) {
        this.enumValue = enumValue;
    }

    public static Size of(short value) {
        return Stream.of(Size.values())
                .filter(p -> p.enumValue == value)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
