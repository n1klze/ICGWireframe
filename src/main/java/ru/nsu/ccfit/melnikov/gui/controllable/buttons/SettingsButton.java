package ru.nsu.ccfit.melnikov.gui.controllable.buttons;

import java.awt.event.ActionListener;

public class SettingsButton extends IconButton {
    public SettingsButton(ActionListener actl) {
        super("Settings", "/icons/settings.png", actl);
    }
}
