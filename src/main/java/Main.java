import timer.Timer;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Timer timer = new Timer();
                timer.setVisible(true);
                timer.requestFocusInWindow(); // Request focus for key events
            }
        });
    }
}
