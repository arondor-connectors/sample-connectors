package com.arondor.viewer;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.stereotype.Component;

@Component
public class SpringBootErrorPageRegistrar implements ErrorPageRegistrar
{

    @Override
    public void registerErrorPages(ErrorPageRegistry registry)
    {
        registry.addErrorPages(new ErrorPage("/arendergwt/exceptionHandlerServlet"));
    }
}
