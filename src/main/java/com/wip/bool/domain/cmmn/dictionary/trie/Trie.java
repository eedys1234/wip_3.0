package com.wip.bool.domain.cmmn.dictionary.trie;

import java.util.*;

public class Trie {

    /**
     * 모든 TrieSong 공유
     */

    private TrieNode rootNode;

    public Trie() {
        this.rootNode = new TrieNode();
    }

    //추가
    public void insert(String words) {

        TrieNode thisNode = this.rootNode;

        for(int i=0;i<words.length();i++)
        {
            thisNode = thisNode.getChildNodes().computeIfAbsent(words.charAt(i), c -> new TrieNode());
        }

        thisNode.setLastChar(true);
        thisNode.setName(words);
    }

    //검색
    public boolean contains(String words) {

        TrieNode thisNode = this.rootNode;

        for(int i=0;i<words.length();i++)
        {
            TrieNode node = thisNode.getChildNodes().get(words.charAt(i));
            if(Objects.isNull(node)) {
                return false;
            }
            thisNode = node;
        }

        return thisNode.isLastChar();
    }

    public List<String> containWords(String words) {

        List<String> tempList = new ArrayList<>();
        TrieNode thisNode = this.rootNode;
        int len = words.length();

        for(int i=0;i<len;i++)
        {
            char character = words.charAt(i);

//            if('%' == character) break;
            TrieNode node = thisNode.getChildNodes().get(character);

            if(Objects.isNull(node)) {
                return Collections.EMPTY_LIST;
            }

            thisNode = node;
        }

        if(thisNode.isLastChar()) {
            tempList.add(thisNode.getName());
        }

        Stack<TrieNode> stack = new Stack<>();

        for(TrieNode node : thisNode.getChildNodes().values()) {
            stack.push(node);
        }

        while(!stack.isEmpty()) {

            TrieNode node = stack.pop();

            if(node.isLastChar()) {
                tempList.add(node.getName());
            }
            stack.addAll(node.getChildNodes().values());
        }

        return tempList;
    }

//    public List<String> containWords(String words, boolean isInitialSound) {
//
//        List<String> tempList = new ArrayList<>();
//        TrieNode thisNode = this.rootNode;
//        int len = words.length();
//
//        if(!isInitialSound) {
//            return containWords(words);
//        }
//
//        Stack<TrieNode> stack = new Stack<>();
//        stack.push(thisNode);
//        while(!stack.isEmpty()) {
//            TrieNode node = stack.pop();
//            if(node.isLastChar()) {
//                if(node.getName().length() >= len) {
//                    if(matching(node.getName(), words)) {
//                        tempList.add(node.getName());
//                    }
//                }
//            }
//            stack.addAll(node.getChildNodes().values());
//        }
//
//        return tempList;
//    }

    public void delete(String words) {
        delete(this.rootNode, words, 0);
    }

    private void delete(TrieNode thisNode, String words, int index) {

        char character = words.charAt(index);

        if(!thisNode.getChildNodes().containsKey(character)) {
            throw new IllegalArgumentException();
        }

        TrieNode childNode = thisNode.getChildNodes().get(character);
        index++;

        if(index == words.length()) {

            if(!childNode.isLastChar()) {
                throw new IllegalArgumentException();
            }

            childNode.setLastChar(false);

            if(childNode.getChildNodes().isEmpty()) {
                thisNode.getChildNodes().remove(character);
            }
        }
        else {

            delete(childNode, words, index);

            if(!childNode.isLastChar() && childNode.getChildNodes().isEmpty()) {
                thisNode.getChildNodes().remove(character);
            }
        }
    }
}
