package com.bookinggo.annotationbasedexperiments.services;

import com.bookinggo.annotationbasedexperiments.annotations.ExperimentOf;
import org.aspectj.lang.ProceedingJoinPoint;

public interface AnnotationBasedExperimentFrameworkService {
    Object redirectToExperiment(ProceedingJoinPoint proceedingJoinPoint, ExperimentOf experimentOf) throws Throwable;
}
