package com.arondor.viewer.filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.samaxes.filter.CacheFilter;

@Configuration
public class JsLowCacheFilterConfig
{
    @Bean
    public FilterRegistrationBean<CacheFilter> jsLowCacheFilterRegistration()
    {
        FilterRegistrationBean<CacheFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CacheFilter());
        registrationBean.addUrlPatterns("/ARenderJSAPI.js");
        registrationBean.addInitParameter("private", "true");
        registrationBean.addInitParameter("expiration", "86400");
        return registrationBean;
    }
}