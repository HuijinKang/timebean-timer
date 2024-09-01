package jni.keyboard;

import timer.Timer;

public class KeyboardHookJni {
    static {
        //System.load("C:/Users/dheld/Desktop/timeBeanTest/timeBeanTest/libKeyboardHook.dll");
        System.loadLibrary("libKeyboardHook");
    }

    public native void startKeyboardHook();
    public native void stopKeyboardHook();

    public void handleKeyEvent(boolean isKeyDown) {
        if (isKeyDown) {
            System.out.println("Key pressed in JNI/C code");
            Timer.resetCloseSeconds();
        } else {
            System.out.println("Key released in JNI/C code");
            Timer.resetCloseSeconds();
        }
    }
}
