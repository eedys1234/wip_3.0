package com.wip.bool.bible.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "words_master")
public class WordsMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "words_master_id")
    private Long id;

    @Column(name = "words_name", nullable = false, length = 20, unique = true)
    private String wordsName;

    @Column(name = "words_order", nullable = false, unique = true)
    private int wordsOrder;

    public static WordsMaster createWordsMaster(String wordsName, Integer wordsOrder) {
        WordsMaster wordsMaster = new WordsMaster();
        wordsMaster.updateWordsName(wordsName);
        wordsMaster.updateWordsOrder(wordsOrder);
        return wordsMaster;
    }

    public void updateWordsName(String wordsName) {
        this.wordsName = wordsName;
    }

    public void updateWordsOrder(Integer wordsOrder) {

        if(Objects.isNull(wordsOrder)) {
            this.wordsOrder = 0;
        }
        else {
            this.wordsOrder = wordsOrder;
        }
    }

}
