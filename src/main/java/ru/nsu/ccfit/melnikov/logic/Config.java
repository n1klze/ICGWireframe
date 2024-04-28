package ru.nsu.ccfit.melnikov.logic;

import lombok.AllArgsConstructor;
import ru.nsu.ccfit.melnikov.logic.algorithms.BSpline;

public class Config {
    private static Config config;
    private int k;
    private int n;
    private int m;
    private int m1;
    private double zn;
    private double thetaX;
    private double thetaY;
    private double thetaZ;
    private BSpline bSpline = null;

    public static Config getInstance(){
        if(config == null){
            config = new Config();
        }
        return config;
    }

    private Config(){
        k = 4;
        n = 1;
        m = 2;
        m1 = 1;
        zn = 9;
        thetaX = 0;
        thetaY = 90;
        thetaZ = 0;
    }

    public static void setBSpline(BSpline spline){
        getInstance().bSpline = spline;
    }
    public static BSpline getBSpline(){
        if(getInstance().bSpline == null){
            return new BSpline();
        }
        return getInstance().bSpline;
    }

    public static void setK(int k){
        getInstance().k = k;
    }
    public static int getK(){
        return getInstance().k;
    }

    public static void setN(int n){
        getInstance().n = n;
    }
    public static int getN(){
        return getInstance().n;
    }

    public static void setM(int m){
        getInstance().m = m;
    }
    public static int getM(){
        return getInstance().m;
    }

    public static void setM1(int m1){
        getInstance().m1 = m1;
    }
    public static int getM1(){
        return getInstance().m1;
    }

    public static void setZn(double zn){
        getInstance().zn = zn;
    }
    public static double getZn(){
        return getInstance().zn;
    }

    public static void setThetaX(double thetaX){
        getInstance().thetaX = thetaX;
    }
    public static double getThetaX(){
        return getInstance().thetaX;
    }

    public static void setThetaY(double thetaY){
        getInstance().thetaY = thetaY;
    }
    public static double getThetaY(){
        return getInstance().thetaY;
    }

    public static void setThetaZ(double thetaZ){
        getInstance().thetaZ = thetaZ;
    }
    public static double getThetaZ(){
        return getInstance().thetaZ;
    }
}
