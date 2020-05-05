package adp.ux.purdy.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class FlatScrollPane extends JScrollPane {
    public FlatScrollPane(Component toAdd) {

        super(toAdd);

        setOpaque(false);
        getViewport().setOpaque(false);

        setBorder(BorderFactory.createEmptyBorder());
        getViewport().setBorder(BorderFactory.createEmptyBorder());

        setVerticalScrollBar(new javax.swing.JScrollBar() {

            {

                setForeground(new java.awt.Color(255, 255, 0));
                setOpaque(false);

                setMaximumSize(new Dimension(6, 9999));
                setMinimumSize(new Dimension(6,5));
                setPreferredSize(new Dimension(6, 40));

                setUI(new BasicScrollBarUI() {

                    @Override
                    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {

                        g.setColor(new Color(0,0,0,0));
                        g.fillRect(0, 0, getWidth(), getHeight());

                    }

                    @Override
                    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {

                        g.setColor(new Color(254,254,254,100));
                        g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);

                        g.setColor(new Color(254,254,254,200));
                        g.fillRect(thumbBounds.x - 10, thumbBounds.y, 9, thumbBounds.height);
                        g.fillRect(thumbBounds.x + thumbBounds.width + 1, thumbBounds.y, 9, thumbBounds.height);

                    }

                    @Override
                    protected JButton createDecreaseButton(int orientation) {
                        return createButton();
                    }

                    @Override
                    protected JButton createIncreaseButton(int orientation) {
                        return createButton();
                    }

                    private JButton createButton() {
                        return new JButton() {{
                            setPreferredSize(new Dimension(0, 0));
                            setMinimumSize(new Dimension(0, 0));
                            setMaximumSize(new Dimension(0, 0));
                        }};
                    }

                });

                setUnitIncrement(20);

            }

            @Override
            public void paintComponent(Graphics g) {

                g.setColor(new Color(1,1,1,200));
                g.fillRect(0, 0, getWidth(), getHeight());

                super.paintComponent(g);

            }

        });

    }
}
