package ru.nsu.ccfit.melnikov.gui.components;

import lombok.Setter;
import ru.nsu.ccfit.melnikov.controllers.EditController;
import ru.nsu.ccfit.melnikov.controllers.FileController;
import ru.nsu.ccfit.melnikov.gui.controllable.buttons.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBar extends JToolBar implements ActionListener {
    private JButton openBtn;
    private JButton saveBtn;
    private JButton infoBtn;
    private JButton settingsBtn;
    private JButton refreshBtn;

    @Setter
    private EditController editController;
    @Setter
    private FileController fileController;


    public ToolBar(){
        super();
        openBtn = new OpenFileButton(this);
        saveBtn = new SaveButton(this);
        infoBtn = new InfoButton(this);
        settingsBtn = new SettingsButton(this);
        refreshBtn = new RefreshButton(this);

        ButtonGroup group = new ButtonGroup();
        group.add(openBtn);
        group.add(saveBtn);
        group.add(infoBtn);
        group.add(settingsBtn);
        group.add(refreshBtn);

        add(openBtn);
        add(saveBtn);
        add(infoBtn);
        add(settingsBtn);
        add(refreshBtn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == openBtn){
            fileController.openFile();
        }
        if(e.getSource() == saveBtn){
            fileController.saveFile();
        }
        if(e.getSource() == infoBtn){
            fileController.openInfoDialog();
        }
        if(e.getSource() == settingsBtn){
            editController.onSettingsAction();
        }
        if(e.getSource() == refreshBtn){
            editController.onRefreshAction();
        }

    }
}
