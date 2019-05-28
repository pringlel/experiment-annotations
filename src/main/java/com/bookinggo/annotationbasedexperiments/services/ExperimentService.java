package com.bookinggo.annotationbasedexperiments.services;

import com.bookinggo.annotationbasedexperiments.experiment.Experiment;

import java.util.Collection;
import java.util.List;

public interface ExperimentService {
    List<Experiment> getExperiments();

    void addExperiment(Experiment experiment);

    void removeAllExperiments();

    void impact(int id);
}
