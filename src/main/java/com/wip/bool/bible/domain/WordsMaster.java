package com.wip.bool.bible.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class WordsMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "words_master_id")
    private Long id;

    @Column(name = "words_name", nullable = false, length = 10, unique = true)
    private String wordsName;

    @Column(name = "words_order", nullable = false)
    private int wordsOrder;

    public static WordsMaster createWordsMaster(String wordsName, int wordsOrder) {
        WordsMaster wordsMaster = new WordsMaster();
        wordsMaster.updateWordsName(wordsName);
        wordsMaster.updateWordsOrder(wordsOrder);
        return wordsMaster;
    }

    public void updateWordsName(String wordsName) {
        this.wordsName = wordsName;
    }

    public void updateWordsOrder(int wordsOrder) {
        this.wordsOrder = wordsOrder;
    }

}
