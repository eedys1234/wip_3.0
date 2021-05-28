package com.wip.bool.bible.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wip.bool.bible.domain.WordsMaster;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class WordsMasterDto {

    @Getter
    @NoArgsConstructor
    public static class WordsMasterSaveRequest {

        @JsonProperty(value = "words_name")
        @NotBlank
        private String wordsName;

    }

    @Getter
    @NoArgsConstructor
    public static class WordsMasterResponse implements Serializable {

        @JsonProperty(value = "words_master_id")
        private Long wordsMasterId;

        @JsonProperty(value = "words_name")
        private String wordsName;

        @JsonProperty(value = "words_order")
        private Integer wordsOrder;

        public WordsMasterResponse(WordsMaster wordsMaster) {
            this.wordsMasterId = wordsMaster.getId();
            this.wordsName = wordsMaster.getWordsName();
            this.wordsOrder = wordsMaster.getWordsOrder();
        }
    }

}
