package com.xiehn.excel.test;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JDTest {
    public static void main(String[] args) {
        Set<String> keys = new HashSet<>();
        keys.add("a");
        keys.add("b");
        keys.add("c");
        Set<String> appKeys = keys.stream().filter(key -> !key.equals("a")).collect(Collectors.toSet());
        Optional<String> tmpKey = appKeys.stream().findFirst();
        System.out.println(tmpKey.get());

    }
}
