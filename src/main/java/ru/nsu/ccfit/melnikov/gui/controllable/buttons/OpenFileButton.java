package ru.nsu.ccfit.melnikov.gui.controllable.buttons;

import java.awt.event.ActionListener;

public class OpenFileButton extends IconButton {
    public OpenFileButton(ActionListener actl) {
        super("Open", "/icons/open.png", actl);
    }
}
