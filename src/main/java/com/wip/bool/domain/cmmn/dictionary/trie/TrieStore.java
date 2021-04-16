package com.wip.bool.domain.cmmn.dictionary.trie;

import com.wip.bool.domain.cmmn.dictionary.SearchStore;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 검색을 위한 Store(자동완성 가능)
 */
@Component(value = "trieStore")
public class TrieStore implements SearchStore {

    private final Trie prefix = new Trie();
    private final Trie suffix = new Trie();

    public TrieStore() {

    }
    @Override
    public boolean insert(String words) {

        String reserveWords = new StringBuilder(words).reverse().toString();
        prefix.insert(words);
        suffix.insert(reserveWords);

        return true;
    }

    @Override
    public List<String> findWords(String words) {

        Set<String> findList = new HashSet<>();
        String reserveWords = new StringBuilder(words).reverse().toString();
        findList.addAll(prefix.containWords(words));
        findList.addAll(suffix.containWords(reserveWords)
                .stream()
                .map(entity -> new StringBuilder(entity).reverse().toString())
                .collect(Collectors.toList()));

        return findList.stream()
                .sorted((String s1, String s2) -> s1.compareTo(s2))
                .collect(Collectors.toList());
    }

    @Override
    public boolean contains(String words) {
        String reserveWords = new StringBuilder(words).reverse().toString();
        return prefix.contains(words) || suffix.contains(reserveWords);
    }

    @Override
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
