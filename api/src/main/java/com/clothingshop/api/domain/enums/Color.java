package com.clothingshop.api.domain.enums;

import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
public enum Color {
    white("#FFFFFF", (byte) 0),
    black("#000000", (byte) 1),
    red("#FF0000", (byte) 2),
    yellow("#FFD700", (byte) 3),
    blue("#1E90FF", (byte) 4),
    green("#9ACD32", (byte) 5);

    private final String code;
    private final short enumValue;

    Color(String code, short enumValue) {
        this.code = code;
        this.enumValue = enumValue;
    }

    public static Color of(short value) {
        return Stream.of(Color.values())
                .filter(p -> Objects.equals(p.getEnumValue(), value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
