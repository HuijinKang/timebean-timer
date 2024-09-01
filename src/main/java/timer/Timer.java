package timer;

import dao.MemberDAO;
import dao.RankingDAO;
import dto.Member;
import dto.Ranking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

public class Timer extends JFrame {
    private JLabel timerLabel;
    private static java.util.Timer timer;
    private int hours;
    private int minutes;
    private int seconds;
    private static int closeSeconds;
    private int totalSeconds;
    static final HookingThread hookingThread = new HookingThread();
    final MemberDAO memberDAO = new MemberDAO();
    final RankingDAO rankingDAO = new RankingDAO();

    public Timer() {
        setTitle("time_bean_beta-1.0");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // BorderLayout으로 변경

        timerLabel = new JLabel("0:00:00", SwingConstants.CENTER); // 중앙 정렬 설정
        add(timerLabel, BorderLayout.CENTER); // 타이머 레이블은 중앙에 배치

        JPanel buttonPanel = new JPanel(new FlowLayout()); // 버튼을 담을 JPanel 생성
        add(buttonPanel, BorderLayout.SOUTH); // 버튼 패널을 아래쪽에 배치

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTimer();
            }
        });
        buttonPanel.add(startButton); // 시작 버튼을 버튼 패널에 추가

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStopConfirmationDialog(); // 정지 확인 대화 상자 표시
            }
        });
        buttonPanel.add(stopButton); // 정지 버튼을 버튼 패널에 추가

        timer = new java.util.Timer();

        // 프레임을 화면 중앙에 배치
        setLocationRelativeTo(null);
    }

    public void startTimer() {
        //stopTimer();
        timer = new java.util.Timer();
        resetCloseSeconds();

        hookingThread.keyboardHooking();
        hookingThread.mouseHooking();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (closeSeconds > 0) {
                    seconds++;
                    totalSeconds++;
                    if (seconds == 60) {
                        seconds = 0;
                        minutes++;
                        if (minutes == 60) {
                            minutes = 0;
                            hours++;
                        }
                    }
                    closeSeconds--;
                    updateTimerLabel();
                }
            }
        };

        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
    }

    public static void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        hookingThread.stopKeyboardHook();
        hookingThread.stopMouseHook();
    }

    public static void resetCloseSeconds() {
        closeSeconds = 3;
    }

    public void updateTimerLabel() {
        String timeString = String.format("%d:%02d:%02d", hours, minutes, seconds);
        timerLabel.setText(timeString);
    }

    public void showStopConfirmationDialog() {
        stopTimer();

        JTextField nicknameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Nickname:"));
        panel.add(nicknameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int option = JOptionPane.showConfirmDialog(null, panel, "Enter Nickname and Password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String nickname = nicknameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars); // char[]를 String으로 변환


            Member member = memberDAO.searchMember(nickname);

            if (member == null) {
                JOptionPane.showMessageDialog(null, "Incorrect Nickname. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                showStopConfirmationDialog();
            }

            if (member != null && password.equals(member.getTimerPassword())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String today = sdf.format(Calendar.getInstance().getTime());

                rankingDAO.addOrUpdateRank(new Ranking(member.getId(), totalSeconds, today));

                totalSeconds = 0;

                // 저장되었음을 알리는 메시지 표시
                JOptionPane.showMessageDialog(null, "기록이 추가되었습니다.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else if (member != null && !password.equals(member.getTimerPassword())) {
                JOptionPane.showMessageDialog(null, "Incorrect password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                showStopConfirmationDialog();
            }
        }
    }
}
