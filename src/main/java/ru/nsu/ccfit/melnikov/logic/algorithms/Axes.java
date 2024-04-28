package ru.nsu.ccfit.melnikov.logic.algorithms;

import ru.nsu.ccfit.melnikov.logic.Config;
import ru.nsu.ccfit.melnikov.logic.utils.MatrixOp;
import ru.nsu.ccfit.melnikov.logic.utils.Vector3;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Axes {
    private final double ZN_INIT = 9;
    private final double TETA_X_INIT = 0;
    private final double TETA_Y_INIT = 90;
    private final double TETA_Z_INIT = 0;

    private final BufferedImage img;
    private final DrawingLineStrategy ds;
    private int width, height;
    private int M, num;
    private double tetaX, tetaY, tetaZ;
    private double zn;
    private final double[] camMatr;
    private double[][][] axesCoords;


    public Axes(BufferedImage image){
        img = image;
        width = image.getWidth();
        height = image.getHeight();
        zn = ZN_INIT;
        ds = new BresenhamLine(img);
        camMatr = getCamMatr();
    }

    public void drawModel(){
        double[] prev = null;
        double[] cur = new double[4];
        tetaY = Config.getThetaY();
        tetaX = Config.getThetaX();
        tetaZ = Config.getThetaZ();
        M = 3;

        num = 1 + 1;
        axesCoords = new double[M][num][4];
        int k;
        axesCoords[0][1][0] = 0.15;
        axesCoords[0][1][1] = 0;
        axesCoords[0][1][2] = 0;
        axesCoords[0][1][3] = 1;
        axesCoords[0][0][0] = 0;
        axesCoords[0][0][1] = 0;
        axesCoords[0][0][2] = 0;
        axesCoords[0][0][3] = 1;

        axesCoords[1][1][0] = 0;
        axesCoords[1][1][1] = -0.15;
        axesCoords[1][1][2] = 0;
        axesCoords[1][1][3] = 1;
        axesCoords[1][0][0] = 0;
        axesCoords[1][0][1] = 0;
        axesCoords[1][0][2] = 0;
        axesCoords[1][0][3] = 1;

        axesCoords[2][1][0] = 0;
        axesCoords[2][1][1] = 0;
        axesCoords[2][1][2] = 0.15;
        axesCoords[2][1][3] = 1;
        axesCoords[2][0][0] = 0;
        axesCoords[2][0][1] = 0;
        axesCoords[2][0][2] = 0;
        axesCoords[2][0][3] = 1;

        double[] resMatr = getTransformMatr();

        Color color;
        for(int j=0; j<M; j++) {
            prev = null;
            for (int i = 0; i < num; i++) {
                if (j == 0)
                    color = new Color(255,0,0);
                else if (j == 1)
                    color = new Color(0,0,255);
                else
                    color = new Color(0,255,0);
                cur = MatrixOp.matrixVectorMul(resMatr, axesCoords[j][i], 4);
                double z = axesCoords[j][i][2];
                if(cur[3]!=0) {
                    cur[0] = cur[0] / cur[3] * width;
                    cur[1] = cur[1] / cur[3] * height;
                    cur[2] /= cur[3];
                }
                if (prev != null) {
                    ds.drawLine(
                            (int) (prev[0] + width / 15), (int) (prev[1] + height / 15),
                            (int) (cur[0] + width / 15), (int) (cur[1] + height / 15),
                            color
                    );
                }
                prev = cur;
            }
        }
    }

    private double[] getCamMatr(){
        Vector3 eye = new Vector3(0, 0, -5);
        Vector3 look = new Vector3(0,0, 5);
        Vector3 up = new Vector3(0, 1, 0);

        Vector3 vz = eye.sub(look);
        vz.normalize();
        Vector3 vx = up.crossProd(vz);
        vx.normalize();
        Vector3 vy = vz.crossProd(vx);
        vy.normalize();

        double[] m1 = new double[]{
                1, 0, 0, -eye.getX(),
                0, 1, 0, -eye.getY(),
                0, 0, 1, -eye.getZ(),
                0, 0, 0, 1
        };
        double[] m2 = new double[]{
                vx.getX(), vx.getY(), vx.getZ(), 0,
                vy.getX(), vy.getY(), vy.getZ(), 0,
                vz.getX(), vz.getY(), vz.getZ(), 0,
                0, 0, 0, 1
        };
        return MatrixOp.matrixMul(m2, m1, 4);
    }

    private double[] getTransformMatr(){
        double[] res = new double[]{
                1,0,0,0,
                0,1,0,0,
                0,0,1,0,
                0,0,0,1
        };

        res = MatrixOp.matrixMul(MatrixOp.getRotateXMatrix(tetaX), res, 4);
        res = MatrixOp.matrixMul(MatrixOp.getRotateYMatrix(tetaY), res,4);
        res = MatrixOp.matrixMul(camMatr, res, 4);
        res = MatrixOp.matrixMul(MatrixOp.getViewPortMatr(width, height, zn), res, 4);
        res = MatrixOp.matrixMul(MatrixOp.getScaleMatrix(50, 50, 1, 1), res, 4);
        return res;
    }

    public void changeRotation(int dx, int dy){
        tetaX += 0.75*dy;
        tetaY += 0.75*dx;
        tetaZ -= 0.5*dx;
    }
    public void resetRotation(){
        tetaX = TETA_X_INIT;
        tetaZ = TETA_Z_INIT;
        tetaY = TETA_Y_INIT;
    }

}
