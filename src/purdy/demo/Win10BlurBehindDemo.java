package purdy.demo;

import purdy.components.BlurBehindFrame;
import purdy.components.ColorPanel;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author antho
 */
public class Win10BlurBehindDemo extends BlurBehindFrame {
    
    public Win10BlurBehindDemo() {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 480);
        setTitle("Hello");

        add(new ColorPanel(new Color(0,0,66,6)) {{

                setLayout(new BorderLayout());

                add(new JLabel("My background is blurry!") {{
                        setFont(new Font("Dialog", Font.BOLD, 48));
                        setForeground(Color.red);
                    }},
                    BorderLayout.CENTER
                );

            }}
        );

        enableBlurBehind();

    }
    
    public static void main(String... args) {
        new Win10BlurBehindDemo();
    }

}

        