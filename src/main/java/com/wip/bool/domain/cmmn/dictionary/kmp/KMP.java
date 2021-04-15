package com.wip.bool.domain.cmmn.dictionary.kmp;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class KMP {

    private String value;

    public KMP() {

    }

    public KMP(String value) {
        this.value = value;
    }

    protected boolean contains(String keyword) {
        List<Integer> list = search(keyword);
        return list.size() > 0;
    }

    protected List<Integer> search(String keyword) {

        List<Integer> list = new ArrayList<>();
        int packageStringLen = value.length();
        int keywordLen = keyword.length();
        int[] table = failureFunc(keyword);
        int j = 0;

        for(int i=0;i<packageStringLen;i++)
        {

            while(j > 0 && value.charAt(i) != keyword.charAt(j)) {


                j = table[j - 1];

            }

            if(value.charAt(i) == keyword.charAt(j)) {

                //문자열을 모두 비교했을 때, 마지막까지 같다면
                if(j == keywordLen - 1) {

                    list.add(i - j);

                    j = table[j];
                }
                else {
                    j++;
                }
            }
        }


        return list;
    }

    void setValue(String value) {
        this.value = value;
    }

    private int[] failureFunc(String keyword) {

        int len = keyword.length();
        int[] table = new int[len];
        int j = 0;

        for(int i=1;i<len;i++)
        {
            while(j > 0 && keyword.charAt(i) != keyword.charAt(j)) {
                j = table[j - 1];
            }

            if(keyword.charAt(i) == keyword.charAt(j)) {
                table[i] = ++j;
            }
        }

        table = new int[value.length()];

        return table;
    }
}
