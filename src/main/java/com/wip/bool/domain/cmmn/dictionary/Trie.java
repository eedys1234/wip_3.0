package com.wip.bool.domain.cmmn.dictionary;

import java.util.List;

public interface Trie {

    public void insert(String words);

    public boolean contains(String words);

    public void delete(String words);

    public List<String> containWords(String words);
}
