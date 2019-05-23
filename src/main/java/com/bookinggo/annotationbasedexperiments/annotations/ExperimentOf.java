package com.bookinggo.annotationbasedexperiments.annotations;

import com.bookinggo.annotationbasedexperiments.experiment.ExperimentVariant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExperimentOf {

    int id();
    String property();
    String expression() default "";
    ExperimentVariant variant();
}
