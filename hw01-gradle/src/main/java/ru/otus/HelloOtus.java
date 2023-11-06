package ru.otus;

import com.google.common.base.CaseFormat;

@SuppressWarnings("java:S106")
public class HelloOtus {
    public static void main(String[] args) {
        System.out.println(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, "helloOtus"));
    }
}
