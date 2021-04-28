package com.wip.bool.cmmn.dictionary;

import java.util.List;

public interface SearchStore {

    public boolean contains(String words);
    public List<String> findWords(String words);
    public boolean insert(String words);
    public boolean delete(String words);
}
