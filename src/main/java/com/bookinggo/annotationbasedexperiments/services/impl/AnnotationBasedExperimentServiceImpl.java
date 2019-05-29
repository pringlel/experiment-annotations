package com.bookinggo.annotationbasedexperiments.services.impl;

import com.bookinggo.annotationbasedexperiments.experiment.AnnotationExperiment;
import com.bookinggo.annotationbasedexperiments.services.AnnotationBasedExperimentService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Scope;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Scope("thread")
@ConditionalOnMissingBean(type = "AnnotationBasedExperimentService")
public class AnnotationBasedExperimentServiceImpl implements AnnotationBasedExperimentService {

    private List<AnnotationExperiment> annotationExperiments = new ArrayList<>();

    @Override
    public List<AnnotationExperiment> getAnnotationExperiments() {
        return new ArrayList<>(annotationExperiments);
    }

    @Override
    public void addExperiment(AnnotationExperiment annotationExperimentToAdd) {
        annotationExperiments.add(annotationExperimentToAdd);
    }

    @Override
    public void removeAllExperiments() {
        annotationExperiments.clear();
    }

    @Override
    public void impact(int id) {
        annotationExperiments.stream()
                .filter(experiment -> experiment.getId() == id)
                .findFirst().ifPresent(experiment -> experiment.setImpacted(true));
    }
}
