package org.example.mn.api;

import com.codahale.metrics.annotation.Timed;
import org.example.mn.config.AppConfiguration;
import org.example.mn.filter.util.Logged;
import org.example.mn.model.Saying;
import org.example.mn.service.HelloWorldService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldApi {
    //private final String helloTemplate;
    //private final String defaultName;

    private final HelloWorldService helloWorldService;
    private final AppConfiguration appConfiguration;

//    public HelloWorldApi(String helloTemplate, String defaultName) {
//        this.helloTemplate = helloTemplate;
//        this.defaultName = defaultName;
//    }

    @Inject
    public HelloWorldApi(AppConfiguration appConfiguration, HelloWorldService helloWorldService){
        this.appConfiguration = appConfiguration;
        this.helloWorldService = helloWorldService;
    }

    @GET
    @Timed
    @Path("hello")
    public Saying syaHello(@QueryParam("name") Optional<String> name){
        //final String value = String.format(helloTemplate, name.orElse(defaultName));
        final String value = String.format(appConfiguration.getHelloTemplate(), name.orElse(appConfiguration.getDefaultName()));
        helloWorldService.hello();
        return new Saying(value);
    }

    @GET
    @Timed
    @Logged //this is custom annotation which ensures logging filter is executed only for this method
    @Path("hello1")
    public Saying syaHello1(@QueryParam("name") Optional<String> name){
        //final String value = String.format(helloTemplate, name.orElse(defaultName));
        final String value = String.format(appConfiguration.getHelloTemplate(), name.orElse(appConfiguration.getDefaultName()));
        helloWorldService.hello();
        return new Saying(value);
    }
}
