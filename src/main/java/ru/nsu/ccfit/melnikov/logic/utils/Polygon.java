package ru.nsu.ccfit.melnikov.logic.utils;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Setter @Getter
public class Polygon {
    private Point center;
    private int radius;
    private int peaks;
    private Color color;
    private int rotation;

    public Polygon(Point center, int r, int n, Color color, int rotation){
        this.center = center;
        this.radius = r;
        this.peaks = n;
        this.color = color;
        this.rotation = rotation;
    }

    public void setCenter(int x, int y){
        center.setX(x);
        center.setY(y);
    }

    public int getX(){
        return center.getX();
    }
    public void setX(int x){
        center.setX(x);
    }

    public int getY(){
        return center.getY();
    }
    public void setY(int y){
        center.setY(y);
    }

}
