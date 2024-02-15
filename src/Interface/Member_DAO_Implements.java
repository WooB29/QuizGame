package Interface;

import DTO.Member_DTO;

import java.sql.SQLException;

// 멤버 DAO 인터페이스
public interface Member_DAO_Implements {

    // 회원가입시 db에 넣는 메소드
    public void insert_M(Member_DTO m)throws SQLException;

    // 로그인시 db에 해당 유저 확인 메소드
    public String member_check(String id, String password) throws SQLException;

    // 아이디 중복체크 확인 메소드
    public boolean member_check(String id) throws SQLException;

    // 로그인한 유저 정보를 가지고 가기 위한 메소드
    public Member_DTO user_seesoin(String id) throws SQLException;

    // 관리창에서 유저 정보 수정을 위한 메소드
    public void member_modify(String value, String id) throws SQLException;
}
