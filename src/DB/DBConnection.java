package DB;

import java.sql.*;

public class DBConnection { // 데이터베이스(DB)의 연결을 위한 클래스
    public static Connection getCt() throws SQLException {  // db 클래스의 싱글톤 작업
        Connection ct = null;
        ct = DriverManager.getConnection("jdbc:mysql://localhost:3306/quizgame","root","1234");
        return ct;
    }
}
