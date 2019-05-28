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

}
