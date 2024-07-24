package com.arondor.viewer.filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.planetj.servlet.filter.compression.CompressingFilter;

@Configuration
public class GzipFilterConfig
{
    @Bean
    public FilterRegistrationBean<CompressingFilter> gzipFilter()
    {
        FilterRegistrationBean<CompressingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CompressingFilter());
        registrationBean.addUrlPatterns("*.js", "*.css", "*.jsp", "/arendergwt/imageServletSVG",
                "/arendergwt/cropImageServlet");

        return registrationBean;
    }

}
