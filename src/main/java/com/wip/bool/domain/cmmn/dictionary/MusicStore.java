package com.wip.bool.domain.cmmn.dictionary;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 검색을 위한 Store
 */
@Component
public class MusicStore {

    private final TrieSong prefix = new TrieSong();
    private final TrieSong suffix = new TrieSong();

    public MusicStore() {

    }

    public boolean insert(String words) {

        String reserveWords = new StringBuilder(words).reverse().toString();
        prefix.insert(words);
        suffix.insert(reserveWords);

        return true;
    }

    public Set<String> find(String words) {

        Set<String> findList = new HashSet<>();
        String reserveWords = new StringBuilder(words).reverse().toString();
        findList.addAll(prefix.containWords(words));
        findList.addAll(suffix.containWords(reserveWords)
                .stream()
                .map(entity -> new StringBuilder(entity).reverse().toString())
                .collect(Collectors.toList()));

        return findList;
    }

    public boolean delete(String words) {

        if(prefix.contains(words)) {
            prefix.delete(words);
        }

        StringBuilder sb = new StringBuilder(words).reverse();
        if(suffix.contains(sb.toString())) {
            suffix.delete(sb.toString());
        }

        return true;
    }
}
