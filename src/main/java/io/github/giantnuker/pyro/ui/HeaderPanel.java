package io.github.giantnuker.pyro.ui;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel
{
    private ImageIcon image;
    
    public HeaderPanel() {
        this.setPreferredSize(new Dimension(128, 64));
        this.image = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/assets/minecraft/pyro/textures/logo.png")).getScaledInstance(128, 64, 4));
    }
    
    public void paintComponent(final Graphics comp) {
        comp.setColor(new Color(17, 17, 17));
        comp.fillRect(0, 0, this.getSize().width, this.getSize().height);
        this.image.paintIcon(this, comp, 0, 0);
    }
}
