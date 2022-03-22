package org.example.mn.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

//@Getter(onMethod_ = {@JsonProperty})
//@Setter(onMethod_ = {@JsonProperty})
@Getter
//@Setter
public class AppConfiguration extends Configuration {
    @NotNull
    @JsonProperty
    private String helloTemplate;

    @NotNull
    @JsonProperty
    private final String defaultName = "Stranger";

    @JsonProperty("redis")
    private RedisConfiguration redisConfiguration;
}
