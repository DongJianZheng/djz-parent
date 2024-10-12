package com.djz.config;

import com.djz.filter.ExceptionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * <p> @Title FilterConfig
 * <p> @Description 过滤器配置类
 *
 * @author ACGkaka
 * @date 2021/6/16 18:08
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean exceptionFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(this.exceptionFilter());
        registration.setName("ExceptionFilter");
        //此处尽量小，要比其他Filter靠前
        registration.setOrder(-1);
        return registration;
    }

    @Bean
    public Filter exceptionFilter() {
        return new ExceptionFilter();
    }
}
