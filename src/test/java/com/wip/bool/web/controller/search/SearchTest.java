package com.wip.bool.web.controller.search;

import com.wip.bool.domain.cmmn.dictionary.MusicStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles(value = "local")
@SpringBootTest
public class SearchTest {

    @Test
    public void Trie_테스트() throws Exception {

        MusicStore store = new MusicStore();
        store.insert("희망이 다가왔어요");
        store.insert("하나님이 용서하심과 같이 하라");
        store.insert("하나님의 전신갑주");
        store.insert("하나님의 사랑이 나의 정죄보다 크네");
        store.insert("이 복음은 하나님의 능력이라");
        store.insert("이 복음은 하나님의 능력이라 하나님");

        Set<String> songs = store.find("하나님");
        System.out.println(songs);
        assertThat(songs.size()).isEqualTo(4);
    }
}
