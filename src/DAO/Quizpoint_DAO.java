package DAO;

import DB.DBConnection;
import DTO.Member_DTO;
import DTO.Quizpoint_DTO;
import Interface.Quizpoint_DAO_Implements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Quizpoint_DAO implements Quizpoint_DAO_Implements {  //데이터베이스 퀴즈점수 테이블을 관리하기 위한 DAO
    private Connection ct = DBConnection.getCt();
    private String sql = null;
    private PreparedStatement pt;
    private ResultSet rs;


    public Quizpoint_DAO() throws SQLException {
    }

    public void gameover(String id, String name, String subject, int point) throws SQLException {
        sql = "insert into quizpoint values(?,?,?,?)";
        pt = ct.prepareStatement(sql);
        pt.setString(1, id);
        pt.setString(2, name);
        pt.setString(3, subject);
        pt.setInt(4, point);
        pt.executeUpdate();
    }

    public ArrayList<Quizpoint_DTO> print_quizpoint(String subject) throws SQLException {
        ArrayList<Quizpoint_DTO> data = new ArrayList<>();
        sql = "select * from quizpoint where subject = '"+subject+"' order by point desc;";
        pt = ct.prepareStatement(sql);
        rs = pt.executeQuery();
        while (rs.next()){
            Quizpoint_DTO d = new Quizpoint_DTO();
            d.setId(rs.getString("id"));
            d.setName(rs.getString("name"));
            d.setSubject(rs.getString("subject"));
            d.setPoint(rs.getInt("point"));
            data.add(d);
        }
        return data;
    }

    public boolean quizpoint_check(String id, String subject) throws SQLException {
        sql = "select * from quizpoint where id = '"+id+"' and subject = '"+subject+"';";
        pt = ct.prepareStatement(sql);
        rs = pt.executeQuery();
        if (rs.next()){
            return false;
        }
        return true;
    }

    public ArrayList<Quizpoint_DTO> all_user() throws SQLException {
        ArrayList<Quizpoint_DTO> list = new ArrayList<>();
        sql = "select * from quizpoint;";
        pt = ct.prepareStatement(sql);
        rs = pt.executeQuery();
        while (rs.next()){
            Quizpoint_DTO m = new Quizpoint_DTO();
            m.setId(rs.getString("id"));
            m.setName(rs.getString("name"));
            m.setSubject(rs.getString("subject"));
            m.setPoint(rs.getInt("point"));
            list.add(m);
        }
        return list;
    }

    @Override
    public int deletepoint(Object id, Object subject) throws SQLException {
        String sql = "delete from quizpoint where id = '"+id+"' and subject = '"+subject+"';";
        pt = ct.prepareStatement(sql);
        return pt.executeUpdate();
    }
}
