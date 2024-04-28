package ru.nsu.ccfit.melnikov.gui.controllable.buttons;

import java.awt.event.ActionListener;

public class ZoomOutButton extends IconButton {
    public ZoomOutButton(ActionListener actl) {
        super("Zoom out", "/icons/zoom_out.png", actl);
    }
}
