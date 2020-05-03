package purdy.demo;

import purdy.PurdyWindow;

import javax.swing.*;
import java.awt.*;

public class PurdyWindowDemo {

    public static void main(String[] args) {

        PurdyWindow.quickWindow("Test Alert", new JPanel(new BorderLayout()) {{
            setBackground(new Color(0,0,0,0));
        }}).setWindowTitleVisible(false);

        PurdyWindow.quickWindow("Test Window", new JPanel(new BorderLayout()) {{
            setBackground(new Color(0,0,0,0));
        }});

    }

}
