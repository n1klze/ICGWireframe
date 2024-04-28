package ru.nsu.ccfit.melnikov.gui.controllable.buttons;

import java.awt.event.ActionListener;

public class RefreshButton extends IconButton {
    public RefreshButton(ActionListener actl) {
        super("Update", "/icons/refresh.png", actl);
    }
}
