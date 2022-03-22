package org.example.mn;

//import de.thomaskrille.dropwizard_template_config.TemplateConfigBundle;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import org.example.mn.api.HelloWorldApi;
import org.example.mn.config.AppConfiguration;
import org.example.mn.config.ConfigYaml;
import org.example.mn.config.CustomConfigurationProvider;
import org.example.mn.filter.CustomLoggingFilter;
import org.example.mn.health.HelloWorldHealthCheck;
import org.example.mn.service.HelloWorldService;
import ru.vyarus.dropwizard.guice.GuiceBundle;

@Slf4j
public class App extends io.dropwizard.Application<AppConfiguration> {
    /**
     * Initializes the application bootstrap.
     *
     * @param bootstrap the application bootstrap
     */
    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        //super.initialize(bootstrap);

        //This is needed to load the config yml within the App jar
        bootstrap.setConfigurationSourceProvider(new CustomConfigurationProvider());

        //This is need to replace the environment variable in the config yml
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)
                )
        );

        /*
            Initially I thought it's needed however I tested removing this and it works fine. Looks like it is not needed
            Dropwizard bundle that enables you to write your config.yaml as a Freemarker template for more advanced config.
            This is needed to load the config yml within the App jar as resource stream
            otherwise with dropwizard it doesn't work. With dropwizard will have to pass the config yaml as jar argument via cli
            This TemplateConfigBundle is the extension library built on top of dropwizard.
        */

        //bootstrap.addBundle(new TemplateConfigBundle());

        bootstrap.addBundle(GuiceBundle.builder().enableAutoConfig(getClass().getPackage().getName()).build());
    }

    /**
     * When the application runs, this is called after the {@link ConfiguredBundle}s are run. Override it to add
     * providers, resources, etc. for your application.
     *
     * @param configuration the parsed {@link Configuration} object
     * @param environment   the application's {@link Environment}
     * @throws Exception if something goes wrong
     */
    @Override
    public void run(AppConfiguration configuration, Environment environment) throws Exception {
        log.info("REDIS HOST:: {}", configuration.getRedisConfiguration().getHost());

        //This is not needed if we enable Guice autoconfig. Below is kind of not doing as auto config instead a manual approach
//        final HelloWorldApi helloWorldApi =
//                new HelloWorldApi(configuration.getHelloTemplate(), configuration.getDefaultName());
//        environment.jersey().register(helloWorldApi);
//
        //This is needed only if without using autowired using  extend NamedHealthCheck
        //This is kind of manually injecting
        //final HelloWorldHealthCheck helloWorldHealthCheck = new HelloWorldHealthCheck(configuration.getHelloTemplate());
        //environment.healthChecks().register("hello-world-health-check", helloWorldHealthCheck);

        environment.jersey().register(CustomLoggingFilter.class);
    }

    public static void main(String[] args) throws Exception {
        if(null != args && args.length == 2){
            new App().run(args);
        }else {
            String[] cliArgs = {"server", ConfigYaml.getconfigYml()};
            new App().run(cliArgs);
        }
    }
}
