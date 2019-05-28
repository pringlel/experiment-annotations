package com.bookinggo.annotationbasedexperiments.experiment;

public enum ExperimentVariant {
    A,
    B,
    C,
    D;

    public String letter() {
        return this.name();
    }
}