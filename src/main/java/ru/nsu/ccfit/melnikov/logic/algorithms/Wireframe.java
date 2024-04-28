package ru.nsu.ccfit.melnikov.logic.algorithms;

import lombok.Getter;
import ru.nsu.ccfit.melnikov.logic.Config;
import ru.nsu.ccfit.melnikov.logic.utils.MatrixOp;
import ru.nsu.ccfit.melnikov.logic.utils.Vector3;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wireframe {
    private final double ZN_INIT = 9;
    private final double THETA_X_INIT = 0;
    private final double THETA_Y_INIT = 90;
    private final double THETA_Z_INIT = 0;


    private final double zoomCoeff = 0.5;
    private final BufferedImage img;
    private final DrawingLineStrategy ds;
    private BSpline spline;
    private int width, height;
    private int K, N, M, M1, num;
    private double thetaX, thetaY, thetaZ;
    private double minX, minY, minZ, maxX, maxY, maxZ;
    private double meanX, meanY, meanZ;
    private double scale;
    private double zn;
    private final double[] camMatr;
    @Getter
    private double[][][] circleCoords;
    @Getter
    private double[][][] curveCoords;



    public Wireframe(BufferedImage image){
        img = image;
        width = image.getWidth();
        height = image.getHeight();
        ds = new BresenhamLine(img);
        zn = ZN_INIT;
        thetaY = THETA_Y_INIT;
        thetaX = THETA_X_INIT;
        thetaZ = THETA_Z_INIT;
        camMatr = getCamMatr();
        minX = minY = minZ = width;
        maxZ = maxX = maxY = -width;
        meanX = meanY = meanZ = 0;
    }

    public void drawModel(){
        double[] prev = null;
        double[] cur = new double[4];
        double[] cur1 = new double[4];
        double minZ = 10000000;
        double maxZ = -10000000;

        spline = Config.getBSpline();
        K = Config.getK();
        M = Config.getM();
        N = Config.getN();
        M1 = Config.getM1();
        zn = Config.getZn();
        thetaY = Config.getThetaY();
        thetaX = Config.getThetaX();
        thetaZ = Config.getThetaZ();
        num = N * (K - 3) + 1;
        curveCoords = new double[M][num][4];
        circleCoords = new double[K-2][M1*M][4];
        int k;


        for (int i = 0; i < num; i++) {
            for (int j = 0; j < M; j++) {
                curveCoords[j][i] = evaluateCurvePoint(j, i, M);
                /*if(curveCoords[j][i][2] < minZ){minZ = curveCoords[j][i][2];}
                if(curveCoords[j][i][2] > maxZ){maxZ = curveCoords[j][i][2];}*/

            }
        }
        for (int i = 0; i < num; i+=N) {
            k = i/N;
            for (int j = 0; j < M1*M; j++) {
                circleCoords[k][j] = evaluateCurvePoint(j, i, M1*M);
            }
        }
        double[] resMatr = getTransformMatr();

        for(int j=0; j<M; j++) {
            for (int i = 0; i < num; i++) {
                cur1 = MatrixOp.matrixVectorMul(resMatr, curveCoords[j][i], 4);
                if(cur1[3]!=0) {
                    cur1[0] = cur1[0] / cur1[3] * width;
                    cur1[1] = cur1[1] / cur1[3] * height;
                    cur1[2] /= cur1[3];
                }
                if(cur1[2] > maxZ){maxZ = cur1[2];}
                if(cur1[2] < minZ){minZ = cur1[2];}
            }
        }

        for(int j=0; j<K-2; j++) {
            for (int i = 0; i < M1 * M; i++) {
                cur1 = MatrixOp.matrixVectorMul(resMatr, circleCoords[j][i], 4);

                if (cur1[3] != 0) {
                    cur1[0] = cur1[0] / cur1[3] * width;
                    cur1[1] = cur1[1] / cur1[3] * height;
                    cur1[2] /= cur1[3];
                }
                if(cur1[2] > maxZ){maxZ = cur1[2];}
                if(cur1[2] < minZ){minZ = cur1[2];}
            }
        }

        for(int j=0; j<M; j++) {
            prev = null;
            for (int i = 0; i < num; i++) {
                cur = MatrixOp.matrixVectorMul(resMatr, curveCoords[j][i], 4);
                if(cur[3]!=0) {
                    cur[0] = cur[0] / cur[3] * width;
                    cur[1] = cur[1] / cur[3] * height;
                    cur[2] /= cur[3];
                }
                if (prev != null) {
                    double z = (cur[2] + prev[2]) / 2;
                    double max = maxZ - minZ;
                    double min = minZ - minZ;
                    z = (z - minZ) / max;
                    int edgeDistance = (int)(z * 255) - 30;
                    edgeDistance = Math.max(edgeDistance, 0);
                    int edgeRGB = 0xFF000000 | (255 - edgeDistance << 16) & 0x00FF0000
                            | (edgeDistance << 8) & 0x00000000 | edgeDistance & 0x000000FF;
                    Color color = new Color(edgeRGB);
                    ds.drawLine(
                            (int) (prev[0] + width / 2), (int) (prev[1] + height / 2),
                            (int) (cur[0] + width / 2), (int) (cur[1] + height / 2),
                            color
                    );
                }
                prev = cur;
            }
        }
        double[] first = null;
        for(int j=0; j<K-2; j++) {
            prev = null;
            for (int i = 0; i < M1 * M; i++) {
                cur = MatrixOp.matrixVectorMul(resMatr, circleCoords[j][i], 4);

                if(cur[3] != 0) {
                    cur[0] = cur[0] / cur[3] * width;
                    cur[1] = cur[1] / cur[3] * height;
                    cur[2] /= cur[3];
                }
                if (prev != null) {
                    double z = (cur[2] + prev[2]) / 2;
                    double max = maxZ - minZ;
                    double min = minZ - minZ;
                    z = (z - minZ) / max;
                    int edgeDistance = (int)(z * 255) - 30;
                    edgeDistance = Math.max(edgeDistance, 0);
                    int edgeRGB = 0xFF000000 | (255 - edgeDistance << 16) & 0x00FF0000
                            | (edgeDistance << 8) & 0x00000000 | edgeDistance & 0x000000FF;
                    Color color = new Color(edgeRGB);
                    ds.drawLine(
                            (int) (prev[0] + width / 2), (int) (prev[1] + height / 2),
                            (int) (cur[0] + width / 2), (int) (cur[1] + height / 2),
                            color
                    );
                } else {
                    first = cur;
                }
                prev = cur;
            }
            double z = (cur[2] + first[2]) / 2;
            double max = maxZ - minZ;
            double min = minZ - minZ;
            z = (z - minZ) / max;
            int edgeDistance = (int)(z * 255) - 30;
            edgeDistance = Math.max(edgeDistance, 0);
            int edgeRGB = 0xFF000000 | (255 - edgeDistance << 16) & 0x00FF0000
                    | (edgeDistance << 8) & 0x00000000 | edgeDistance & 0x000000FF;
            Color color = new Color(edgeRGB);
            ds.drawLine(
                    (int) (cur[0] + width / 2), (int) (cur[1] + height / 2),
                    (int) (first[0] + width / 2), (int) (first[1] + height / 2),
                    color
            );
        }
    }

    private double[] getCamMatr(){
        Vector3 eye = new Vector3(0, 0, -10);
        Vector3 look = new Vector3(0,0, 10);
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
        int cnt = M*num + (K-2)*M1*M;
        meanX /= cnt;
        meanY /= cnt;
        meanZ /= cnt;

        res = MatrixOp.matrixMul(MatrixOp.getShiftMatrix(-meanX, -meanY, -meanZ), res,4);
        res = MatrixOp.matrixMul(
                MatrixOp.getScaleMatrix(1.0/scale, 1.0/scale, 1.0/scale, 1.0),
                res,
                4
        );
//        res = MatrixOp.matrixMul(MatrixOp.getOrthographicMatr(1, -1, -1, 1, -1, 1), res, 4);
        res = MatrixOp.matrixMul(MatrixOp.getRotateXMatrix(thetaX), res, 4);
        res = MatrixOp.matrixMul(MatrixOp.getRotateYMatrix(thetaY), res,4);
        res = MatrixOp.matrixMul(camMatr, res, 4);
        res = MatrixOp.matrixMul(MatrixOp.getViewPortMatr(width, height, zn), res, 4);
        res = MatrixOp.matrixMul(MatrixOp.getScaleMatrix(50, 50, 1, 1), res, 4);
        return res;
    }

    private double[] evaluateCurvePoint(int j, int i, int cnt){
        double[] res = new double[4];
        double fi = j * 2.0 * Math.PI / cnt;
        res[0] = spline.getCurvePoint(i).getY() * Math.cos(fi);
        res[2] = spline.getCurvePoint(i).getY() * Math.sin(fi);
        res[1] = spline.getCurvePoint(i).getX();
        res[3] = 1;

        scale = Math.max(scale, Math.abs(res[0]));
        scale = Math.max(scale, Math.abs(res[1]));
        scale = Math.max(scale, Math.abs(res[2]));
        scale = Math.max(scale, Math.abs(res[3]));

        meanX += res[0];
        meanY += res[1];
        meanZ += res[2];

        return res;
    }

    public void changeZoom(int zoom){
        zn -= zoom * zoomCoeff;
        Config.setZn(zn);
    }
    public void changeRotation(int dx, int dy){
        thetaX += 0.75*dy;
        thetaY += 0.75*dx;
        thetaZ -= 0.5*dx;
        Config.setThetaX(thetaX);
        Config.setThetaY(thetaY);
        Config.setThetaZ(thetaZ);
    }
    public void resetRotation(){
        thetaX = THETA_X_INIT;
        thetaZ = THETA_Z_INIT;
        thetaY = THETA_Y_INIT;
        Config.setThetaX(thetaX);
        Config.setThetaY(thetaY);
        Config.setThetaZ(thetaZ);
    }
    public void resetZoom(){
        zn = ZN_INIT;
        Config.setZn(zn);
    }
}
