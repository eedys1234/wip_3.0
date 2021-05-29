package com.wip.bool.cmmn.dictionary.kmp;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class KMP {

    private String orgValue;
    private String newValue;

    public KMP() {

    }

    public KMP(String value) {
        this.orgValue = value;
        this.newValue = value;
    }

    protected boolean contains(String keyword) {
        List<Integer> list = search(keyword);
        return !list.isEmpty();
    }

    protected List<Integer> search(String keyword) {

        List<Integer> list = new ArrayList<>();
        int packageStringLen = newValue.length();
        int keywordLen = keyword.length();
        int[] table = failureFunc(keyword);
        int j = 0;

        for(int i=0;i<packageStringLen;i++)
        {

            while(j > 0 && newValue.charAt(i) != keyword.charAt(j)) {


                j = table[j - 1];

            }

            if(newValue.charAt(i) == keyword.charAt(j)) {

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
        this.newValue = value;
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

        table = new int[newValue.length()];

        return table;
    }
}
