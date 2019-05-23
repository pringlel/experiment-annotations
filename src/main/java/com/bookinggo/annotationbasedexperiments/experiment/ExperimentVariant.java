package com.bookinggo.annotationbasedexperiments.experiment;

public enum ExperimentVariant {
    A,
    B,
    C,
    D,
    E,
    F,
    G,
    H,
    I,
    J;

    public static ExperimentVariant byLetter(String variantLetter) {
        if(variantLetter != null)
            return ExperimentVariant.valueOf(variantLetter.toUpperCase());
        else
            return null;
    }

    public static ExperimentVariant fromIntegerVariant(int ordinal) {
        return ExperimentVariant.values()[ordinal];
    }

    public String letter() {
        return this.name();
    }
}