package DAO;

import DB.DBConnection;
import DTO.Quiz_DTO;
import Interface.Quiz_DAO_Implements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Quiz_DAO implements Quiz_DAO_Implements {  //데이터베이스 쿠즈 테이블을 관리하기 위한 DAO
    private Connection ct = DBConnection.getCt();
    private String sql = null;
    private PreparedStatement pt;
    private ResultSet rs;

    public Quiz_DAO() throws SQLException {
    }

    @Override
    public void insert_q(Quiz_DTO q) {
        try{
            pt = ct.prepareStatement("insert into quiz values(?,?,?,?)");
            pt.setInt(1, q.getNum());
            pt.setString(2, q.getSubject());
            pt.setString(3, q.getTitle());
            pt.setString(4, q.getContemt());
            pt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Quiz_DTO> randomselect(String subject) throws SQLException {
        // 게임 실행시 데이터베이스에서 무작위로 게임 10문항을 가져오기 위한 메소드
        Random r = new Random();    // 랜덤 클래스 변수 r 선언
        int count = 0;  // 문항수를 넣을 int count 선언
        List<Quiz_DTO> list = new ArrayList<>();  // 추출한 퀴즈를 넣을 quiz DTO 리스트 선언
        // 선택한 주제의 전체 문항 수량을 가져오는 sql문 입력
        sql = "select count(*) from quiz where subject = '"+subject+"';";
        pt = ct.prepareStatement(sql);  // 만들어놓은 sql 구문 넣어서 db 명령 실행
        rs = pt.executeQuery();   // ResultSet rs 변수에 결과값 담기
        while (rs.next()){
            count += rs.getInt("count(*)");// 나온 결과값 count에 대입
        }
        int[] ram = new int[10];    // 10개의 숫자를 받을수있는 int 배열 선언
        for (int i =0; i<ram.length; i++) {
            ram[i] = r.nextInt(count) + 1;  // 1~count 까지의 숫자 랜덤 받기
            for (int j = 0; j < i; j++) {
                if (ram[i] == ram[j]) { // 숫자비교 -> 같은숫자를 받았다면
                    i--;    // 반복문 -1
                    break;
                }
            }
        }
        for (int i = 0; i<ram.length; i++){
            // 만든 랜덤 숫자와 퀴즈 주제의 번호와 같은 문제들을 가져오는 sql문 입력
            sql = "select * from quiz where subject = '"+subject+"' and num = "+ram[i]+";";
            pt = ct.prepareStatement(sql);
            rs = pt.executeQuery(); // ResultSet rs 변수에 결과값 담기
            while (rs.next()){ // dto에 번호, 주제, 정답, 문제들을 반복하며 넣기
                Quiz_DTO dto = new Quiz_DTO();  // 퀴즈 문제들을 넣을 DTO 선언
                dto.setNum(rs.getInt("num"));   // 번호
                dto.setSubject(subject);    // 주제
                dto.setTitle(rs.getString("title"));    // 정답
                dto.setContemt(rs.getString("contemt"));// 문제
                list.add(dto);  // 리스트에 dto 삽입
            }
        }
        return list;    // 리스트 리턴
    }

    public ArrayList<Quiz_DTO> print_quiz(String subject) throws SQLException {
        ArrayList<Quiz_DTO> data = new ArrayList<>();
        sql = "select * from quiz where subject = '"+subject+"';";
        pt = ct.prepareStatement(sql);
        rs = pt.executeQuery();
        while (rs.next()){
            Quiz_DTO d = new Quiz_DTO();
            d.setNum(rs.getInt("num"));
            d.setSubject(rs.getString("subject"));
            d.setTitle(rs.getString("title"));
            d.setContemt(rs.getString("contemt"));
            data.add(d);
        }
        return data;
    }

    @Override
    public int delete_quiz(Object title) throws SQLException {
        sql = "delete from quiz where title = '"+title+"';";
        pt = ct.prepareStatement(sql);
        return pt.executeUpdate();
    }

    @Override
    public int update_quiz(Object title, String colum, String value) throws SQLException {
        sql = "update quiz set "+colum+" = '"+value+"' where title = '"+title+"';";
        pt = ct.prepareStatement(sql);
        return pt.executeUpdate();
    }

    @Override
    public boolean quizname_check(String name) throws SQLException {
        sql = "select * from quiz where title = '"+name+"';";
        pt = ct.prepareStatement(sql);
        rs = pt.executeQuery();
        if (rs.next()){
            return false;
        }
        return true;
    }


}
