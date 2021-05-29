package com.wip.bool.cmmn.dictionary.kmp;

import com.wip.bool.cmmn.dictionary.SearchStore;
import com.wip.bool.cmmn.dictionary.standard.Originer;
import com.wip.bool.cmmn.dictionary.standard.Standard;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component(value = "kmpStore")
public class KMPStore implements SearchStore {

    private List<KMP> store = new ArrayList<>();
    private Standard standard;

    public KMPStore() {
        this.standard = new Originer();
    }

    public KMPStore(Standard standard) {
        this.standard = standard;
    }

    @Override
    public boolean insert(String words) {
        return store.add(new KMP(words));
    }

    @Override
    public boolean delete(String words) {

        //TODO : remove 시 전체 배열을 모두 복사할 경우가 존재하여 이 때의 시간복잡도는 O(n^2)가 될 수 있으므로
        // filter를 이용하여 제거.
        store = store.stream()
                .filter(kmp -> !kmp.getOrgValue().equals(words))
                .collect(Collectors.toList());

        return true;
    }

    @Override
    public boolean contains(String words) {
        List<String> findWords = findWords(words);
        return !findWords.isEmpty() &&  findWords.size() > 0;
    }

    @Override
    public List<String> findWords(String words) {

        String initWords = standard.getValue(words);

        //TODO : 객체 생성에 대한 Resource 고려한 코드.....
        return store.stream()
                .map(kmp -> {
                    kmp.setValue(standard.getValue(kmp.getOrgValue()));
                    return kmp;
                })
                .filter(kmp -> kmp.contains(initWords))
                .map(KMP::getOrgValue)
                .sorted()
                .collect(Collectors.toList());
    }

}
