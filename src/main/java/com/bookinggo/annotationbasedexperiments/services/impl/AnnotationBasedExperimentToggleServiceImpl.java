package com.bookinggo.annotationbasedexperiments.services.impl;

import com.bookinggo.annotationbasedexperiments.services.AnnotationBasedExperimentToggleService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnnotationBasedExperimentToggleServiceImpl implements AnnotationBasedExperimentToggleService {
    private Map<String, Boolean> applicationPropertyMap = new HashMap<>();

    @Override
    public boolean getExperimentToggle(String applicationPropertyKey) {
        return applicationPropertyMap.getOrDefault(applicationPropertyKey, false);
    }

    @Override
    public void addApplicationProperty(String property, Boolean value) {
        applicationPropertyMap.put(property, value);
    }

    @Override
    public void removeApplicationProperty(String property) {
        applicationPropertyMap.remove(property);
    }

    @Override
    public void removeAllApplicationProperties() {
        applicationPropertyMap.clear();
    }
}
