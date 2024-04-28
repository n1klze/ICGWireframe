package ru.nsu.ccfit.melnikov.logic;

public enum Parameters {
    K, N, M, M1;

    public int getMin() {
        return switch (this) {
            case K -> 4;
            case M -> 2;
            default -> 1;
        };
    }

    public int getMax() {
        return switch (this) {
            case K, N -> 50;
            case M -> 20;
            default -> 10;
        };
    }

    public String getName(){
        return switch (this) {
            case K -> "K";
            case N -> "N";
            case M -> "M";
            case M1 -> "M1";
        };
    }
}
