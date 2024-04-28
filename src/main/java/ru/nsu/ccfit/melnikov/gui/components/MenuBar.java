package ru.nsu.ccfit.melnikov.gui.components;

import lombok.Setter;
import ru.nsu.ccfit.melnikov.controllers.EditController;
import ru.nsu.ccfit.melnikov.controllers.FileController;
import ru.nsu.ccfit.melnikov.gui.controllable.menuitem.MenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBar extends JMenuBar implements ActionListener{
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu helpMenu;
    private JMenuItem openFileItem;
    private JMenuItem saveFileItem;
    private JMenuItem helpItem;
    private JMenuItem settingsItem;
    private JMenuItem refreshItem;
    @Setter
    private EditController editController;
    @Setter
    private FileController fileController;

    public MenuBar(){
        super();
        initFileMenu(this);
        initHelpMenu(this);
        initEditMenu(this);

        add(fileMenu);
        add(editMenu);
        add(helpMenu);
    }

    private void initFileMenu(ActionListener l){
        fileMenu = new JMenu("File");
        openFileItem = new MenuItem("Open", l);
        saveFileItem = new MenuItem("Save", l);
        fileMenu.add(openFileItem);
        fileMenu.add(saveFileItem);
    }

    private void initEditMenu(ActionListener l){
        editMenu = new JMenu("Modify");
        settingsItem = new MenuItem("Settings", l);
        refreshItem = new MenuItem("Refresh", l);
        editMenu.add(settingsItem);
        editMenu.add(refreshItem);
    }

    private void initHelpMenu(ActionListener l){
        helpMenu = new JMenu("Help");
        helpItem = new MenuItem("About", l);
        helpMenu.add(helpItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == openFileItem){
            fileController.openFile();
        }
        if(e.getSource() == saveFileItem) {
            fileController.saveFile();
        }
        if(e.getSource() == settingsItem){
            editController.onSettingsAction();
        }
        if(e.getSource() == refreshItem){
            editController.onRefreshAction();
        }
        if(e.getSource() == helpItem){
            fileController.openInfoDialog();
        }
    }
}
