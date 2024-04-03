package com.zoho.leadmanagement;

import com.zoho.leadmanagement.filter.UserFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LeadManagementApplication
{

    @Bean
    public FilterRegistrationBean<UserFilter> loggingFilter(){
        FilterRegistrationBean<UserFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new UserFilter());
        registrationBean.addUrlPatterns("/api/*","/index.html","/logout");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    public static void main(String[] args) throws Exception
    {

        SpringApplication.run(LeadManagementApplication.class, args);

    }
}
