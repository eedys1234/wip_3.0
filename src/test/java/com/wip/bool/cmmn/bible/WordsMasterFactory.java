package com.wip.bool.cmmn.bible;

import com.wip.bool.bible.domain.WordsMaster;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class WordsMasterFactory {

    public static String[] wordsNames = {
            "창세기",
            "출애굽기",
            "레위기",
            "민수기",
            "신명기",
            "여호수아",
            "사사기",
            "롯기",
            "사무엘상",
            "사무엘하",
            "열왕기상",
            "열왕기하",
            "역대상",
            "역대하",
            "에스라",
            "느헤미야",
            "에스더",
            "욥기",
            "시편",
            "잠언",
            "전도서",
            "아가서",
            "이사야",
            "예레미야",
            "예레미야애가",
            "에스겔",
            "다니엘",
            "호세아",
            "요엘",
            "아모스",
            "오바다",
            "요나",
            "마가",
            "나훔",
            "하박국",
            "스바냐",
            "학개",
            "스가라",
            "말라기",
            "마태복음",
            "마가복음",
            "누가복음",
            "요한복음",
            "사도행전",
            "로마서",
            "고린도전서",
            "고린도후서",
            "갈라디아서",
            "에베소서",
            "빌립보서",
            "글로새서",
            "데살로니가전서",
            "데살로니가후서",
    };

    public static List<String> getWordsNames() {
        return Arrays.stream(wordsNames).collect(Collectors.toList());
    }

    public static List<WordsMaster> getWordsMasters() {
        AtomicInteger index = new AtomicInteger(1);
        return Arrays.stream(wordsNames)
                .map(wordsName -> {
                    WordsMaster wordsMaster = getWordsMaster(wordsName, index.intValue());
                    index.incrementAndGet();
                    return wordsMaster;
                })
                .collect(Collectors.toList());
    }

    public static List<WordsMaster> getWordsMastersWithId() {
        AtomicInteger index = new AtomicInteger(1);
        return Arrays.stream(wordsNames)
                .map(wordsName -> {
                    WordsMaster wordsMaster = getWordsMaster(wordsName, index.intValue(), index.longValue());
                    index.incrementAndGet();
                    return wordsMaster;
                })
                .collect(Collectors.toList());
    }

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
