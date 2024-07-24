package com.arondor.viewer.filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class EtagFilterConfig
{
    @Bean
    public FilterRegistrationBean<ShallowEtagHeaderFilter> etagFilterRegistration()
    {
        FilterRegistrationBean<ShallowEtagHeaderFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ShallowEtagHeaderFilter());
        registrationBean.addUrlPatterns("*.css", "*.js");
        return registrationBean;
    }
}
