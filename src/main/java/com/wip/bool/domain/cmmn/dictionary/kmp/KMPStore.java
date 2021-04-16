package com.wip.bool.domain.cmmn.dictionary.kmp;

import com.wip.bool.domain.cmmn.dictionary.SearchStore;
import com.wip.bool.domain.cmmn.dictionary.standard.Originer;
import com.wip.bool.domain.cmmn.dictionary.standard.Standard;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
            if(kmp.getValue().equals(words)) {
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

        List<String> tempList = new ArrayList<>();
        String initWords = standard.getValue(words);

          for(int i=0;i<store.size();i++)
          {
              KMP kmp = store.get(i);
              String str = kmp.getValue();
              kmp.setValue(standard.getValue(str));

              if(kmp.contains(initWords)) {
                  tempList.add(str);
              }

              kmp.setValue(str);
          }

          return tempList;
    }

}
