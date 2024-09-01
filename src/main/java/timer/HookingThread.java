package timer;

import jni.keyboard.KeyboardHookJni;
import jni.mouse.MouseHookJni;

public class HookingThread {
    private KeyboardHookJni keyboardHookJni;
    private MouseHookJni mouseHookJni;
    private Thread keyboardThread;
    private Thread mouseThread;

    // 키보드 후킹 시작
    public void keyboardHooking() {
        keyboardHookJni = new KeyboardHookJni();

        keyboardThread = new Thread(() -> {
            keyboardHookJni.startKeyboardHook();
        });

        keyboardThread.start();
    }

    // 마우스 후킹 시작
    public void mouseHooking() {
        mouseHookJni = new MouseHookJni();

        mouseThread = new Thread(() -> {
            mouseHookJni.startMouseHook();
        });

        mouseThread.start();
    }

    // 키보드 후킹 종료
    public void stopKeyboardHook() {
        if (keyboardHookJni != null) {
            keyboardHookJni.stopKeyboardHook(); // Assuming a method to stop the hook
        }
    }

    // 마우스 후킹 종료
    public void stopMouseHook() {
        if (mouseHookJni != null) {
            mouseHookJni.stopMouseHook(); // Assuming a method to stop the hook
        }
    }
}