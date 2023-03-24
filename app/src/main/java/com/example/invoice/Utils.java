package com.example.invoice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Utils {

    public static String getMonthOfDate(LocalDate localDate) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return localDate.format(dateTimeFormatter);
    }

    public static String getCurrentMonth() {
        return getMonthOfDate(LocalDate.now());
    }


}
