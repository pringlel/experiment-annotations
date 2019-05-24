package com.bookinggo.annotationbasedexperiments.config;

import com.bookinggo.annotationbasedexperiments.beanfactory.ThreadScopeEnabledBeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.bookinggo.annotationbasedexperiments.services")
public class AppConfig {

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new ThreadScopeEnabledBeanFactoryPostProcessor();
    }
}
