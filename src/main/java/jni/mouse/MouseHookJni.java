package jni.mouse;

import timer.Timer;

public class MouseHookJni {
    static {
        //System.load("C:/Users/dheld/Desktop/timeBeanTest/timeBeanTest/libMouseHook.dll");
        System.loadLibrary("libMouseHook");
    }

    public native void startMouseHook();
    public native void stopMouseHook();

    public void handleMouseEvent(int eventType, int lParam) {
        switch (eventType) {
            case 0x0200: // WM_MOUSEMOVE
                System.out.println("Mouse moved");
                Timer.resetCloseSeconds();
                break;
            case 0x0201: // WM_LBUTTONDOWN
                System.out.println("Left mouse button down");
                Timer.resetCloseSeconds();
                break;
            case 0x0202: // WM_LBUTTONUP
                System.out.println("Left mouse button up");
                Timer.resetCloseSeconds();
                break;
            case 0x0204: // WM_RBUTTONDOWN
                System.out.println("Right mouse button down");
                Timer.resetCloseSeconds();
                break;
            case 0x0205: // WM_RBUTTONUP
                System.out.println("Right mouse button up");
                Timer.resetCloseSeconds();
                break;
            case 0x0207: // WM_MBUTTONDOWN
                System.out.println("Middle mouse button down");
                Timer.resetCloseSeconds();
                break;
            case 0x0208: // WM_MBUTTONUP
                System.out.println("Middle mouse button up");
                Timer.resetCloseSeconds();
                break;
            default:
                System.out.println("Unknown mouse event");
                Timer.resetCloseSeconds();
                break;
        }
    }
}
