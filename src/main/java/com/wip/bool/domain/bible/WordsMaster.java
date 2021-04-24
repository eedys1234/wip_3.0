package com.wip.bool.domain.bible;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class WordsMaster {

    @Id
    @GeneratedValue
    @Column(name = "words_master_id")
    private Long id;

    @Column(name = "words_name")
    private String wordsName;

    @Column(name = "words_order")
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
