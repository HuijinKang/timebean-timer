package dto;

public class Member {
    private long id;
    private String nickname;
    private String timerPassword;
    private String createdDate;

    public Member(long id, String nickname, String timerPassword, String createdDate) {
        this.id = id;
        this.nickname = nickname;
        this.timerPassword = timerPassword;
        this.createdDate = createdDate;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + nickname + '\'' +
                ", password='" + timerPassword + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
