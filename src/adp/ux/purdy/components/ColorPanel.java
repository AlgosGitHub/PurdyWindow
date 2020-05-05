package adp.ux.purdy.components;

import javax.swing.*;
import java.awt.*;

public class ColorPanel extends JPanel {

    public ColorPanel() {}

    public ColorPanel(Color backgroundColor) {
        this.setBackground(backgroundColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(this.getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g); 
    }

}