package com.wip.bool.cmmn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CodeMapper<T> {

    private final Map<String, List<T>> factory = new HashMap<>();

    public void put(String key, List<T> list) {
        factory.put(key, list);
    }

    public Map<String, List<T>> getAll() {
        return factory;
    }

    public Map<String, List<T>> get(String keys) {
        return Arrays.stream(keys.split(","))
                .collect(Collectors.toMap(Function.identity(), factory::get)); //test 해본 후 변경예정
    }
}
