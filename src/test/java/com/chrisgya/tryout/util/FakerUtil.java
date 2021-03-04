package com.chrisgya.tryout.util;

import com.github.javafaker.Faker;


public class FakerUtil {
    private static final Faker faker = new Faker();

    public static String getTitle() {
        return faker.name().prefix();
    }

    public static String getFirstName() {
        return faker.name().firstName();
    }

    public static String getLastName() {
        return faker.name().lastName();
    }

    public static String getMobileNumber() {
         return "234808" + faker.number().digits(7);
    }

    public static String getEmailAddress() {
        return faker.internet().emailAddress();
    }

    public static String getPassword() {
        return faker.internet().password(5, 20, true, true, true);
    }

}
