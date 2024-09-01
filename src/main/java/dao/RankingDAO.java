package dao;

import dto.Ranking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RankingDAO {
    private final Connection connection;

    public RankingDAO() {
        this.connection = new DBConnection().getConnection();
    }

    public void addRank(Ranking ranking) {
        String sql = "INSERT INTO ranking(member_id, total_time, recorded_date) VALUES(?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, ranking.getMemberId());
            ps.setInt(2, ranking.getTotalTime());
            ps.setString(3, ranking.getRecordedDate());
            // 구문 실행
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addOrUpdateRank(Ranking ranking) {
        String sql = "SELECT * FROM ranking WHERE member_id = ? AND DATE(recorded_date) = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            //오늘 날짜
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(Calendar.getInstance().getTime());

            ps.setLong(1, ranking.getMemberId());
            ps.setString(2, today);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // 동일한 이름의 레코드가 이미 존재할 경우
                int currentTime = rs.getInt("total_time");
                int newTime = currentTime + ranking.getTotalTime();

                // 기존 레코드 업데이트
                String updateQuery = "UPDATE ranking SET total_time = ?, recorded_date = CURRENT_TIMESTAMP WHERE member_id = ? AND DATE(recorded_date) = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setInt(1, newTime);
                updateStmt.setLong(2, ranking.getMemberId());
                updateStmt.setString(3, today);
                updateStmt.executeUpdate();
            } else {
                // 동일한 이름의 레코드가 존재하지 않을 경우
                String insertQuery = "INSERT INTO ranking(member_id, total_time, recorded_date) VALUES(?, ?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setLong(1, ranking.getMemberId());
                insertStmt.setInt(2, ranking.getTotalTime());
                insertStmt.setString(3, ranking.getRecordedDate());
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //오늘 날짜로 랭킹을 가져오는 로직 구현 예정
}
