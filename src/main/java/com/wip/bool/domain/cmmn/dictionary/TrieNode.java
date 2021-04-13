package com.wip.bool.domain.cmmn.dictionary;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class TrieNode {

    private Map<String, TrieNode> childNodes = new HashMap<>();
    private boolean isLastString;
    private String name;

    public TrieNode(String name) {
        this.name = name;
    }

    public void setLastString(boolean isLastString) {
        this.isLastString = isLastString;
    }
}
