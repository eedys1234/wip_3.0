package com.wip.bool.search;

import com.wip.bool.cmmn.dictionary.SearchStore;
import com.wip.bool.cmmn.dictionary.kmp.KMPStore;
import com.wip.bool.cmmn.dictionary.standard.Initializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(value = "local")
@SpringBootTest
public class SearchTest {

    @Test
    public void Trie_테스트() throws Exception {

        SearchStore store = new KMPStore(new Initializer());
        store.insert("희망이 다가왔어요");
        store.insert("하나님이 용서하심과 같이 하라");
        store.insert("하나님의 전신갑주");
        store.insert("하나님의 사랑이 나의 정죄보다 크네");
        store.insert("이 복음은 하나님의 능력이라");
        store.insert("이 복음은 하나님의 능력이라 하나님");

        List<String> songs = store.findWords("ㅎㄴㄴ");
        System.out.println(songs);
        assertThat(songs.size()).isEqualTo(5);
    }
}
