package ru.nsu.ccfit.melnikov.gui.frames;

import ru.nsu.ccfit.melnikov.controllers.EditController;
import ru.nsu.ccfit.melnikov.controllers.FileController;
import ru.nsu.ccfit.melnikov.gui.MainPanel;
import ru.nsu.ccfit.melnikov.gui.components.ToolBar;
import ru.nsu.ccfit.melnikov.gui.components.MenuBar;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    private JPanel mainPanel;
    private JToolBar toolBar;
    private JMenuBar menuBar;
    private EditController editController;
    private FileController fileController;

    public MyFrame(){
        super("Editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        toolBar = new ToolBar();
        add(toolBar, BorderLayout.NORTH);
        menuBar = new MenuBar();
        setJMenuBar(menuBar);
        mainPanel = new MainPanel();
        initControllers();
        add(mainPanel);

        setMinimumSize(new Dimension(640, 480));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initControllers(){
        fileController = new FileController(this, mainPanel);
        editController = new EditController(this, mainPanel);

        ((ToolBar) toolBar).setEditController(editController);
        ((ToolBar) toolBar).setFileController(fileController);

        ((MenuBar) menuBar).setEditController(editController);
        ((MenuBar) menuBar).setFileController(fileController);
    }
}
