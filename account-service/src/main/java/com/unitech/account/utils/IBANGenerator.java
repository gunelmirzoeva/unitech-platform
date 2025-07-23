package com.unitech.account.utils;


import java.util.UUID;

public class IBANGenerator {
    public static String generate() {
        return "AZ" + UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase();
    }
}
