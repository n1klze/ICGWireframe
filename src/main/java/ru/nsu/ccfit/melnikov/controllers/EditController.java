package ru.nsu.ccfit.melnikov.controllers;

import ru.nsu.ccfit.melnikov.gui.MainPanel;
import ru.nsu.ccfit.melnikov.gui.frames.EditorDialog;

import javax.swing.*;

public class EditController {
    private JFrame frame;
    private JPanel mainPanel;

    public EditController(JFrame frame, JPanel panel){
        this.frame = frame;
        this.mainPanel = panel;
    }


    public void onSettingsAction(){
        new EditorDialog(this);
    }

    public void onRefreshAction(){
        ((MainPanel) mainPanel).resetParams();
    }

    public void onDrawAction(){
        ((MainPanel)mainPanel).update();
    }
}
