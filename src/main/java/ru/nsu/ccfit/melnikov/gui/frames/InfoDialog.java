package ru.nsu.ccfit.melnikov.gui.frames;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class InfoDialog extends JDialog implements ActionListener {
    private final JButton okButton;
    private static final String HELP_TEXT = """
            --------------------------------------ICGWireframe--------------------------------------------
            Author: Melnikov Nikita
            """;

    public InfoDialog(){
        this(HELP_TEXT);
    }

    public InfoDialog(String msg) {
        super(new Frame(), "Information", Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JTextArea textArea = new JTextArea();
        textArea.setText(msg);
        textArea.setEditable(false);
        okButton = new JButton("OK");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(this);

        JPanel panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);
        panel.setBorder(new EmptyBorder(new Insets(10, 15, 10, 15)));
        panel.add(textArea);
        panel.add(okButton);

        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == okButton){
            setVisible(false);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }
}
