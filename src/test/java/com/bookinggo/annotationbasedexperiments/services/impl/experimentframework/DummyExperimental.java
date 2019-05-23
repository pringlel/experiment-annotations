package com.bookinggo.annotationbasedexperiments.services.impl.experimentframework;

import com.bookinggo.annotationbasedexperiments.annotations.ExperimentOf;
import com.bookinggo.annotationbasedexperiments.experiment.ExperimentVariant;
import org.springframework.stereotype.Service;

@Service
public class DummyExperimental implements DummyInterface {
    @Override
    @ExperimentOf(id = 66666, property = "number.of.beast.experiment", variant = ExperimentVariant.A)
    public int addition(int number, int otherNumber) {
        return number + otherNumber;
    }

    @Override
    @ExperimentOf(id = 66666, property = "number.of.beast.experiment", variant = ExperimentVariant.B)
    public int subtraction(int number, int otherNumber) {
        return number - otherNumber;
    }

    @Override
    @ExperimentOf(id = 66666, property = "number.of.beast.experiment", variant = ExperimentVariant.C)
    public int multiplication(int number, int otherNumber) {
        return number * otherNumber;
    }

    @Override
    @ExperimentOf(id = 66666, property = "number.of.beast.experiment", variant = ExperimentVariant.D)
    public int division(int number, int otherNumber) {
        if(otherNumber == 0){
            return 0;
        }
        return number / otherNumber;
    }

    @Override
    @ExperimentOf(id = 11111, property = "all.the.ones.experiment", variant = ExperimentVariant.A)
    public double pow(double number, int toPow){
        return Math.pow(number,toPow);
    }

    @Override
    @ExperimentOf(id = 11111, property = "all.the.ones.experiment", variant = ExperimentVariant.B)
    public double max(double number, int toPow){
        return Math.max(number,toPow);
    }

    @Override
    @ExperimentOf(id = 3142, property = "sine.cosine", expression ="isLeadTimeLessThanTwentyFourHours()", variant = ExperimentVariant.A)
    public double sine(double number) {
        return Math.sin(number);
    }

    @Override
    @ExperimentOf(id = 3142, property = "sine.cosine", variant = ExperimentVariant.B)
    public double cosine(double number) {
        return Math.cos(number);
    }


}
