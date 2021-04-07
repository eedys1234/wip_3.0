package com.wip.bool.domain.cmmn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CodeMapper {

    private Map<String, List<? extends CodeModel>> factory = new HashMap<>();

    public void put(String key, List<? extends CodeModel> list) {
        factory.put(key, list);
    }

    public Map<String, List<? extends CodeModel>> getAll() {
        return factory;
    }

    public Map<String, List<? extends CodeModel>> get(String keys) {
        return Arrays.stream(keys.split(","))
                .collect(Collectors.toMap(Function.identity(), key -> factory.get(key)));
    }
}
