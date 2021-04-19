package com.wip.bool.domain.cmmn.dictionary.kmp;

import com.wip.bool.domain.cmmn.dictionary.SearchStore;
import com.wip.bool.domain.cmmn.dictionary.standard.Originer;
import com.wip.bool.domain.cmmn.dictionary.standard.Standard;
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

        for(KMP kmp : store) {
            if(kmp.getOrgValue().equals(words)) {
                return store.remove(kmp);
            }
        }
        return false;
    }

    @Override
    public boolean contains(String words) {
        return findWords(words).size() > 0;
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
                .collect(Collectors.toList());
    }

}
