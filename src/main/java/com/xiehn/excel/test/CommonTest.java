package com.xiehn.excel.test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CommonTest {
    public static void main(String[] args) {
        // Long start = 1722398403296l;
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println(currentTimeMillis);
        //converDate();
    }
    public static void converDate(long currentTimeMillis){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(localDateTime.format(formatter));
    }
}
