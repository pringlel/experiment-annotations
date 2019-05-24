package com.bookinggo.annotationbasedexperiments.services.impl;

import com.bookinggo.annotationbasedexperiments.experiment.Experiment;
import com.bookinggo.annotationbasedexperiments.services.ExperimentService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Scope;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Scope("thread")
@ConditionalOnMissingBean(type = "ExperimentService")
public class ExperimentServiceImpl implements ExperimentService {

    private List<Experiment> experiments = new ArrayList<>();

    @Override
    public List<Experiment> getExperiments() {
        return new ArrayList<>(experiments);
    }

    @Override
    public void addExperiment(Experiment experimentToAdd) {
        experiments.add(experimentToAdd);
    }

    @Override
    public void addAllExperiments(List<Experiment> experimentsToAdd) {
        experiments.addAll(experimentsToAdd);
    }

    @Override
    public void removeExperiment(Experiment experiment) {
        experiments.remove(experiment);
    }

    @Override
    public void removeAllExperiments() {
        experiments.clear();
    }

    @Override
    public void impact(int id) {
        experiments.stream()
                .filter(experiment -> experiment.getId() == id)
                .findFirst().ifPresent(experiment -> experiment.setImpacted(true));
    }
}
