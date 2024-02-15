package DAO;

import DB.DBConnection;
import Interface.QuizList_DAO_Implements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizList_DAO implements QuizList_DAO_Implements {  //데이터베이스 퀴즈리스트 테이블을 관리하기 위한 DAO
    private Connection ct = DBConnection.getCt();
    private String sql = null;
    private PreparedStatement pt;
    private ResultSet rs;

    public QuizList_DAO() throws SQLException {
    }

    public List<String> choicelist() throws SQLException {
        List<String> list = new ArrayList<>();
        sql = "select subject from quizlist;";
        pt = ct.prepareStatement(sql);
        rs = pt.executeQuery();
        while (rs.next()){
            String subject = rs.getString("subject");
            list.add(subject);
        }
        return list;
    }

    public int add_quizlist(String name) throws SQLException {
        sql = "insert into quizlist value(?)";
        pt = ct.prepareStatement(sql);
        pt.setString(1,name);
        return pt.executeUpdate();
    }

    public boolean quizname_check(String name) throws SQLException {
        sql = "select * from quizlist where subject = '"+name+"';";
        pt = ct.prepareStatement(sql);
        rs = pt.executeQuery();
        if (rs.next()){
            return true;
        }
        return false;
    }
}
