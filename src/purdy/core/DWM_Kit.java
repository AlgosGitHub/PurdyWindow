package purdy.core;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class DWM_Kit {
    
    int DWM_BB_ENABLE = 0x00000001;

    public static class DWM_BLURBEHIND extends Structure {

        public int dwFlags;
        public boolean fEnable;
        public int hRgnBlur;
        public int fTransitionOnMaximized;

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

        public int cxLeftWidth;
        public int cxRightWidth;
        public int cyTopHeight;
        public int cyBottomHeight;

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

        public int Attribute;
        public Pointer Data;
        public int SizeOfData;

        @Override
        protected List<String> getFieldOrder() {
                return FIELDS;
        }

    }

    public interface Accent {
        int ACCENT_DISABLED = 0;
        int ACCENT_ENABLE_GRADIENT = 1;
        int ACCENT_ENABLE_TRANSPARENTGRADIENT = 2;
        int ACCENT_ENABLE_BLURBEHIND = 3;
        int ACCENT_ENABLE_ACRYLIC = 4; // YES, available on build 17063
        int ACCENT_INVALID_STATE = 5;
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

        public int AccentState;
        public int AccentFlags;
        public int GradientColor;
        public int AnimationId;

        @Override
        protected List<String> getFieldOrder() {
                return FIELDS;
        }

    }

}
