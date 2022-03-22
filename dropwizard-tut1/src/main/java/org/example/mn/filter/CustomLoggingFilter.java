package org.example.mn.filter;

import lombok.extern.slf4j.Slf4j;
import org.example.mn.filter.util.Logged;
import org.glassfish.jersey.message.internal.ReaderWriter;

import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;

//@Provider //for some reason this annotation doesn't work. Means it doesn't inject ResourceInfo.
// hence manually registering this filter in App.java
@Slf4j
@Logged
//https://stackoverflow.com/questions/33666406/logging-request-and-response-in-one-place-with-jax-rs
public class CustomLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Context
    private ResourceInfo resourceInfo;
    /**
     * Filter method called before a request has been dispatched to a resource.
     *
     * <p>
     * Filters in the filter chain are ordered according to their {@code javax.annotation.Priority}
     * class-level annotation value.
     * If a request filter produces a response by calling {@link ContainerRequestContext#abortWith}
     * method, the execution of the (either pre-match or post-match) request filter
     * chain is stopped and the response is passed to the corresponding response
     * filter chain (either pre-match or post-match). For example, a pre-match
     * caching filter may produce a response in this way, which would effectively
     * skip any post-match request filters as well as post-match response filters.
     * Note however that a responses produced in this manner would still be processed
     * by the pre-match response filter chain.
     * </p>
     *
     * @param requestContext request context.
     * @throws IOException if an I/O exception occurs.
     * @see PreMatching
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        log.info("Entering in Resource : /{} ", requestContext.getUriInfo().getPath());
        log.info("Method Name : {} ", resourceInfo.getResourceMethod().getName());
        log.info("Class : {} ", resourceInfo.getResourceClass().getCanonicalName());
        logQueryParameters(requestContext);
        logMethodAnnotations();
        logRequestHeader(requestContext);

        //log entity stream...
        String entity = readEntityStream(requestContext);
        if(entity.trim().length() > 0) {
            log.debug("Entity Stream : {}",entity);
        }
    }

    /**
     * Filter method called after a response has been provided for a request
     * (either by a {@link ContainerRequestFilter request filter} or by a
     * matched resource method.
     * <p>
     * Filters in the filter chain are ordered according to their {@code javax.annotation.Priority}
     * class-level annotation value.
     * </p>
     *
     * @param requestContext  request context.
     * @param responseContext response context.
     * @throws IOException if an I/O exception occurs.
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        log.info("RESPONSE:: {}", responseContext.getEntity().toString());
    }

    private void logQueryParameters(ContainerRequestContext requestContext) {
        for (String name : requestContext.getUriInfo().getPathParameters().keySet()) {
            List<String> obj = requestContext.getUriInfo().getPathParameters().get(name);
            String value = null;
            if (null != obj && obj.size() > 0) {
                value = obj.get(0);
            }
            log.info("Query Parameter Name: {}, Value :{}", name, value);
        }
    }

    private void logMethodAnnotations() {
        Annotation[] annotations = resourceInfo.getResourceMethod().getDeclaredAnnotations();
        if (annotations.length > 0) {
            log.debug("----Start Annotations of resource ----");
            for (Annotation annotation : annotations) {
                log.debug(annotation.toString());
            }
            log.debug("----End Annotations of resource----");
        }
    }

    private void logRequestHeader(ContainerRequestContext requestContext) {
        Iterator<String> iterator;
        log.info("----Start Header Section of request ----");
        log.info("Method Type : {}", requestContext.getMethod());
        iterator = requestContext.getHeaders().keySet().iterator();
        while (iterator.hasNext()) {
            String headerName = iterator.next();
            String headerValue = requestContext.getHeaderString(headerName);
            log.info("Header Name: {}, Header Value :{} ",headerName, headerValue);
        }
        log.info("----End Header Section of request ----");
    }

    private String readEntityStream(ContainerRequestContext requestContext)
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final InputStream inputStream = requestContext.getEntityStream();
        final StringBuilder builder = new StringBuilder();
        try
        {
            ReaderWriter.writeTo(inputStream, outStream);
            byte[] requestEntity = outStream.toByteArray();
            if (requestEntity.length == 0) {
                builder.append(" ");
            } else {
                builder.append(new String(requestEntity));
            }

            //Need to set the stream back other REST Resources will not get the original request
            requestContext.setEntityStream(new ByteArrayInputStream(requestEntity) );
        } catch (IOException ex) {
            log.info("----Exception occurred while reading entity stream :{}",ex.getMessage());
        }
        return builder.toString();
    }
}
