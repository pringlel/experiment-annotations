package com.bookinggo.annotationbasedexperiments.services;

import com.bookinggo.annotationbasedexperiments.experiment.AnnotationExperiment;

import java.util.List;

public interface AnnotationBasedExperimentService {
    List<AnnotationExperiment> getAnnotationExperiments();

    void addExperiment(AnnotationExperiment annotationExperiment);

    void removeAllExperiments();

    void impact(int id);
}
