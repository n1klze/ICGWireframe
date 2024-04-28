package ru.nsu.ccfit.melnikov.logic.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Vector3 {
    private double x;
    private double y;
    private double z;

    public Vector3 sub(Vector3 another){
        return new Vector3(
                x - another.x,
                y - another.y,
                z - another.z
        );
    }

    public void normalize(){
        double norm = Math.sqrt(x * x + y * y + z * z);
        x /= norm;
        y /= norm;
        z /= norm;
    }

    public Vector3 crossProd(Vector3 another) {
        return new Vector3(
                y * another.z - z * another.y,
                z * another.x - x * another.z,
                x * another.y - y * another.x
        );
    }

    public void mulScalar(double k){
        x *= k;
        y *= k;
        z *= k;
    }

    public double getLength(){
        return Math.sqrt(x*x + y*y + z*z);
    }
}
