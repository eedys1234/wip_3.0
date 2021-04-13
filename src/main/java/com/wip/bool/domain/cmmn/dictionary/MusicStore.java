package com.wip.bool.domain.cmmn.dictionary;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 검색을 위한 Store
 */
@Component
public class MusicStore {

    private final Map<Integer, Trie> prefix = new ConcurrentHashMap<>();
    private final Map<Integer, Trie> suffix = new ConcurrentHashMap<>();
    private int max = 0;

    public boolean insert(int digit, String words) {

        max = Math.max(max, digit);

        Trie prefixTrie = prefix.get(digit);
        Trie suffixTrie = suffix.get(digit);

        if(Objects.isNull(prefixTrie)) {
            prefixTrie = new TrieSong();
        }

        prefixTrie.insert(words);
        prefix.put(digit, prefixTrie);

        if(Objects.isNull(suffixTrie)) {
            suffixTrie = new TrieSong();
        }

        suffixTrie.insert(new StringBuilder(words).reverse().toString());
        suffix.put(digit, suffixTrie);

        return true;
    }

    public Set<String> find(String words) {

        Set<String> findList = new HashSet<>();
        int len = words.length();

        if(len > max) {
            return Collections.EMPTY_SET;
        }

        StringBuilder prefixSB = new StringBuilder();
        StringBuilder suffixSB = new StringBuilder();

        prefixSB.append(words);
        suffixSB.append(words);

        for(int i=len;i<=max;i++) {
            findList.addAll(prefix.get(i).containWords(prefixSB.toString()));
            findList.addAll(suffix.get(i).containWords(suffixSB.reverse().toString()));
            prefixSB.append("?");
            suffixSB.append("?");
        }

        return findList;
    }

    public boolean delete(String words) {

        int len = words.length();

        if(prefix.get(len).contains(words)) {
            prefix.get(len).delete(words);
        }

        StringBuilder sb = new StringBuilder(words).reverse();
        if(suffix.get(len).contains(sb.toString())) {
            suffix.get(len).delete(sb.toString());
        }

        return true;
    }
}
