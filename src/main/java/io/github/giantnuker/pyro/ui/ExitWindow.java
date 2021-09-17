package io.github.giantnuker.pyro.ui;

import javax.swing.*;
import java.awt.*;

public class ExitWindow extends JFrame
{
    public ExitWindow() {
        super("Pyro Client");
        this.add(new JLabel("Shutting down. I can't shut down directly because of forge. (Banned from discord for asking lmao)"));
        this.pack();
        this.setDefaultCloseOperation(0);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setVisible(true);
    }
}
