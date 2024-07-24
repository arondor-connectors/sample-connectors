package com.arondor.viewer.filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.samaxes.filter.CacheFilter;

@Configuration
public class CacheFilterConfig
{
    @Bean
    public FilterRegistrationBean<CacheFilter> imagesCacheFilterRegistration()
    {
        FilterRegistrationBean<CacheFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CacheFilter());
        registrationBean.addUrlPatterns("*.png", "*.gif", "*.jpg", "*.svg");
        registrationBean.addInitParameter("static", "true");
        registrationBean.addInitParameter("expiration", "2592000");
        return registrationBean;
    }
}