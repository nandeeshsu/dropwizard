package org.example.mn.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter(onMethod_ = {@JsonProperty})
@NoArgsConstructor
public class Saying {
    private String content;

    public Saying(String content) {
        this.content = content;
    }
}
