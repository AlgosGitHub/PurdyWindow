package purdy.components;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class DWM_Kit {
    
    int DWM_BB_ENABLE = 0x00000001;

    public static class DWM_BLURBEHIND extends Structure {

        public boolean fEnable;
        public int
            dwFlags,
            hRgnBlur,
            fTransitionOnMaximized;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(
                "dwFlags",
                "fEnable",
                "hRgnBlur",
                "fTransitionOnMaximized"
            );
        }

    }

    public class MARGINS extends Structure {

        public int
            cxLeftWidth,
            cxRightWidth,
            cyTopHeight,
            cyBottomHeight;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(
                "cxLeftWidth",
                "cxRightWidth",
                "cyTopHeight",
                "cyBottomHeight"
            );
        }

    }

    public static class WindowCompositionAttributeData extends Structure {

        public static final List<String>
            FIELDS = createFieldsOrder(
                "Attribute",
                "Data",
                "SizeOfData"
            );

        public Pointer Data;
        public int
            Attribute,
            SizeOfData;

        @Override
        protected List<String> getFieldOrder() {
                return FIELDS;
        }

    }

    public interface Accent {
        int ACCENT_DISABLED = 0,
            ACCENT_ENABLE_GRADIENT = 1,
            ACCENT_ENABLE_TRANSPARENTGRADIENT = 2,
            ACCENT_ENABLE_BLURBEHIND = 3,
            ACCENT_ENABLE_ACRYLIC = 4, // YES, available on build 17063
            ACCENT_INVALID_STATE = 5;
    }

    public interface WindowCompositionAttribute {
        int WCA_ACCENT_POLICY = 19;
    }

    public static class AccentPolicy extends Structure implements Structure.ByReference {

        public static final List<String>
            FIELDS = createFieldsOrder(
                "AccentState",
                "AccentFlags",
                "GradientColor",
                "AnimationId"
            );

        public int
            AccentState,
            AccentFlags,
            GradientColor,
            AnimationId;

        @Override
        protected List<String> getFieldOrder() {
                return FIELDS;
        }

    }

}
