package com.bookinggo.annotationbasedexperiments.services.impl.experimentframework;

public interface DummyInterface {

    int addition(int number, int otherNumber);
    int subtraction(int number, int otherNumber);
    int multiplication(int number, int otherNumber);
    int division(int number, int otherNumber);
    double pow(double number, int toPow);
    double max(double number, int toPow);

    double sine(double number);
    double cosine(double number);
}
