package Interface;

import DTO.Quiz_DTO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//퀴즈 DAO 관한 인터페이스
public interface Quiz_DAO_Implements {

    // 퀴즈를 추가하기 위한 메소드
    public void insert_q(Quiz_DTO q);

    // 게임시 퀴즈중 랜덤으로 퀴즈를 가져와 출력하는 메소드
    public List<Quiz_DTO> randomselect(String subject) throws SQLException;

    // 퀴즈 목록을 불러오기 위한 메소드
    public ArrayList<Quiz_DTO> print_quiz(String subject) throws SQLException;

    // 퀴즈를 제거하기 위한 메소드
    public int delete_quiz(Object title) throws SQLException;

    // 퀴즈 수정을 위한 메소드
    public int update_quiz(Object title, String colum, String value) throws SQLException;

    // 퀴즈 추가시 중복제거를 위한 메소드
    public boolean quizname_check(String name) throws SQLException;
}
