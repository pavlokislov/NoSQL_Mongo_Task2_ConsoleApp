package com.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Category {
    HOME("home"), WORK("work"), PERSONAL("personal");

    private String name;

    public static Category getCategory(String input) {
       return Arrays.stream(Category.values())
                .filter(category -> category.getName().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid category: " + input));

    }
}
