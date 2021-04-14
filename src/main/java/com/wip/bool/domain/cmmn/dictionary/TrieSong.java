package com.wip.bool.domain.cmmn.dictionary;

import java.util.*;
import java.util.stream.Collectors;

public class TrieSong implements Trie {

    /**
     * 모든 TrieSong 공유
     */
    private static final char HANGUL_BEGIN_UNICODE = 44032;
    private static final char HANGUL_LAST_UNICODE = 55203;
    private static final char HANGUL_BASE_UNIT = 588;

    private static final char[] INITIAL_SOUND = {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ',
    'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};

    private TrieNode rootNode;

    public TrieSong() {
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

//        while(!queue.isEmpty()) {
//
//            TrieModel model = queue.poll();
//            iCount = model.getDepth();
//
//            if(iCount == len) break;
//            char character = words.charAt(iCount);
//
//            if('?' == character) break;
//
//            List<TrieNode> nodes = find(model.getTrieNode(), character);
//
//            for(TrieNode node : nodes) {
//                queue.offer(new TrieModel(node, iCount + 1));
//            }
//        }

        //초성검색
        for(int i=0;i<len;i++)
        {
            char character = words.charAt(i);

            if('%' == character) break;
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

    private List<TrieNode> find(TrieNode rootNode, char value) {

        return rootNode.getChildNodes()
                .keySet()
                .stream()
                .filter(c -> matching(c, value))
                .map(rootNode.getChildNodes()::get)
                .collect(Collectors.toList());
    }

    private boolean matching(char c, char value) {
        return matchingCharacter(c, value) || matchingInitial(c, value);
    }

    private boolean matchingInitial(char c, char value) {
        return isInitialSound(value) && isHangul(c) && getInitialSound(c) == value;
    }

    private boolean matchingCharacter(char c, char value) {
        return !(isInitialSound(value) && isHangul(c)) && value == c;
    }

    private boolean isInitialSound(char search) {

        for(char c : INITIAL_SOUND) {
            if(c == search) {
                return true;
            }
        }
        return false;
    }

    private char getInitialSound(char c) {
        int hanBegin = (c - HANGUL_BEGIN_UNICODE);
        int index = hanBegin/HANGUL_BASE_UNIT;
        return INITIAL_SOUND[index];
    }

    private boolean isHangul(char c) {
        return HANGUL_BASE_UNIT <= c && c <= HANGUL_LAST_UNICODE;
    }
}
