package com.wip.bool.domain.bible;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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


}
