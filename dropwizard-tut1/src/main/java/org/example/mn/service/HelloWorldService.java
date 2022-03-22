package org.example.mn.service;

import lombok.extern.slf4j.Slf4j;
import org.example.mn.config.AppConfiguration;
import org.example.mn.dao.HelloWorldDao;

import javax.inject.Inject;

@Slf4j
public class HelloWorldService {
    private final AppConfiguration appConfiguration;
    private final HelloWorldDao helloWorldDao;

    @Inject
    public HelloWorldService(AppConfiguration appConfiguration, HelloWorldDao helloWorldDao){
        this.appConfiguration = appConfiguration;
        this.helloWorldDao = helloWorldDao;
    }

    public void hello(){
      log.info("AppConfiguration from HelloWorldService:: {}", appConfiguration.getDefaultName());
      helloWorldDao.helloDao();
    }
}
