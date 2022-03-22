package org.example.mn.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RedisConfiguration {

    @JsonProperty
    private String host;
    @JsonProperty
    private int port;
}
