package io.github.giantnuker.pyro.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ReconnectPanel extends JPanel
{
    public ReconnectPanel(final Runnable retry) {
        this.setLayout(new BoxLayout(this, 1));
        final JLabel text = new JLabel("Connection Failed");
        final JButton retryButton = new JButton("Retry");
        retryButton.addActionListener(e -> retry.run());
        this.add(text);
        this.add(retryButton);
    }
}
