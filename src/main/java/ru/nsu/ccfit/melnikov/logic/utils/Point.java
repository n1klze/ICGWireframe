package ru.nsu.ccfit.melnikov.logic.utils;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class Point {
    private int x;
    private int y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }
    public Point(){
        this(-1,-1);
    }

    public void moveX(int dx){
        x += dx;
    }
    public void moveY(int dy){
        y += dy;
    }

    @Override
    public String toString() {
        return x + ":" + y;
    }
}