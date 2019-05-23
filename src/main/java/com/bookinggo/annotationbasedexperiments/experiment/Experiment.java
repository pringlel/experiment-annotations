package com.bookinggo.annotationbasedexperiments.experiment;

import lombok.ToString;

@ToString
public class Experiment {

    private int id;
    private String variant;
    private boolean impacted;

    public Experiment(int id, String variant, boolean used) {
        this.id = id;
        this.variant = variant;
        this.impacted = used;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Experiment)) return false;

        Experiment that = (Experiment) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getId() {
        return id;
    }

    public boolean isImpacted() {
        return impacted;
    }

    public void setImpacted(boolean impacted) {
        this.impacted = impacted;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }
}
