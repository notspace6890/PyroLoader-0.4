package io.github.giantnuker.pyro.ui;

import javax.swing.*;
import java.awt.*;

public class ConnectingPanel extends JPanel
{
    public ConnectingPanel() {
        this.setLayout(new GridLayout(4, 1));
        final JLabel text = new JLabel("Connecting to Server");
        final JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        this.add(text);
        this.add(bar);
    }
}
