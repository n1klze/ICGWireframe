package ru.nsu.ccfit.melnikov.exceptions;

public class ValueException extends Exception{
    public ValueException(String paramName, int actual, int expectedMin, int expectedMax){
        super("Parameter " + paramName + "=" + actual + " is out of bounds. Expected value between " + expectedMin + " and " + expectedMax);
    }
}
