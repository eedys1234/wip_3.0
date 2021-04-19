package com.wip.bool.domain.cmmn.dictionary.standard;

public class Initializer implements Standard {

    private static final char HANGUL_BEGIN_UNICODE = 44032;
    private static final char HANGUL_LAST_UNICODE = 55203;
    private static final char HANGUL_BASE_UNIT = 588;

    private static final char[] INITIAL_SOUND = {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ',
            'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};

    private boolean isInitialSound(char search) {

        for(char c : INITIAL_SOUND) {
            if(c == search) {
                return true;
            }
        }
        return false;
    }

    private char getInitialSound(char c) {
        int hanBegin = (c - HANGUL_BEGIN_UNICODE);
        int index = hanBegin/HANGUL_BASE_UNIT;
        return INITIAL_SOUND[index];
    }

    private boolean isHangul(char c) {
        return HANGUL_BASE_UNIT <= c && c <= HANGUL_LAST_UNICODE;
    }

    @Override
    public String getValue(String words) {
        int len = words.length();
        char[] temp = new char[words.length()];

        for(int i=0; i<len; i++) {
            if(!isHangul(words.charAt(i)) || isInitialSound(words.charAt(i))) {
                temp[i] = words.charAt(i);
            }
            else {
                temp[i] = getInitialSound(words.charAt(i));
            }
        }

        StringBuilder sb = new StringBuilder();

        for(int i=0;i<temp.length;i++)
        {
            sb.append(temp[i]);
        }

        return sb.toString();
    }
}
