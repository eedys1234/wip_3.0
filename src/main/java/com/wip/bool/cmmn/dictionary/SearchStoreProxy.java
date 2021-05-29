package com.wip.bool.cmmn.dictionary;

import com.wip.bool.music.song.domain.SongDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component(value = "searchStoreProxy")
@RequiredArgsConstructor
public class SearchStoreProxy implements SearchStore {

    @Resource(name = "kmpStore")
    private SearchStore searchStore;

    private final Helper helper;

    @PostConstruct
    private void init() {
        helper.findAllTitle().stream().forEach(title -> searchStore.insert(title));
    }

    @Override
    public boolean contains(String words) {
        return searchStore.contains(words);
    }

    @Override
    public List<String> findWords(String words) {
        return searchStore.findWords(words);
    }

    @Override
    public boolean insert(String words) {
        return searchStore.insert(words);
    }

    @Override
    public boolean delete(String words) {
        return searchStore.delete(words);
    }

    @Component
    @RequiredArgsConstructor
    public static class Helper {

        private final SongDetailRepository songDetailRepository;

        @Transactional(readOnly = true)
        public List<String> findAllTitle() {
            return songDetailRepository.findAllTitle();
        }
    }
}
