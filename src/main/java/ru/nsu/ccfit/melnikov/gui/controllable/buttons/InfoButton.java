package ru.nsu.ccfit.melnikov.gui.controllable.buttons;

import java.awt.event.ActionListener;

public class InfoButton extends IconButton {
    public InfoButton(ActionListener actl) {
        super("About", "/icons/info.png", actl);
    }
}
