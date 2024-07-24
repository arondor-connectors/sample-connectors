package com.arondor.viewer.filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.samaxes.filter.NoCacheFilter;

@Configuration
public class JsNoCacheFilterConfig
{
    @Bean
    public FilterRegistrationBean<NoCacheFilter> jsNoCacheFilterRegistration()
    {
        FilterRegistrationBean<NoCacheFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new NoCacheFilter());
        registrationBean.addUrlPatterns("/arendergwt/arendergwt.nocache.js");
        return registrationBean;
    }
}