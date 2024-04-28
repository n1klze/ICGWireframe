package ru.nsu.ccfit.melnikov.logic.algorithms;

import ru.nsu.ccfit.melnikov.logic.utils.Polygon;
import ru.nsu.ccfit.melnikov.logic.utils.Point;

import java.awt.*;
public interface DrawingLineStrategy {
    void drawLine(Point first, Point second, Color color);
    void drawLine(int startX, int startY, int endX, int endY, Color color);
    void drawRectangle(Polygon polygon);
}
