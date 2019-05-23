package com.bookinggo.annotationbasedexperiments.services;

public interface ExperimentToggleService {

    boolean getExperimentToggle(String key);

    void addApplicationProperty(String property, Boolean value);

    void removeApplicationProperty(String property);

    void removeAllApplicationProperties();
}
