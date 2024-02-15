package DAO;

import DB.DBConnection;
import DTO.Member_DTO;
import Interface.Member_DAO_Implements;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Member_DAO implements Member_DAO_Implements {  //데이터베이스 멤버 테이블을 관리하기 위한 DAO
    private Connection ct = DBConnection.getCt();
    private String sql = null;
    private PreparedStatement pt;
    private ResultSet rs;

    public Member_DAO() throws SQLException {
    }

    public void insert_M(Member_DTO m) throws SQLException {
        try{
            pt = ct.prepareStatement("insert into member values(?,?,?)");
            pt.setString(1, m.getId());
            pt.setString(2, m.getPassword());
            pt.setString(3, m.getName());
            pt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String member_check(String id, String password) throws SQLException {
        sql = "select * from member where id = '"+id+"' and password = '"+password+"';";
        pt = ct.prepareStatement(sql);
        rs = pt.executeQuery();
        String name = null;
        if (rs.next()){
            name = rs.getString("name");
        }
        return name;
    }
    public boolean member_check(String id) throws SQLException {
        sql = "select * from member where id = '"+id+"';";
        pt = ct.prepareStatement(sql);
        rs = pt.executeQuery();
        if (rs.next()){
            return false;
        }
        else {
            return true;
        }
    }

    public Member_DTO user_seesoin(String id) throws SQLException {
        Member_DTO m = new Member_DTO();
        sql = "select * from member where id = '"+id+"';";
        pt = ct.prepareStatement(sql);
        rs = pt.executeQuery();
        while (rs.next()){
            m.setId(rs.getString("id"));
            m.setPassword(rs.getString("password"));
            m.setName(rs.getString("name"));
        }
        return m;
    }

    @Override
    public void member_modify(String name, String id) throws SQLException {
        sql = "update member set name = '"+name+"' where id = '"+id+"';";
        pt = ct.prepareStatement(sql);
        pt.executeUpdate();
        sql = "update quizpoint set name = '"+name+"' where id = '"+id+"';";
        pt = ct.prepareStatement(sql);
        pt.executeUpdate();
    }


}
