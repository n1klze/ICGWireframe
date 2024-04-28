package ru.nsu.ccfit.melnikov.logic.algorithms;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.ccfit.melnikov.logic.Config;
import ru.nsu.ccfit.melnikov.logic.Constants;
import ru.nsu.ccfit.melnikov.logic.utils.Point;

import java.util.ArrayList;
import java.util.List;

public class BSpline {
    @Getter @Setter
    private List<Point> controlPoints;
    @Getter
    private List<Point> curvePoints;
    private final int[] matrixM;
    private int curPoint;
    @Setter @Getter
    private Point center;

    public BSpline(){
        matrixM = new int[]{
                -1, 3, -3, 1,
                3, -6, 3, 0,
                -3, 0, 3, 0,
                1, 4, 1, 0
        };

        controlPoints = new ArrayList<>();
        addPoint(-150, 50);
        addPoint(-50, -50);
        addPoint(50, 50);
        addPoint(150, -50);

        makeCurve();
    }
    public BSpline(List<Point> controlPoints){
        matrixM = new int[]{
                -1, 3, -3, 1,
                3, -6, 3, 0,
                -3, 0, 3, 0,
                1, 4, 1, 0
        };

        this.controlPoints = controlPoints;
//        addPoint(-150, 50);
//        addPoint(-50, -50);
//        addPoint(50, 50);
//        addPoint(150, -50);

        makeCurve();
    }

    public int getControlPointsNum(){
        return controlPoints.size();
    }
    public int getCurvePointsNum(){
        return curvePoints.size();
    }
    public Point getControlPoint(int i){return controlPoints.get(i);}
    public Point getLastControlPoint(){
        return controlPoints.get(controlPoints.size()-1);
    }
    public Point getCurvePoint(int i){return  curvePoints.get(i); }
    public void removePoint(){
        controlPoints.remove(controlPoints.size()-1);
    }

    public int getMeanX(){
        int mean = 0;
        for(Point point: curvePoints){
            mean += point.getX();
        }
        return mean/curvePoints.size();
    }

    public void addPoint(int x, int y){
        if(isNewPoint(x, y)){
            controlPoints.add(new Point(x,y));
            Config.setK(controlPoints.size());
        }
        else{
            movePoint(x,y);
        }
    }

    public void delPoint(int x, int y){
        if(controlPoints.size() <= Constants.MIN_K || isNewPoint(x, y))
            return;

        controlPoints.remove(curPoint);
        Config.setK(controlPoints.size());
    }

    public void movePoint(int x, int y){
        controlPoints.get(curPoint).setX(x);
        controlPoints.get(curPoint).setY(y);
    }



    private void shiftTo(int dx, int dy){
        for(Point point: controlPoints){
            point.setX(point.getX()+dx);
            point.setY(point.getY()+dy);
        }
        for(Point point: curvePoints){
            point.setX(point.getX()+dx);
            point.setY(point.getY()+dy);
        }
    }


    public boolean isCurrentPoint(Point point){
        int index = controlPoints.indexOf(point);
        return index == curPoint;
    }

    public void makeCurve(){
        int K = controlPoints.size();
        int N = Config.getN();
        int i, j=0;
        curvePoints = new ArrayList<>();
        for(i = 0; i < K-3; i++) {
            for (j = 0; j < N; j++) {
                curvePoints.add(evaluatePoint(1.0 * j / N, i));
            }
        }
        curvePoints.add(evaluatePoint( 1.0*j/N, i-1));
    }

    private Point evaluatePoint(double t, int i){
        double[] coeffX = new double[4];
        double[] coeffY = new double[4];
        double temp;
        for(int k = 0; k < 4; k++){
            for(int l = 0; l < 4; l++){
                temp = 1.0/6 * matrixM[k* 4 +l];
                coeffX[k] += temp * getControlPoint(i+l).getX();
                coeffY[k] += temp * getControlPoint(i+l).getY();
            }
        }
        double resX = coeffX[0]*t*t*t + coeffX[1]*t*t + coeffX[2]*t + coeffX[3];
        double resY = coeffY[0]*t*t*t + coeffY[1]*t*t + coeffY[2]*t + coeffY[3];
        return new Point((int) resX , (int) resY );
    }

    private boolean isNewPoint(int x, int y){
        double dx, dy, dr;
        for(int i = 0; i< controlPoints.size(); i++){
            dx = 1.0* getControlPoint(i).getX()-x;
            dy = 1.0* getControlPoint(i).getY()-y;
            dr = 1.0* Constants.CIRCLE_RADIUS * Constants.CIRCLE_RADIUS;
            if(dx*dx + dy*dy <= dr){
                curPoint = i;
                return false;
            }
        }
        return true;
    }

//    private int getPosition(int x, int y){
//        int i;
//        if(pointsList.isEmpty() || x < getControlPoint(0).getX() - Constants.CIRCLE_RADIUS){
//            return 0;
//        }
//        for(i = 0; i < pointsList.size()-1; i++){
//            if(x > getControlPoint(i).getX()+Constants.CIRCLE_RADIUS &&
//                    x < getControlPoint(i+1).getX()-Constants.CIRCLE_RADIUS){
//                return i+1;
//            }
//        }
//        return i;
//    }
}
