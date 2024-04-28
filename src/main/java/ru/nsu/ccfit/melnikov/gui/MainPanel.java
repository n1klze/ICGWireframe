package ru.nsu.ccfit.melnikov.gui;

import ru.nsu.ccfit.melnikov.logic.Config;
import ru.nsu.ccfit.melnikov.logic.algorithms.Axes;
import ru.nsu.ccfit.melnikov.logic.algorithms.BresenhamLine;
import ru.nsu.ccfit.melnikov.logic.algorithms.DrawingLineStrategy;
import ru.nsu.ccfit.melnikov.logic.algorithms.Wireframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MainPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener {
    private BufferedImage image;
    private Graphics2D graphics2D;
    private DrawingLineStrategy ds;
    private Wireframe wireframe;
    private Axes axes;
    private int lastX, lastY;
    private int width, height;
    private final Color BG_COLOR = Color.WHITE;
    private final Color LINE_COLOR = Color.BLACK;

    public MainPanel(){
        super();
        setPreferredSize(new Dimension(640, 480));
        addMouseListener(this);

        width = 640;
        height = 480;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics2D = image.createGraphics();
        graphics2D.setBackground(BG_COLOR);
        graphics2D.clearRect(0, 0, width, height);
        ds = new BresenhamLine(image);
        wireframe = new Wireframe(image);
        axes = new Axes(image);

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addComponentListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    public void updateOld(){
        graphics2D.setBackground(BG_COLOR);
        graphics2D.clearRect(0,0, image.getWidth(), image.getHeight());
        int M = Config.getM();
        int M1 = Config.getM1();
        int N = Config.getN();
        int K = Config.getK();
        int num = (N * (K - 3) + 1);
        int dx = width/2;
        int dy = height/2;
        int i, j;
        double[][][] coords = wireframe.getCurveCoords();
        graphics2D.setColor(LINE_COLOR);
        for(i=0; i< M; i++){
            for(j=0; j<num-1; j++){
                ds.drawLine(
                        (int) (coords[i][j][0]+ dx), (int) (coords[i][j][1] + dy),
                        (int) (coords[i][j+1][0]+dx), (int) (coords[i][j+1][1] + dy),
                        LINE_COLOR
                );
            }
        }
        coords = wireframe.getCircleCoords();
        for(i=0; i < K-2; i++){
            for( j=0; j<M1*M-1; j++){
                ds.drawLine(
                        (int) (coords[i][j][0] + dx), (int) (coords[i][j][1] + dy),
                        (int) (coords[i][j+1][0] + dx), (int) (coords[i][j+1][1] + dy),
                        LINE_COLOR
                );
            }
            ds.drawLine(
                    (int) (coords[i][j][0] + dx), (int) (coords[i][j][1] + dy),
                    (int) (coords[i][0][0] + dx), (int) (coords[i][0][1] + dy),
                    LINE_COLOR
            );
        }
        repaint();
    }

    public void update(){
        graphics2D.setBackground(BG_COLOR);
        graphics2D.clearRect(0,0, image.getWidth(), image.getHeight());
        wireframe.drawModel();
        axes.drawModel();
        repaint();
    }

    public void resetParams(){
        wireframe.resetRotation();
        axes.resetRotation();
        wireframe.resetZoom();
        update();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(image == null){
            return;
        }
        int dx = (e.getX() - lastX);
        int dy = (e.getY() - lastY);
        lastX = e.getX();
        lastY = e.getY();
        wireframe.changeRotation(dx, dy);
        axes.changeRotation(dx, dy);
        update();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (image == null) {
            return;
        }
        wireframe.changeZoom(e.getWheelRotation());
        update();
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void componentResized(ComponentEvent e) {
        setPreferredSize(getSize());
        addMouseListener(this);

        width = getSize().width;
        height = getSize().height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics2D = image.createGraphics();
        graphics2D.setBackground(BG_COLOR);
        graphics2D.clearRect(0, 0, width, height);
        ds = new BresenhamLine(image);
        wireframe = new Wireframe(image);
        axes = new Axes(image);
        update();
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}
}