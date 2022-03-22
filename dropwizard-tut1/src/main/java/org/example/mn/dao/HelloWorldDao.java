package org.example.mn.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.mn.config.AppConfiguration;

import javax.inject.Inject;

@Slf4j
public class HelloWorldDao {
    private final AppConfiguration appConfiguration;

    @Inject
    public HelloWorldDao(AppConfiguration appConfiguration){
      this.appConfiguration = appConfiguration;
    }

    public void helloDao(){
        log.info("HelloWorldDao::helloDao::{}",appConfiguration.getDefaultName());
    }
}
