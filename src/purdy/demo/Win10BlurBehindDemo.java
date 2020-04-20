package purdy.demo;

import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import purdy.core.DWM_Kit;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author antho
 */
public class Win10BlurBehindDemo extends JFrame {
    
    public Win10BlurBehindDemo() {

        setUndecorated(true);
        
        JRootPane root = getRootPane();
        root.setOpaque(false);
        root.setDoubleBuffered(false);

        setSize(640, 480);
        setBackground(new Color(0,0,0,0));
        setTitle("Hello");
                
        TranslucentDemoPanel panel = new TranslucentDemoPanel();
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel("My background is blurry!");
        label.setFont(new Font("Dialog", Font.BOLD, 48));
        label.setForeground(Color.red);
        panel.add(label, BorderLayout.CENTER);
        
        add(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
        HWND hwnd = new HWND(Native.getWindowPointer(this));

        NativeLibrary user32 = NativeLibrary.getInstance("user32");
        
        DWM_Kit.AccentPolicy accent = new DWM_Kit.AccentPolicy();
        accent.AccentState = DWM_Kit.Accent.ACCENT_ENABLE_BLURBEHIND;
        accent.GradientColor = 0x7F000000;
        accent.write();

        DWM_Kit.WindowCompositionAttributeData data = new DWM_Kit.WindowCompositionAttributeData();
        data.Attribute = DWM_Kit.WindowCompositionAttribute.WCA_ACCENT_POLICY;
        data.SizeOfData = accent.size();
        data.Data = accent.getPointer();
        
        Function setWindowCompositionAttribute = user32.getFunction("SetWindowCompositionAttribute");
        setWindowCompositionAttribute.invoke(HRESULT.class, new Object[] { hwnd, data });
                
    }
    
    public static void main(String... args) {
        new Win10BlurBehindDemo();
    }

    private static class TranslucentDemoPanel extends JPanel
    {
        
        Color smoke_color = new Color(166,166,166,66);
        
        @Override
        protected void paintComponent(Graphics g) 
        {
            if (g instanceof Graphics2D) {
                
                Graphics2D g2d = (Graphics2D)g;
                
                g2d.setColor(smoke_color);
                
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
            }
        }
    }
}

        