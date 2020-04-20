package purdy.components;

import javax.swing.*;
import java.awt.*;

public class ColorPanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(this.getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g); 
    }

}