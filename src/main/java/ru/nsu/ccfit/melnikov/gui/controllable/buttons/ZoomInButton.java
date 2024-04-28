package ru.nsu.ccfit.melnikov.gui.controllable.buttons;

import java.awt.event.ActionListener;

public class ZoomInButton extends IconButton {
    public ZoomInButton(ActionListener actl) {
        super("Zoom in", "/icons/zoom_in.png", actl);
    }
}
