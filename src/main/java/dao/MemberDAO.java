package dao;

import dto.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDAO {
    private final Connection connection;

    public MemberDAO() {
        this.connection = new DBConnection().getConnection();
    }

    public Member searchMember(String nickname) {
        Member member = null;

            String sql = "select * from member where nickname = ?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, nickname); // nickname 파라미터를 쿼리에 설정

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        long id = rs.getLong("id");
                        String memberNickname = rs.getString("nickname");
                        String timerPassword = rs.getString("timer_password");
                        String createdDate = rs.getString("created_date");

                        member = new Member(id, memberNickname, timerPassword, createdDate);
                        System.out.println(memberNickname + ", " + timerPassword + ", " + createdDate);
                    }
                } catch (Exception e) {
                    System.out.println("Error while processing ResultSet: " + e);
                }
            } catch (Exception e) {
                System.out.println("Error executing PreparedStatement: " + e);
            }

        return member;
    }
}
