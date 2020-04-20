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
        bodyColor = value;
        jPanel2.setBackground(bodyColor);
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
        
        jPanel1.setBackground(new Color(  0,  0,  0, 0));
        jPanel2.setBackground(bodyColor);
        jPanel3.setBackground(new Color(255,255,255,25));
        jPanel4.setBackground(new Color(255,255,255,25));
        jPanel5.setBackground(new Color(255,255,255,25));
        jPanel_TopBorder.setBackground(new Color(255,255,255,25));
        
        MouseListener ml = new MouseAdapter() {
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if(getDefaultCloseOperation() != JFrame.DO_NOTHING_ON_CLOSE) 
                    jButton1.setVisible(true);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3) 
                    setWindowTitleVisible(!jPanel_TitleBar.isVisible());                
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                jButton1.setVisible(false);
            }
            
        };
                
        jButton1.setVisible(false);
        jButton1.addMouseListener(ml);
        
        jPanel_TopBorder.addMouseListener(ml);
        jPanel_TopBorder.addMouseListener(resizeListener);
        jPanel_TopBorder.addMouseMotionListener(mml);
        jPanel_TopBorder.addMouseListener(movement_ML);
        jPanel_TopBorder.addMouseMotionListener(movement_MML);
        
        colorize(borderColor);
        
        enableBlurBehind();
        
    }
    
    public PurdyWindow(Component toAdd) {
        this();
        jPanel2.add(toAdd);
    }
    
    public PurdyWindow(String label, Component toAdd) {
        this(toAdd);
        jLabel_Title.setText(label);
    }
    
    public PurdyWindow(String label, Component toAdd, Color c) {
        this(label, toAdd);
        colorize(c);
    }
    
    @Override
    public final void colorize(Color c) {

        jPanel_TopBorder.setBackground(c);
        jPanel_TitleBar.setBackground(c);
        jPanel3.setBackground(c);
        jPanel4.setBackground(c);
        jPanel5.setBackground(c);

    }
    
    public void setWindowTitleVisible(boolean value) {
         
        jPanel_TitleBar.setVisible(value);
        
    }

    //=- Movement 

    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        jPanel_TopBorder = new ColorPanel();
        jButton1 = new javax.swing.JButton() {
            @Override
            protected void paintComponent(Graphics g) {
            g.setColor(this.getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
            }
        };
        jPanel_TitleBar = new ColorPanel();
        jLabel_Title = new javax.swing.JLabel();
        jPanel1 = new ColorPanel();
        jPanel2 = new ColorPanel();
        jPanel3 = new ColorPanel();
        jPanel4 = new ColorPanel();
        jPanel5 = new ColorPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel6.setOpaque(false);
        jPanel6.setPreferredSize(new java.awt.Dimension(0, 0));

        jPanel_TopBorder.setBackground(new java.awt.Color(212,238,201,50));
        jPanel_TopBorder.setMinimumSize(new java.awt.Dimension(10, 3));
        jPanel_TopBorder.setName("");
        jPanel_TopBorder.setOpaque(false);
        jPanel_TopBorder.setPreferredSize(new java.awt.Dimension(199, 3));

        javax.swing.GroupLayout jPanel_TopBorderLayout = new javax.swing.GroupLayout(jPanel_TopBorder);
        jPanel_TopBorder.setLayout(jPanel_TopBorderLayout);
        jPanel_TopBorderLayout.setHorizontalGroup(
            jPanel_TopBorderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 310, Short.MAX_VALUE)
        );
        jPanel_TopBorderLayout.setVerticalGroup(
            jPanel_TopBorderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButton1.setBackground(new Color(255,50,50,100));
        jButton1.setBorder(null);
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusPainted(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMinimumSize(new java.awt.Dimension(0, 3));
        jButton1.addActionListener(this::jButton1ActionPerformed);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel_TopBorder, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel_TopBorder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 3, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        jPanel_TitleBar.setBackground(new java.awt.Color(212,238,201,50));
        jPanel_TitleBar.setMinimumSize(new java.awt.Dimension(10, 3));
        jPanel_TitleBar.setName(""); // NOI18N
        jPanel_TitleBar.setOpaque(false);
        jPanel_TitleBar.setPreferredSize(new java.awt.Dimension(199, 3));

        jLabel_Title.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 16)); // NOI18N
        jLabel_Title.setForeground(new java.awt.Color(254, 254, 254));
        jLabel_Title.setText("Window Title");
        jLabel_Title.setMaximumSize(new java.awt.Dimension(0, 0));
        jLabel_Title.setMinimumSize(new java.awt.Dimension(0, 0));
        jLabel_Title.setPreferredSize(new java.awt.Dimension(0, 0));

        javax.swing.GroupLayout jPanel_TitleBarLayout = new javax.swing.GroupLayout(jPanel_TitleBar);
        jPanel_TitleBar.setLayout(jPanel_TitleBarLayout);
        jPanel_TitleBarLayout.setHorizontalGroup(
            jPanel_TitleBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_TitleBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_Title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel_TitleBarLayout.setVerticalGroup(
            jPanel_TitleBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_Title, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
        );

        jPanel1.setOpaque(false);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel5.setOpaque(false);
        jPanel5.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
            .addComponent(jPanel_TitleBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel_TitleBar, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(() -> {

            PurdyWindow.quickWindow("Test Alert", new JPanel(new BorderLayout()) {{
                setBackground(new Color(0,0,0,1));
            }}).setWindowTitleVisible(false);

            PurdyWindow.quickWindow("Test Window", new JPanel(new BorderLayout()) {{
                setBackground(new Color(0,0,0,1));
            }});

        });
        
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel_Title;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel_TitleBar;
    private javax.swing.JPanel jPanel_TopBorder;

}
