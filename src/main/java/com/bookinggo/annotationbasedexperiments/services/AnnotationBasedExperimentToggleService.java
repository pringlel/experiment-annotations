package com.bookinggo.annotationbasedexperiments.services;

public interface AnnotationBasedExperimentToggleService {

    boolean getExperimentToggle(String key);

    void addApplicationProperty(String property, Boolean value);

    void removeApplicationProperty(String property);

    void removeAllApplicationProperties();
}
