package com.wip.bool.domain.cmmn.dictionary;

import java.util.*;
import java.util.stream.Collectors;

public class TrieSong implements Trie {

    private TrieNode rootNode;
    private List<String> containWords;

    public TrieSong() {
        this.rootNode = new TrieNode();
        this.containWords = new ArrayList<>();
    }

    //추가
    public void insert(String words) {

        TrieNode thisNode = this.rootNode;

        for(int i=0;i<words.length();i++)
        {
            thisNode = thisNode.getChildNodes().computeIfAbsent(String.valueOf(words.charAt(i)), TrieNode::new);
        }

        thisNode.setLastString(true);
    }

    //검색
    public boolean contains(String words) {

        TrieNode thisNode = this.rootNode;

        for(int i=0;i<words.length();i++)
        {
            TrieNode node = thisNode.getChildNodes().get(String.valueOf(words.charAt(i)));
            if(Objects.isNull(node)) {
                return false;
            }
            thisNode = node;
        }

        return thisNode.isLastString();
    }

    public List<String> containWords(String words) {

        TrieNode thisNode = this.rootNode;
        int len = words.length();

        for(int i=0;i<len;i++)
        {
            String str = String.valueOf(words.charAt(i));
            if("?".equals(str)) break;
            TrieNode node = thisNode.getChildNodes().get(str);

            if(Objects.isNull(node)) {
                return null;
            }

            thisNode = node;
        }

        //단어 자체 추가
        if(thisNode.isLastString()) {
            containWords.add(words);
        }

        //자식이 있을경우
        if(thisNode.getChildNodes().size() > 0) {
            dfs(thisNode, new ArrayDeque<>());
        }

        List<String> tempList = new ArrayList();
        tempList.addAll(containWords);
        containWords.clear();

        return tempList;
    }

    private void dfs(TrieNode root, Deque<String> deque) {

        //END
        if(root.getChildNodes().isEmpty()) {
            containWords.add(deque.stream().collect(Collectors.joining()));
            deque.removeLast();
            return ;
        }

        for(Map.Entry<String, TrieNode> node : root.getChildNodes().entrySet()) {
            deque.addLast(node.getKey());
            dfs(node.getValue(), deque);
        }
    }

    public void delete(String words) {
        delete(this.rootNode, words, 0);
    }

    private void delete(TrieNode thisNode, String words, int index) {

        String str = String.valueOf(words.charAt(index));

        if(!thisNode.getChildNodes().containsKey(str)) {
            throw new IllegalArgumentException();
        }

        TrieNode childNode = thisNode.getChildNodes().get(str);
        index++;

        if(index == words.length()) {

            if(!childNode.isLastString()) {
                throw new IllegalArgumentException();
            }

            childNode.setLastString(false);

            if(childNode.getChildNodes().isEmpty()) {
                thisNode.getChildNodes().remove(str);
            }
        }
        else {

            delete(childNode, words, index);

            if(!childNode.isLastString() && childNode.getChildNodes().isEmpty()) {
                thisNode.getChildNodes().remove(str);
            }
        }
    }
}
