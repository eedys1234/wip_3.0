package com.wip.bool.domain.cmmn.dictionary;

import com.wip.bool.domain.music.SongDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component(value = "searchStoreProxy")
@RequiredArgsConstructor
public class SearchStoreProxy implements SearchStore {

    @Resource(name = "kmpStore")
    private SearchStore searchStore;
    private final SongDetailRepository songDetailRepository;

    @PostConstruct
    private void init() {
        songDetailRepository.findAll().stream().forEach(title -> searchStore.insert(title));
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
}
