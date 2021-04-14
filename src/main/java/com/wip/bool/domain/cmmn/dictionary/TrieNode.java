package com.wip.bool.domain.cmmn.dictionary;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class TrieNode {

    private Map<Character, TrieNode> childNodes = new HashMap<>();
    private boolean isLastChar;
    private String name;


    public void setLastChar(boolean isLastChar) {
        this.isLastChar = isLastChar;
    }

    public void setName(String name) {
        this.name = name;
    }
}
