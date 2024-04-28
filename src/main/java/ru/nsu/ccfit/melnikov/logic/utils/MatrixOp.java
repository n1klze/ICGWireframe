package ru.nsu.ccfit.melnikov.logic.utils;

public class MatrixOp {

    public static double[] matrixMul(double[] A, double[] B, int size){
        double[] res = new double[size * size];
        for(int i = 0; i< size; i++){
            for(int j = 0; j<size; j++){
                for(int k = 0; k< size; k++){
                    res[i*size+j] += A[i*size + k] * B[k*size + j];
                }
            }
        }
        return res;
    }

    public static double[] matrixVectorMul(double[] A, double[] vec, int aRows){
        double[] res = new double[aRows];
        for(int i=0; i < aRows; i++){
            for(int j = 0; j < aRows; j++){
                res[i] += A[i*aRows+j]*vec[j];
            }
        }
        return res;
    }

    public static double[] getViewPortMatr(int Sw, int Sh, double zn) {
        double sx = 2*zn/Sw;
        double sy =  2*zn/Sh;
        return new double[]{
                sx, 0, 0, 0,
                0, sy, 0, 0,
                0, 0, -1, -2*zn,
                0, 0, -1, 0
        };
    }

    public static double[] getProjMatr(double zn) {
        return new double[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 1.0/zn, 0
        };
    }



    public static double[] getOrthographicMatr(double r, double l, double n, double t, double b, double f){
        double dx = -(r+l)/(r-l);
        double dy = -(t+b)/(t-b);
        double dz = -(f+n)/(f-n);
        return new double[]{
                2.0/(r-l), 0, 0, 0,
                0, 2.0/(t-b), 0, 0,
                0, 0, 1.0/(f-n), -n/(f-n),
                0, 0, 0, 1
        };
    }


    public static double[] getScaleMatrix(double kx, double ky, double kz, double kw){
        return new double[]{
                kx, 0, 0, 0,
                0, ky, 0, 0,
                0, 0, kz, 0,
                0, 0, 0, kw
        };
    }

    public static double[] getShiftMatrix(double dx, double dy, double dz) {
        return new double[]{
                1, 0, 0, dx,
                0, 1, 0, dy,
                0, 0, 1, dz,
                0, 0, 0, 1
        };
    }
    public static double[] getRotateXMatrix(double teta) {
        double rad = Math.PI  * teta / 180;
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new double[]{
                1, 0, 0, 0,
                0, cos, -sin, 0,
                0, sin, cos, 0,
                0, 0, 0, 1
        };
    }
    public static double[] getRotateYMatrix(double teta) {
        double rad = Math.PI * teta / 180;
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new double[]{
                cos, 0, sin, 0,
                0, 1, 0, 0,
                -sin, 0, cos, 0,
                0, 0, 0, 1
        };
    }
    public static double[] getRotateZMatrix(double teta) {
        double rad = Math.PI / 180 * teta;
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new double[]{
                cos, -sin, 0, 0,
                sin, cos, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };
    }
}
