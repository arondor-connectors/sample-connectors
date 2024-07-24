package com.arondor.viewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@ServletComponentScan
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class,
        JtaAutoConfiguration.class, ReactiveSecurityAutoConfiguration.class })
@Configuration
@ImportResource(locations = { "classpath:arender.xml", "classpath:arender-user-context.xml" })
public class ARenderApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ARenderApplication.class, args);
    }
}