package ru.nsu.ccfit.melnikov.gui.frames;

import lombok.Setter;
import ru.nsu.ccfit.melnikov.controllers.EditController;
import ru.nsu.ccfit.melnikov.gui.EditPanel;
import ru.nsu.ccfit.melnikov.logic.Config;
import ru.nsu.ccfit.melnikov.logic.Constants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class EditorDialog extends JDialog implements ActionListener, ChangeListener {
    private JPanel paramsPanel;
    private JPanel graphicPanel;
    private JSpinner kSpinner;
    private JSpinner nSpinner;
    private JSpinner mSpinner;
    private JSpinner m1Spinner;
    private JButton okButton;
    private JButton cancelButton;

    private EditController controller;


    public EditorDialog(EditController controller){
        super(new Frame(), "B-spline configure", ModalityType.MODELESS);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //setResizable(false);

        this.controller = controller;
        initParamsPanel();
        initGraphicPanel();

        add(graphicPanel, BorderLayout.CENTER);
        add(paramsPanel, BorderLayout.SOUTH);
        setMinimumSize(new Dimension(640, 480));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initParamsPanel(){
        paramsPanel = new JPanel();
        paramsPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        paramsPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(15,10,15,10);
        constraints.weighty = 1.0;
        constraints.weightx = 0.5;

        kSpinner = new JSpinner(new SpinnerNumberModel(Config.getK(), Constants.MIN_K, Constants.MAX_K, 1));
        nSpinner = new JSpinner(new SpinnerNumberModel(Config.getN(), Constants.MIN_N, Constants.MAX_N, 1));
        mSpinner = new JSpinner(new SpinnerNumberModel(Config.getM(), Constants.MIN_M, Constants.MAX_M, 1));
        m1Spinner = new JSpinner(new SpinnerNumberModel(Config.getM1(), Constants.MIN_M1, Constants.MAX_M1, 1));
        kSpinner.addChangeListener(this);
        nSpinner.addChangeListener(this);
        mSpinner.addChangeListener(this);
        m1Spinner.addChangeListener(this);

        JLabel kLabel = new JLabel("K");
        JLabel nLabel = new JLabel("N");
        JLabel mLabel = new JLabel("M");
        JLabel m1Label = new JLabel("M1");

        ButtonGroup toggleBtnGroup = new ButtonGroup();
        okButton = createButton("Apply", toggleBtnGroup, this);
        cancelButton = createButton("Cancel", toggleBtnGroup, this);

        constraints.weightx = 0.1;
        constraints.gridy = 0;
        constraints.gridx = 0;
        paramsPanel.add(kLabel, constraints);
        constraints.gridx++;
        paramsPanel.add(kSpinner, constraints);

        constraints.weightx = 0.5;
        constraints.gridx++;
        paramsPanel.add(nLabel, constraints);
        constraints.weightx = 0.1;
        constraints.gridx++;
        paramsPanel.add(nSpinner, constraints);

        constraints.gridy++;
        constraints.gridx=0;
        paramsPanel.add(mLabel, constraints);
        constraints.gridx++;
        paramsPanel.add(mSpinner, constraints);

        constraints.weightx=0.5;
        constraints.gridx++;
        paramsPanel.add(m1Label, constraints);
        constraints.weightx=0.1;
        constraints.gridx++;
        paramsPanel.add(m1Spinner, constraints);

        constraints.weightx = 0.5;
        constraints.gridy++;
        constraints.gridx=0;
        paramsPanel.add(okButton, constraints);
        constraints.gridx++;
        paramsPanel.add(cancelButton, constraints);
    }

    public void setElements(){
        kSpinner.setValue(Config.getK());
        nSpinner.setValue(Config.getN());
        mSpinner.setValue(Config.getM());
        m1Spinner.setValue(Config.getM1());
    }

    private void initGraphicPanel(){
        graphicPanel = new EditPanel();
        ((EditPanel)graphicPanel).setEditorDialog(this);
    }

    private JButton createButton(String text, ButtonGroup group, ActionListener l){
        JButton button = new JButton(text);
        button.addActionListener(l);
        group.add(button);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == okButton){
            ((EditPanel)graphicPanel).saveSpline();
            controller.onDrawAction();
        }
        if(e.getSource() == cancelButton){
            this.setVisible(false);
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() == kSpinner){
            Config.setK((Integer) kSpinner.getValue());
            ((EditPanel)graphicPanel).update();
        }
        if(e.getSource() == nSpinner){
            Config.setN((Integer) nSpinner.getValue());
            ((EditPanel)graphicPanel).update();
        }
        if(e.getSource() == mSpinner){
            Config.setM((Integer) mSpinner.getValue());
        }
        if(e.getSource() == m1Spinner){
            Config.setM1((Integer) m1Spinner.getValue());
        }
    }
}
