package purdy;

import purdy.components.ColorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PurdyWindow extends AWindowFrame {

    static final String DEFAULT_ICON_PATH = "images/sphere.png";
    
    Color
        bodyColor = new Color(0,0,0,60),
        borderColor = new Color(0,0,0,60);
    
    public void setBGColor(Color value) {
        jPanel_core.setBackground(bodyColor = value);
    }

    public static PurdyWindow quickWindow(String title, boolean titleVisible, Component c) {
        return quickWindow(title, titleVisible, c, new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static PurdyWindow quickWindow(String title, Component c) {
        return quickWindow(title, true, c, new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static PurdyWindow quickWindow(String title, boolean titleVisible, Component c, WindowListener listener) {
        return new PurdyWindow(title, c) {{

            addWindowListener(listener);

            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocation(500, 500);
            enablePersistentSizing(title);
            setVisible(true);

            setWindowTitleVisible(titleVisible);

        }};
    }
    
    public PurdyWindow() {

        initComponents();
        
        setIconImage(new javax.swing.ImageIcon(PurdyWindow.class.getResource(DEFAULT_ICON_PATH)).getImage());
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        jPanel_lowerBody.setBackground(new Color(  0,  0,  0, 0));
        jPanel_core.setBackground(bodyColor);
        
        MouseListener ml = new MouseAdapter() {
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if(getDefaultCloseOperation() != JFrame.DO_NOTHING_ON_CLOSE) 
                    jButton_exit.setVisible(true);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3) 
                    setWindowTitleVisible(!jPanel_titleBar.isVisible());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                jButton_exit.setVisible(false);
            }
            
        };
                
        jButton_exit.setVisible(false);
        jButton_exit.addMouseListener(ml);
        
        jPanel_border_top.addMouseListener(ml);
        jPanel_border_top.addMouseListener(resizeListener);
        jPanel_border_top.addMouseMotionListener(mml);
        jPanel_border_top.addMouseListener(movement_ML);
        jPanel_border_top.addMouseMotionListener(movement_MML);
        
        colorize(borderColor);

        enableBlurBehind();

    }
    
    public PurdyWindow(Component toAdd) {
        this();
        jPanel_core.add(toAdd);
    }
    
    public PurdyWindow(String label, Component toAdd) {
        this(toAdd);
        jLabel_title.setText(label);
    }
    
    public PurdyWindow(String label, Component toAdd, Color c) {
        this(label, toAdd);
        colorize(c);
    }

    public final void colorize(Color c) {

        jPanel_border_top.setBackground(c);
        jPanel_titleBar.setBackground(c);
        jPanel_border_left.setBackground(c);
        jPanel_border_right.setBackground(c);
        jPanel_border_bottom.setBackground(c);

    }
    
    public void setWindowTitleVisible(boolean value) {
         
        jPanel_titleBar.setVisible(value);
        
    }

    //=- Movement 

    private void initComponents() {

        jPanel_exitButtonHolder = new javax.swing.JPanel();
        jPanel_border_top = new ColorPanel();
        jButton_exit = new javax.swing.JButton() {
            @Override
            protected void paintComponent(Graphics g) {
            g.setColor(this.getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
            }
        };
        jPanel_titleBar = new ColorPanel();
        jLabel_title = new javax.swing.JLabel();
        jPanel_lowerBody = new ColorPanel();
        jPanel_core = new ColorPanel();
        jPanel_border_left = new ColorPanel();
        jPanel_border_right = new ColorPanel();
        jPanel_border_bottom = new ColorPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel_exitButtonHolder.setOpaque(false);
        jPanel_exitButtonHolder.setPreferredSize(new java.awt.Dimension(0, 0));

        jPanel_border_top.setBackground(new java.awt.Color(212,238,201,50));
        jPanel_border_top.setMinimumSize(new java.awt.Dimension(10, 3));
        jPanel_border_top.setName("");
        jPanel_border_top.setOpaque(false);
        jPanel_border_top.setPreferredSize(new java.awt.Dimension(199, 3));

        javax.swing.GroupLayout jPanel_TopBorderLayout = new javax.swing.GroupLayout(jPanel_border_top);
        jPanel_border_top.setLayout(jPanel_TopBorderLayout);
        jPanel_TopBorderLayout.setHorizontalGroup(
            jPanel_TopBorderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 310, Short.MAX_VALUE)
        );
        jPanel_TopBorderLayout.setVerticalGroup(
            jPanel_TopBorderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButton_exit.setBackground(new Color(255,50,50,100));
        jButton_exit.setBorder(null);
        jButton_exit.setBorderPainted(false);
        jButton_exit.setContentAreaFilled(false);
        jButton_exit.setFocusPainted(false);
        jButton_exit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_exit.setMinimumSize(new java.awt.Dimension(0, 3));
        jButton_exit.addActionListener(this::jButton1ActionPerformed);

        javax.swing.GroupLayout layout_exitButtonHolder = new javax.swing.GroupLayout(jPanel_exitButtonHolder);
        jPanel_exitButtonHolder.setLayout(layout_exitButtonHolder);
        layout_exitButtonHolder.setHorizontalGroup(
            layout_exitButtonHolder.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout_exitButtonHolder.createSequentialGroup()
                .addComponent(jPanel_border_top, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jButton_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout_exitButtonHolder.setVerticalGroup(
            layout_exitButtonHolder.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout_exitButtonHolder.createSequentialGroup()
                .addGroup(layout_exitButtonHolder.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel_border_top, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_exit, javax.swing.GroupLayout.DEFAULT_SIZE, 3, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        jPanel_titleBar.setBackground(new java.awt.Color(212,238,201,50));
        jPanel_titleBar.setMinimumSize(new java.awt.Dimension(10, 3));
        jPanel_titleBar.setName(""); // NOI18N
        jPanel_titleBar.setOpaque(false);
        jPanel_titleBar.setPreferredSize(new java.awt.Dimension(199, 3));

        jLabel_title.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 16)); // NOI18N
        jLabel_title.setForeground(new java.awt.Color(254, 254, 254));
        jLabel_title.setText("Window Title");
        jLabel_title.setMaximumSize(new java.awt.Dimension(0, 0));
        jLabel_title.setMinimumSize(new java.awt.Dimension(0, 0));
        jLabel_title.setPreferredSize(new java.awt.Dimension(0, 0));

        javax.swing.GroupLayout layout_titleBar = new javax.swing.GroupLayout(jPanel_titleBar);
        jPanel_titleBar.setLayout(layout_titleBar);
        layout_titleBar.setHorizontalGroup(
            layout_titleBar.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout_titleBar.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout_titleBar.setVerticalGroup(
            layout_titleBar.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_title, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
        );

        jPanel_lowerBody.setOpaque(false);

        jPanel_core.setOpaque(false);
        jPanel_core.setLayout(new java.awt.BorderLayout());

        jPanel_border_left.setOpaque(false);
        jPanel_border_left.setLayout(new java.awt.BorderLayout());

        jPanel_border_right.setOpaque(false);
        jPanel_border_right.setLayout(new java.awt.BorderLayout());

        jPanel_border_bottom.setOpaque(false);
        jPanel_border_bottom.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout_lowerBody = new javax.swing.GroupLayout(jPanel_lowerBody);
        jPanel_lowerBody.setLayout(layout_lowerBody);
        layout_lowerBody.setHorizontalGroup(
            layout_lowerBody.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout_lowerBody.createSequentialGroup()
                .addComponent(jPanel_border_left, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout_lowerBody.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel_core, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel_border_bottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addComponent(jPanel_border_right, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout_lowerBody.setVerticalGroup(
            layout_lowerBody.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_border_left, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout_lowerBody.createSequentialGroup()
                .addComponent(jPanel_core, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel_border_bottom, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel_border_right, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout_root = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout_root);
        layout_root.setHorizontalGroup(
            layout_root.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_lowerBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel_exitButtonHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
            .addComponent(jPanel_titleBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
        );
        layout_root.setVerticalGroup(
            layout_root.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout_root.createSequentialGroup()
                .addComponent(jPanel_exitButtonHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel_titleBar, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel_lowerBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public static void main(String[] args) {

        PurdyWindow.quickWindow("Test Alert", new JPanel(new BorderLayout()) {{
            setBackground(new Color(0,0,0,0));
        }}).setWindowTitleVisible(false);

        PurdyWindow.quickWindow("Test Window", new JPanel(new BorderLayout()) {{
            setBackground(new Color(0,0,0,0));
        }});
        
    }

    private javax.swing.JButton jButton_exit;
    private javax.swing.JLabel jLabel_title;
    private javax.swing.JPanel jPanel_lowerBody;
    private javax.swing.JPanel jPanel_core;
    private javax.swing.JPanel jPanel_border_left;
    private javax.swing.JPanel jPanel_border_right;
    private javax.swing.JPanel jPanel_border_bottom;
    private javax.swing.JPanel jPanel_exitButtonHolder;
    private javax.swing.JPanel jPanel_titleBar;
    private javax.swing.JPanel jPanel_border_top;

}
