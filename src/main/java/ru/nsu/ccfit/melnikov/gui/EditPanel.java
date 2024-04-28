package ru.nsu.ccfit.melnikov.gui;

import lombok.Setter;
import ru.nsu.ccfit.melnikov.gui.frames.EditorDialog;
import ru.nsu.ccfit.melnikov.logic.*;
import ru.nsu.ccfit.melnikov.logic.algorithms.BSpline;
import ru.nsu.ccfit.melnikov.logic.utils.Point;
import ru.nsu.ccfit.melnikov.logic.utils.Polygon;
import ru.nsu.ccfit.melnikov.logic.algorithms.BresenhamLine;
import ru.nsu.ccfit.melnikov.logic.algorithms.DrawingLineStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class EditPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener {
    private BufferedImage image;
    private Graphics2D graphics2D;
    private DrawingLineStrategy ds;
    private BSpline bSpline;
    @Setter
    private JDialog editorDialog;
    private int width;
    private int height;

    public EditPanel(){
        super();
        setPreferredSize(new Dimension(640, 480));
        addMouseListener(this);

        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //width = screenSize.width;
        //height = screenSize.height;
        width = 640;
        height = 480;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics2D = image.createGraphics();

        ds = new BresenhamLine(image);
        bSpline = Config.getBSpline();
        bSpline.setCenter(new Point(image.getWidth()/2, image.getHeight()/2));
        drawAxes();
        drawSpline();
        drawCurve();

        addMouseListener(this);
        addMouseMotionListener(this);
        addComponentListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    public void update(){
        if(Config.getK() > bSpline.getControlPointsNum()) {
            Point point = bSpline.getLastControlPoint();
            bSpline.addPoint(point.getX(), point.getY()+50);
        }
        else if(Config.getK() < bSpline.getControlPointsNum()) {
            bSpline.removePoint();
        }
        bSpline.makeCurve();
        drawAxes();
        drawSpline();
        drawCurve();
        repaint();
    }

    public void saveSpline(){
        Config.setBSpline(bSpline);
    }


    private void drawAxes() {
        Color axesColor = Color.LIGHT_GRAY;
        graphics2D.setBackground(Color.BLACK);
        graphics2D.clearRect(0, 0, image.getWidth(), image.getHeight());
        Point first = new Point(image.getWidth() / 2, 0);
        Point second = new Point(image.getWidth() / 2, image.getHeight() - 1);
        ds.drawLine(first, second, axesColor);

        first.setX(0);
        first.setY(image.getHeight() / 2);
        second.setX(image.getWidth() - 1);
        second.setY(image.getHeight() / 2);
        ds.drawLine(first, second, axesColor);

        int width = 8;

        first.setX(image.getWidth() / 2);
        first.setY((image.getHeight() - width) / 2);
        second.setX(image.getWidth() / 2);
        second.setY((image.getHeight() + width) / 2);
        while (first.getX() >= 0 && second.getX() >= 0) {
            first.moveX(-Constants.PIXELS_IN_CM);
            second.moveX(-Constants.PIXELS_IN_CM);
            ds.drawLine(first, second, axesColor);
        }

        first.setX(image.getWidth() / 2);
        second.setX(image.getWidth() / 2);
        while (first.getX() < image.getWidth() && second.getX() < image.getWidth()) {
            first.moveX(Constants.PIXELS_IN_CM);
            second.moveX(Constants.PIXELS_IN_CM);
            ds.drawLine(first, second, axesColor);
        }

        first.setX((image.getWidth() - width) / 2);
        first.setY(image.getHeight() / 2);
        second.setX((image.getWidth() + width) / 2);
        second.setY(image.getHeight() / 2);
        while (first.getY() >= 0 && second.getY() >= 0) {
            first.moveY(-Constants.PIXELS_IN_CM);
            second.moveY(-Constants.PIXELS_IN_CM);
            ds.drawLine(first, second, axesColor);
        }

        first.setY(image.getHeight() / 2);
        second.setY(image.getHeight() / 2);
        while (first.getY() < image.getHeight() && second.getY() < image.getHeight()) {
            first.moveY(Constants.PIXELS_IN_CM);
            second.moveY(Constants.PIXELS_IN_CM);
            ds.drawLine(first, second, axesColor);
        }
    }

    private void drawSpline(){
        Polygon circle = new Polygon(new Point(), Constants.CIRCLE_RADIUS, Constants.CIRCLE_PEAKS, Color.RED, Constants.CIRCLE_ROTATION);
        Point point;
        int size = bSpline.getControlPoints().size();
        int startX, startY, endX, endY;
        for(int i=0; i<size; i++) {
            point = bSpline.getControlPoint(i);
            startX = point.getX()+width/2;
            startY = point.getY()+height/2;
            if (bSpline.isCurrentPoint(point)) {
                circle.setColor(Color.GREEN);
            } else {
                circle.setColor(Color.RED);
            }
            circle.setCenter(startX, startY);
            ds.drawRectangle(circle);

            if(i != size-1){
                endX = bSpline.getControlPoint(i+1).getX()+width/2;
                endY = bSpline.getControlPoint(i+1).getY()+height/2;
                ds.drawLine(startX, startY, endX, endY, Color.RED);
            }
        }
    }

    private void drawCurve() {
        int startX, startY, endX, endY;
        for (int i = 0; i < bSpline.getCurvePoints().size() - 1; i++) {
            startX = bSpline.getCurvePoint(i).getX()+width/2;
            startY = bSpline.getCurvePoint(i).getY()+height/2;
            endX = bSpline.getCurvePoint(i+1).getX()+width/2;
            endY = bSpline.getCurvePoint(i+1).getY()+height/2;
            ds.drawLine(startX, startY, endX, endY, Color.CYAN);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            bSpline.addPoint(e.getX() - width / 2, e.getY() - height / 2);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            bSpline.delPoint(e.getX() - width / 2, e.getY() - height / 2);
        }
        ((EditorDialog)editorDialog).setElements();
        bSpline.makeCurve();
        drawAxes();
        drawSpline();
        drawCurve();
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            bSpline.addPoint(e.getX() - width / 2, e.getY() - height / 2);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            bSpline.delPoint(e.getX() - width / 2, e.getY() - height / 2);
        }
        drawAxes();
        drawSpline();
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        ((EditorDialog)editorDialog).setElements();
        bSpline.makeCurve();
        drawCurve();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        bSpline.movePoint(e.getX()-width/2, e.getY()-height/2);
        drawAxes();
        drawSpline();
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void componentResized(ComponentEvent e) {
        setPreferredSize(getSize());
        width = getSize().width;
        height = getSize().height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics2D = image.createGraphics();

        ds = new BresenhamLine(image);
        bSpline.setCenter(new Point(image.getWidth()/2, image.getHeight()/2));
        drawAxes();
        drawSpline();
        drawCurve();
        repaint();
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
