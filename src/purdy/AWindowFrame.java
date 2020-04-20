package purdy;

import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import purdy.core.DWM_Kit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AWindowFrame extends javax.swing.JFrame {
    
    String windowPersistenceID = "";
    
    int startX = 0,
        startY = 0,
        myXinWindow = 0,
        myYinWindow = 0,
        right,
        left,
        top,
        bottom,
        initialWidth,
        initialHeight;
    
    boolean
        resizingInProgress = false,
        resizing_N   =  false,
        resizing_NE  =  false,
        resizing_E   =  false,
        resizing_SE  =  false,
        resizing_S   =  false,
        resizing_SW  =  false,
        resizing_W   =  false,
        resizing_NW  =  false;
    
    MouseMotionListener mml;
    MouseAdapter resizeListener;
    public MouseListener movement_ML;    
    public MouseMotionListener movement_MML;

    class WindowLocationOffset {

        final AWindowFrame sisterWindow;
        final int
                xOffset,
                yOffset;

        WindowLocationOffset(AWindowFrame sisterWindow, int xOffset, int yOffset) {
            this.sisterWindow = sisterWindow;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }

    }

    static final List<AWindowFrame> parallelInstancesForSnapping = new ArrayList<>();

    //-
    
    public AWindowFrame() {

        setUndecorated(true);
        setBackground(new Color(0,0,0,0)); 
        setupResizeAndDrag();

        parallelInstancesForSnapping.add(this);

        addComponentListener(new ComponentAdapter() {
            
            void ensureWindowIsOnScreen(ComponentEvent ce) {

                boolean someoneIsHoldingAlt = holdingAlt;

                if(!someoneIsHoldingAlt)
                    for(WindowLocationOffset sis : sistersInDrag) {
                        if(sis.sisterWindow.holdingAlt) {
                            someoneIsHoldingAlt = true;
                            break;
                        }
                    }

                if(!someoneIsHoldingAlt) {
                    return;
                }

                Rectangle myHostScreen = getGraphicsConfiguration().getDevice().getDefaultConfiguration().getBounds();

                //todo: only run this when we first create the window, and otherwise handle screen edge snapping with the other snapping features

                int winX = getX(),
                    winY = getY(),
                    minX = winX,
                    minY = winY,
                    maxX = winX + getWidth(),
                    maxY = winY + getHeight();

                for(WindowLocationOffset sis : sistersInDrag) {
                    minX = Math.min(sis.sisterWindow.getX(), minX);
                    minY = Math.min(sis.sisterWindow.getY(), minY);
                    //
                    maxX = Math.max(sis.sisterWindow.getX() + sis.sisterWindow.getWidth(), maxX);
                    maxY = Math.max(sis.sisterWindow.getY() + sis.sisterWindow.getHeight(), maxY);
                }

                int
                    leftOffset = winX - minX,
                    topOffset = winY - minY;

                int winWidth = maxX - winX,
                    winHeight = maxY - winY;

                if(minX < myHostScreen.x) {
                    snappedX = minX + leftOffset + 5;
                    isSnapped_X = true;
                }
                
                if(maxX > myHostScreen.x + myHostScreen.width) {
                    snappedX = (myHostScreen.x + myHostScreen.width) - (winWidth + 5);
                    isSnapped_X = true;
                }

                if(minY < myHostScreen.y) {
                    snappedY = minY + topOffset + 5;
                    isSnapped_Y = true;
                }

                if(maxY > myHostScreen.y + myHostScreen.height) {
                    snappedY = (myHostScreen.y + myHostScreen.height) - (winHeight + 5);
                    isSnapped_Y = true;
                }

                //System.out.println("Snapping");

                //setLocation(newX, newY);
                
            }
            
            @Override
            public void componentResized(ComponentEvent ce) {
                ensureWindowIsOnScreen(ce);
            }

            @Override
            public void componentMoved(ComponentEvent ce) {
                ensureWindowIsOnScreen(ce);
            }

            @Override
            public void componentShown(ComponentEvent ce) {
                ensureWindowIsOnScreen(ce);
            }
            
        });

    }

    boolean
        holdingControl = false,
        holdingAlt = false,
        isSnapped_X = false,
        isSnapped_Y = false;

    int
        snappingDistanceInPx = 0,
        snappingTriggerDistanceInPx = 8,
        snappedX = 0,//these are only used when being snapped
        snappedY = 0;

    //when whichever window is being dragged, it'll update all sisters in this shared groupID to move with it. Pixel-by-pixel
    //screen corner alignment will honor the edge-windows in the cluster of snapped-together windows
    //this way; persistence only needs to remember a groupID!!!!

    String
        snappedTo_GroupID = "";

    List<WindowLocationOffset> sistersInDrag = new ArrayList<>();

    final void setupResizeAndDrag() {
        
        final JFrame frame = this; 
        
        movement_ML = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getXOnScreen();
                startY = e.getYOnScreen();

                sistersInDrag.clear();

                if(!snappedTo_GroupID.isEmpty())
                    for(AWindowFrame sisWin : parallelInstancesForSnapping)
                        if(sisWin.snappedTo_GroupID.equals(snappedTo_GroupID) && !sisWin.windowPersistenceID.equals(windowPersistenceID)) {
                            sistersInDrag.add(new WindowLocationOffset(sisWin, getX() - sisWin.getX(), getY() - sisWin.getY()));
                            sisWin.setVisible(true);
                        }
                //-
                myXinWindow = e.getXOnScreen() - frame.getX();
                myYinWindow = e.getYOnScreen() - frame.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                resizingInProgress = false;
                isSnapped_X = false;
                isSnapped_Y = false;
                holdingControl = false;
                holdingAlt = false;
                sistersInDrag.forEach( offset -> {
                    offset.sisterWindow.holdingAlt = false;
                    offset.sisterWindow.holdingControl = false;
                });
            }

            @Override
            public void mouseExited(MouseEvent e) {
                frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

        };

        movement_MML = new MouseMotionAdapter() {

            void setGroupID(AWindowFrame sisWin) {

                if(sisWin.snappedTo_GroupID.isEmpty())
                    sisWin.snappedTo_GroupID = Long.toString(new Random().nextLong());

                snappedTo_GroupID = sisWin.snappedTo_GroupID;

            }


            @Override
            public void mouseDragged(MouseEvent e) {

                int deltaX = e.getXOnScreen() - startX - myXinWindow;
                int deltaY = e.getYOnScreen() - startY - myYinWindow;

                //find containing monitor
                  //snap-to 8px from walls if within 10px of wall

                if(!resizingInProgress) {

                    int newX = startX + deltaX,
                        newY = startY + deltaY;

                    if(holdingControl) {

                        for(AWindowFrame sisWin : parallelInstancesForSnapping) {

                            int xDistance_right = frame.getX() + frame.getWidth() - sisWin.getX(),
                                xDistance_left = sisWin.getX() + sisWin.getWidth() - frame.getX(),
                                yDistance_top = (sisWin.getY() + sisWin.getHeight()) - frame.getY(),
                                yDistance_bottom = frame.getY() + frame.getHeight() - sisWin.getY();

                            boolean
                                xTouchingRight = xDistance_right < snappingTriggerDistanceInPx && xDistance_right > 0,
                                xTouchingLeft = xDistance_left < snappingTriggerDistanceInPx && xDistance_left > 0,
                                yTouchingTop = yDistance_top < snappingTriggerDistanceInPx && yDistance_top > 0,
                                yTouchingBottom = yDistance_bottom < snappingTriggerDistanceInPx && yDistance_bottom > 0,

                                yOverlap =
                                   frame.getY() + frame.getHeight() > sisWin.getY()
                                && frame.getY() < sisWin.getY() + sisWin.getHeight(),

                                xOverlap =
                                   frame.getX() + frame.getWidth() > sisWin.getX()
                                && frame.getX() < sisWin.getX() + sisWin.getWidth();

                            if(yOverlap) {

                                if(xTouchingRight) {

                                    snappedX = sisWin.getX() - snappingDistanceInPx - frame.getWidth();

                                    setGroupID(sisWin);

                                    isSnapped_X = true;

                                }

                                if(xTouchingLeft) {

                                    snappedX = sisWin.getX() + sisWin.getWidth() + snappingDistanceInPx;

                                    setGroupID(sisWin);

                                    isSnapped_X = true;

                                }

                            }

                            if (xOverlap) {

                                if(yTouchingTop) {

                                    snappedY = sisWin.getY() + sisWin.getHeight() + snappingDistanceInPx;

                                    setGroupID(sisWin);

                                    isSnapped_Y = true;

                                }

                                if(yTouchingBottom) {

                                    snappedY = sisWin.getY() - snappingDistanceInPx - frame.getHeight();

                                    setGroupID(sisWin);

                                    isSnapped_Y = true;

                                }

                            }

                        }

                        if(!isSnapped_X && !isSnapped_Y) {
                            snappedTo_GroupID = "";
                            sistersInDrag.clear();
                        }

                    }

                    int finalX = isSnapped_X ? snappedX : newX,
                        finalY = isSnapped_Y ? snappedY : newY;

                    frame.setLocation(finalX, finalY);

                    sistersInDrag.forEach( offset -> offset.sisterWindow.setLocation(finalX - offset.xOffset, finalY - offset.yOffset) );

                }


            }

        };
        
        mml = new MouseMotionListener() {

            boolean cursorChanged = false;

            @Override
            public void mouseDragged(MouseEvent e) {

                
                if(resizing_W) {
                    
                    int newWidth = Math.max(frame.getMinimumSize().width,
                            initialWidth + (left - e.getXOnScreen()));

                    frame.setLocation(left - (left - e.getXOnScreen()), top);
                    frame.setSize(newWidth, initialHeight);

                    resizingInProgress = true;
                    
                } else if(resizing_NW) {
                    
                    int newWidth = Math.max(frame.getMinimumSize().width,
                            initialWidth + (left - e.getXOnScreen()));
                    int newHeight = Math.max(frame.getMinimumSize().height,
                            bottom - e.getYOnScreen()); 

                    frame.setLocation(left - (left - e.getXOnScreen()), bottom - newHeight);
                    frame.setSize(newWidth, newHeight);
                    
                    resizingInProgress = true;

                } else if(resizing_SW) {
                    
                    int newWidth = Math.max(frame.getMinimumSize().width,
                            initialWidth + (left - e.getXOnScreen()));
                    int newHeight = Math.max(frame.getMinimumSize().height,
                            e.getYOnScreen() - top); 

                    frame.setLocation(left - (left - e.getXOnScreen()), top);
                    frame.setSize(newWidth, newHeight);
                    
                    resizingInProgress = true;

                } else if(resizing_E) {
                    
                    frame.setSize(Math.max(frame.getMinimumSize().width, e.getXOnScreen() - left), initialHeight);

                    resizingInProgress = true;
                    
                } else if(resizing_SE) {
                    
                    int targetX = e.getXOnScreen();
                    int targetY = e.getYOnScreen();

                    int newWidth = Math.max(frame.getMinimumSize().width, targetX - left);
                    int newHeight = Math.max(frame.getMinimumSize().height, targetY - top); 

                    frame.setLocation(left, top);
                    frame.setSize(newWidth, newHeight);
                    
                    resizingInProgress = true;
                    
                } else if(resizing_S) {
                    
                    int newHeight = Math.max(frame.getMinimumSize().height, e.getYOnScreen() - top); 
                    
                    frame.setLocation(left, top);
                    frame.setSize(initialWidth, newHeight);
                    
                    resizingInProgress = true;
                    
                } else if(resizing_N) {
                    
                    int newHeight = Math.max(frame.getMinimumSize().height, bottom - e.getYOnScreen()); 
                    
                    frame.setLocation(left, bottom - newHeight);
                    frame.setSize(initialWidth, newHeight);
                    
                    resizingInProgress = true;
                    
                }

            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                int width = frame.getWidth();
                int height = frame.getHeight();
                
                int mouse_X = e.getX();
                int mouse_Y = e.getY();
                
                if(mouse_X < 10 && ( mouse_Y > 10 && mouse_Y < height-10) ) {
                    frame.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
                    cursorChanged = true;
                } else if(mouse_X < 10 && mouse_Y < 10) {
                    frame.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
                    cursorChanged = true;
                } else if(mouse_X < 10 && mouse_Y > height-10) {
                    frame.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
                    cursorChanged = true;
                } else if (mouse_X > width - 10 && ( mouse_Y > 10 && mouse_Y < height-10)) {
                    frame.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
                    cursorChanged = true;
                } else if (mouse_X > width - 10 && mouse_Y > height - 10 ) {
                    frame.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
                    cursorChanged = true;
                } else if (mouse_Y > height - 10 && (mouse_X > 10 && mouse_X < width-10) ) {
                    frame.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
                    cursorChanged = true;
                } else if (mouse_Y < 3 && (mouse_X > 10 && mouse_X < width-20) ) {
                    frame.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
                    cursorChanged = true;
                } else if(cursorChanged) {
                    cursorChanged = false;
                    frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
                           
            }
                                    
        };
        
        resizeListener = new MouseAdapter(){
            
            @Override
            public void mousePressed(MouseEvent e){
                left = frame.getX();
                right = frame.getX() + frame.getWidth();
                top = frame.getY();
                bottom = frame.getY() + frame.getHeight();
                initialWidth = frame.getWidth();
                initialHeight = frame.getHeight();
                
                int 
                    width = frame.getWidth(),
                    height = frame.getHeight(),                        
                    mouse_X = e.getX(),
                    mouse_Y = e.getY();
                
                if(mouse_X < 10 && ( mouse_Y > 10 && mouse_Y < height-10) ) {
                    resizing_W = true;
                } else if(mouse_X < 10 && mouse_Y < 10) {
                    resizing_NW = true;
                } else if(mouse_X < 10 && mouse_Y > height-10) {
                    resizing_SW = true;
                } else if (mouse_X > width - 10 && ( mouse_Y > 10 && mouse_Y < height-10)) {
                    resizing_E = true;
                } else if (mouse_X > width - 10 && mouse_Y > height - 10 ) {
                    resizing_SE = true;
                } else if (mouse_Y > height - 10 && (mouse_X > 10 && mouse_X < width-10) ) {
                    resizing_S = true;
                } else if (mouse_Y < 3 && (mouse_X > 10 && mouse_X < width-20) ) {
                    resizing_N = true;
                }
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                resizing_N = false;
                resizing_S = false;
                resizing_E = false;
                resizing_W = false;
                resizing_NE = false;
                resizing_NW = false;
                resizing_SE = false;
                resizing_SW = false;
            }
            
        };
        
        this.addMouseListener(resizeListener);
        this.addMouseMotionListener(mml);
        this.addMouseListener(movement_ML);
        this.addMouseMotionListener(movement_MML);

        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                holdingControl = e.isControlDown();
                holdingAlt = e.isAltDown();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                holdingControl = false;
                holdingAlt = false;
            }

        });

    }
    
    public void enablePersistentSizing(String windowSizeMemoryID) {

        //todo: save all windows persistence metadata to a single cache file?
        //todo: remember if titleBar was visible too.

        this.windowPersistenceID = windowSizeMemoryID;
        String filePath = "d:/moneypenny/windows/"+windowSizeMemoryID;
        
        Path input = Paths.get(filePath);
        if (input.toFile().canRead() && input.toFile().isFile()) 
            try (BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8)) {
                
                int x, width,
                    y, height;

                
                for (String line; (line = reader.readLine()) != null; ) 
                    if(!line.isEmpty()) {
                        String[] values = line.split("/");
                        x = Integer.parseInt(values[0]);
                        y = Integer.parseInt(values[1]);
                        width  = Integer.parseInt(values[2]);
                        height = Integer.parseInt(values[3]);

                        if(values.length > 4)
                            this.snappedTo_GroupID = String.valueOf(values[4]);

                        this.setLocation(x, y);
                        this.setSize(width, height);
                    }

            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
        
        final AWindowFrame me = this;
        
        this.addComponentListener(new ComponentListener() {
            
            synchronized void saveSizeAndPosition() {

                File path = new File(filePath.substring(0, filePath.lastIndexOf("/")));

                try {

                    if (!path.exists() && !path.mkdirs())
                        throw new Exception("Unable to create window persistence storage file");

                    try (FileOutputStream fos = new FileOutputStream(filePath, false); PrintStream ps = new PrintStream(fos)) {

                        ps.println(me.getX() + "/" + me.getY() + "/" + me.getWidth() + "/" + me.getHeight() + "/" + me.snappedTo_GroupID);

                    }

                } catch (Exception ex) {
                    ex.printStackTrace(System.out);
                }

            }

            @Override
            public void componentResized(ComponentEvent e) {
                saveSizeAndPosition();      
            }

            @Override
            public void componentMoved(ComponentEvent ce) {
                saveSizeAndPosition();
            }

            @Override
            public void componentShown(ComponentEvent ce) {

            }

            @Override
            public void componentHidden(ComponentEvent ce) {

            }

        }); 
        
    }

    public void colorize(Color c)  { }


    public void enableBlurBehind() {
        
        WinDef.HWND hwnd = new WinDef.HWND(Native.getWindowPointer(this));

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
        setWindowCompositionAttribute.invoke(WinNT.HRESULT.class, new Object[] { hwnd, data });
        
    }

}
