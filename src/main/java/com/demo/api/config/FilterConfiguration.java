package com.demo.api.config;

import com.demo.api.filter.AuthenticationFilter;
import com.demo.api.filter.AuthorisationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    private final AuthenticationFilter authenticationFilter;
    private final AuthorisationFilter authorisationFilter;

    @Autowired
    public FilterConfiguration(AuthenticationFilter authenticationFilter, AuthorisationFilter authorisationFilter) {
        this.authenticationFilter = authenticationFilter;
        this.authorisationFilter = authorisationFilter;
    }

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilterRegistration() {
        FilterRegistrationBean<AuthenticationFilter> filterRegistration = new FilterRegistrationBean<>(authenticationFilter);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setName("authenticationFilter");
        filterRegistration.setOrder(1);
        return filterRegistration;
    }

    @Bean
    public FilterRegistrationBean<AuthorisationFilter> authorisationFilterRegistration() {
        FilterRegistrationBean<AuthorisationFilter> registration = new FilterRegistrationBean<>(authorisationFilter);
        registration.addUrlPatterns("/*");
        registration.setName("authorisationFilter");
        registration.setOrder(2);
        return registration;
    }

}
