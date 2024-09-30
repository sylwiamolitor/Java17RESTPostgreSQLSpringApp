package com.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"2020-02-19", "1999-01-10"})
    public void givenString_whenValidateDate_thenReturnCorrectnessStatus(String input) {
        assertTrue(DateValidator.isValid(input));
    }

}
