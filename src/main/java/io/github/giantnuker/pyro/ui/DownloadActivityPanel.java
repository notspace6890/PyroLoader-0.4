package io.github.giantnuker.pyro.ui;

import javax.swing.*;
import java.awt.*;

public class DownloadActivityPanel extends JPanel
{
    JProgressBar bar;
    JLabel label;
    
    public DownloadActivityPanel() {
        this.setLayout(new GridLayout(5, 1));
        this.label = new JLabel("Readying");
        (this.bar = new JProgressBar()).setIndeterminate(true);
        this.add(this.label);
        this.add(this.bar);
    }
    
    public void setDownloadProgress(final int amount, final int max) {
        this.bar.setIndeterminate(false);
        this.bar.setMaximum(max + 1);
        this.label.setText("Loading");
        this.bar.setValue(amount + 1);
    }
}
