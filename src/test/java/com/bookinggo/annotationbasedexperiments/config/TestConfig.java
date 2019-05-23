package com.bookinggo.annotationbasedexperiments.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.bookinggo.annotationbasedexperiments"})
public class TestConfig {
}
