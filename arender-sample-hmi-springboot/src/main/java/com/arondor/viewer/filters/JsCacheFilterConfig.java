package com.arondor.viewer.filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.samaxes.filter.CacheFilter;

@Configuration
public class JsCacheFilterConfig
{
    @Bean
    public FilterRegistrationBean<CacheFilter> jsCacheFilterRegistration()
    {
        FilterRegistrationBean<CacheFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CacheFilter());
        registrationBean.addUrlPatterns("*.js");
        registrationBean.addInitParameter("private", "true");
        registrationBean.addInitParameter("expiration", "2592000");
        return registrationBean;
    }
}