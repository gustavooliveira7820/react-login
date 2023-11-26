package com.gusta.lever.utils;

public class ParameterValidation {
    public static <V> void checkIfIsNullOrBlankThrowingEx(boolean aBoolean) {
        if (aBoolean) {
            throw new IllegalArgumentException("Please put a valid value");
        }
    }
}
