package com.arondor.viewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.arondor.viewer.server.security.RequestParameterAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests().antMatchers("/**").permitAll().and()
                .addFilterBefore(urlFilter(), BasicAuthenticationFilter.class).authorizeRequests();
        http.csrf().disable();
        // Needed for file uploading because of the dependency spring-security
        // 4.x
        http.headers().frameOptions().sameOrigin();

    }

    @Bean
    public RequestParameterAuthenticationFilter urlFilter() throws Exception
    {
        RequestParameterAuthenticationFilter filter = new RequestParameterAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }
}