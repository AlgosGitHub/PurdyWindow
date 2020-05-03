package purdy.components;

import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

import javax.swing.*;
import java.awt.*;

public class BlurBehindFrame extends JFrame {

    {

        JRootPane root = getRootPane();
        root.setOpaque(false);
        root.setDoubleBuffered(false);

        setUndecorated(true);
        setBackground(new Color(0,0,0,1));

    }

    /**
     * Enable the Blur Behind Effect.
     * <p>
     * Window Frame must be visible,
     * therefore setVisible(true) is implied.
     * </p>
     * Once enabled, the effect cannot be disabled.
     *
     */
    public final void enableBlurBehind() {

        //This Frame must be visible to enable the effect
        setVisible(true);

        //-

        DWM_Kit.AccentPolicy accent = new DWM_Kit.AccentPolicy();
        accent.AccentState = DWM_Kit.Accent.ACCENT_ENABLE_BLURBEHIND;
        accent.GradientColor = 0x7F000000;
        accent.write();

        DWM_Kit.WindowCompositionAttributeData data = new DWM_Kit.WindowCompositionAttributeData();
        data.Attribute = DWM_Kit.WindowCompositionAttribute.WCA_ACCENT_POLICY;
        data.SizeOfData = accent.size();
        data.Data = accent.getPointer();

        Function setWindowCompositionAttribute = NativeLibrary.getInstance("user32").getFunction("SetWindowCompositionAttribute");

        //Frame must be visible for this line to execute
        setWindowCompositionAttribute.invoke(WinNT.HRESULT.class, new Object[] { new WinDef.HWND(Native.getWindowPointer(this)), data });

    }

}
