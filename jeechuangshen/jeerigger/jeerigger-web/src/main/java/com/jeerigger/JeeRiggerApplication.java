package com.jeerigger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class JeeRiggerApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(JeeRiggerApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(JeeRiggerApplication.class);
    }
}


