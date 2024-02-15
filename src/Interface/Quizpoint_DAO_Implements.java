package Interface;

import DTO.Quizpoint_DTO;
import java.sql.SQLException;
import java.util.ArrayList;

//퀴즈점수 DAO 관한 인터페이스
public interface Quizpoint_DAO_Implements {

    // 게임종료시 점수 결과값을 넣기 위한 메소드
    public void gameover(String id, String name, String subject, int point) throws SQLException;

    // 유저들의 퀴즈점수를 출력하기 위한 메소드
    public ArrayList<Quizpoint_DTO> print_quizpoint(String subject) throws SQLException;

    // 해당 게임 진행 확인을 위한 메소드
    public boolean quizpoint_check(String id, String subject) throws SQLException;

    // 모든 유저들의 퀴즈점수를 확인하기 위한 메소드
    public ArrayList<Quizpoint_DTO> all_user() throws SQLException;

    // 퀴즈점수 제거를 위한 메소드
    public int deletepoint(Object id, Object subject) throws SQLException;
}
