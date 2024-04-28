package ru.nsu.ccfit.melnikov.gui.controllable.buttons;

import java.awt.event.ActionListener;

public class SaveButton extends IconButton {
    public SaveButton(ActionListener actl) {
        super("Save", "/icons/save.png", actl);
    }
}
