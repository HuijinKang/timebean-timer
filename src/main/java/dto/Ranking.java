package dto;

public class Ranking {
    private long id;
    private long memberId;
    private int totalTime;
    private String recordedDate;

    public Ranking(long memberId, int totalTime, String recordedDate) {
        this.memberId = memberId;
        this.totalTime = totalTime;
        this.recordedDate = recordedDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public String getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(String recordedDate) {
        this.recordedDate = recordedDate;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", time=" + totalTime +
                ", regDate='" + recordedDate + '\'' +
                '}';
    }
}
