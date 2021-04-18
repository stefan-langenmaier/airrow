package net.langenmaier.airrow.backend.app.filter;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.core.ResteasyContext;
import org.jboss.resteasy.spi.HttpRequest;


// https://stackoverflow.com/questions/61386439/unable-to-find-a-messagebodyreader-error-with-multipart-request-containing-a-f
@ApplicationScoped
@Provider
public class MultipartFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // apply next only for your form-data path and ignore all the other requests
        final HttpRequest httpRequest = ResteasyContext.getContextData(HttpRequest.class);
        httpRequest.setAttribute("resteasy.provider.multipart.inputpart.defaultContentType", "application/json");
    }
}