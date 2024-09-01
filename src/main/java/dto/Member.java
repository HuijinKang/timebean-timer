package dto;

public class Member {
    private long id;
    private String nickname;
    private String timerPassword;
    private String recordedDate;

    public Member(long id, String nickname, String timerPassword, String recordedDate) {
        this.id = id;
        this.nickname = nickname;
        this.timerPassword = timerPassword;
        this.recordedDate = recordedDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTimerPassword() {
        return timerPassword;
    }

    public void setTimerPassword(String timerPassword) {
        this.timerPassword = timerPassword;
    }

    public String getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(String recordedDate) {
        this.recordedDate = recordedDate;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + nickname + '\'' +
                ", password='" + timerPassword + '\'' +
                ", regDate='" + recordedDate + '\'' +
                '}';
    }
}
