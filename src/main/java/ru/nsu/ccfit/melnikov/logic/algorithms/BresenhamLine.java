package ru.nsu.ccfit.melnikov.logic.algorithms;

import lombok.Setter;
import ru.nsu.ccfit.melnikov.logic.utils.Point;
import ru.nsu.ccfit.melnikov.logic.utils.Polygon;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BresenhamLine implements DrawingLineStrategy{
    @Setter
    private BufferedImage image;

    public BresenhamLine(BufferedImage image){
        this.image = image;
    }

    @Override
    public void drawLine(Point first, Point second, Color color) {
        drawLine(first.getX(), first.getY(), second.getX(), second.getY(), color);
    }

    @Override
    public void drawLine(int startX, int startY, int endX, int endY, Color color) {
        int err;
        int signX = (endX - startX >= 0) ? 1 : -1;
        int signY = (endY - startY >= 0) ? 1 : -1;
        int absDX = Math.abs(endX - startX);
        int absDY = Math.abs(endY - startY);
        int end = Math.max(absDX, absDY);
        int rgb = color.getRGB();
        if (absDX == 0 && absDY == 0) {
            if(startX >= 0 && startX < image.getWidth() && startY >= 0 && startY < image.getHeight()) {
                image.setRGB(startX, startY, rgb);
            }
        } else if (absDY <= absDX) {
            err = -absDX;
            for (int i = 0; i < end; i++) {
                startX += signX;
                err += 2 * absDY;
                if (err > 0) {
                    err -= 2 * absDX;
                    startY += signY;
                }
                if(startX >= 0 && startX < image.getWidth() && startY >= 0 && startY < image.getHeight()) {
                    image.setRGB(startX, startY, rgb);
                }
            }
        } else {
            err = -absDY;
            for (int i = 0; i < end; i++) {
                startY += signY;
                err += 2 * absDX;
                if (err > 0) {
                    err -= 2 * absDY;
                    startX += signX;
                }
                if(startX >= 0 && startX < image.getWidth() && startY >= 0 && startY < image.getHeight()) {
                    image.setRGB(startX, startY, rgb);
                }
            }
        }
    }

    @Override
    public void drawRectangle(Polygon polygon) {
        int peaks = polygon.getPeaks();
        int radius = polygon.getRadius();
        double rotation = polygon.getRotation();
        int x = polygon.getX();
        int y = polygon.getY();

        double angle = rotation;
        int prevX = (int) (radius * Math.cos(angle) + x);
        int prevY = (int) (radius * Math.sin(angle) + y);
        int startX = prevX, startY = prevY;
        int nextX, nextY;

        Point first = new Point();
        Point second = new Point();

        for (int i = 1; i < peaks; i++) {
            angle = 2 * Math.PI * i / peaks + rotation;
            nextX = (int) (radius * Math.cos(angle) + x);
            nextY = (int) (radius * Math.sin(angle) + y);

            first.setX(prevX);
            first.setY(prevY);
            second.setX(nextX);
            second.setY(nextY);
            drawLine(first, second, polygon.getColor());

            prevX = nextX;
            prevY = nextY;
        }
        first.setX(prevX);
        first.setY(prevY);
        second.setX(startX);
        second.setY(startY);
        drawLine(first, second, polygon.getColor());
    }

    private int checkBounds(int val, int max){
        if(val >= max){
            return max;
        } else if(val < 0){
            return 0;
        }
        return val;
    }
}
