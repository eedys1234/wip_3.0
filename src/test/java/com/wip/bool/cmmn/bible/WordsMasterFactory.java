package com.wip.bool.cmmn.bible;

import com.wip.bool.bible.domain.WordsMaster;
import org.springframework.test.util.ReflectionTestUtils;

public class WordsMasterFactory {

    public static WordsMaster getWordsMaster() {
        WordsMaster wordsMaster = getWordsMaster("마태복음");
        return wordsMaster;
    }

    public static WordsMaster getWordsMaster(Long id) {
        WordsMaster wordsMaster = getWordsMaster("마태복음");
        ReflectionTestUtils.setField(wordsMaster, "id", id);
        return wordsMaster;
    }

    public static WordsMaster getWordsMaster(String wordsName) {
        WordsMaster wordsMaster = getWordsMaster(wordsName, 1);
        return wordsMaster;
    }

    public static WordsMaster getWordsMaster(String wordsName, Long id) {
        WordsMaster wordsMaster = getWordsMaster(wordsName, 1);
        ReflectionTestUtils.setField(wordsMaster, "id", id);
        return wordsMaster;
    }

    public static WordsMaster getWordsMaster(String wordsName, int wordsOrder) {
        WordsMaster wordsMaster = WordsMaster.createWordsMaster(wordsName, wordsOrder);
        return wordsMaster;
    }

    public static WordsMaster getWordsMaster(String wordsName, int wordsOrder, Long id) {
        WordsMaster wordsMaster = WordsMaster.createWordsMaster(wordsName, wordsOrder);
        ReflectionTestUtils.setField(wordsMaster, "id", id);
        return wordsMaster;
    }
}
