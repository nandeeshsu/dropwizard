package org.example.mn.health;

import com.codahale.metrics.health.HealthCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mn.config.AppConfiguration;
import ru.vyarus.dropwizard.guice.module.installer.feature.health.NamedHealthCheck;

import javax.inject.Inject;

@Slf4j
//@RequiredArgsConstructor
//public class HelloWorldHealthCheck extends HealthCheck {
//in the above line, this class will not autowired and need to register in App.java manually
//below line is what it does autowired and no need to register in App.java

public class HelloWorldHealthCheck extends NamedHealthCheck {
    //private final String template;

    private final AppConfiguration appConfiguration;

    @Inject
    public HelloWorldHealthCheck(AppConfiguration appConfiguration){
        this.appConfiguration = appConfiguration;
    }

    /**
     * Perform a check of the application component.
     *
     * @return if the component is healthy, a healthy {@link Result}; otherwise, an unhealthy {@link
     * Result} with a descriptive error message or exception
     * @throws Exception if there is an unhandled error during the health check; this will result in
     *                   a failed health check
     */
    @Override
    protected Result check() throws Exception {
        log.info("HelloWorldHealthCheck::check::template {}", appConfiguration.getHelloTemplate());
        final String value = String.format(appConfiguration.getHelloTemplate(), "TEST");
        log.info("HelloWorldHealthCheck::check::value {}", value);
        if(!value.contains("TEST")){
            return Result.unhealthy("template doesn't include a name");
        }
        return Result.healthy();
    }

    /**
     * @return health check name
     */
    @Override
    public String getName() {
        return "hello-world-health-check";
    }
}
