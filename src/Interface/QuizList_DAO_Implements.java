package Interface;

import java.sql.SQLException;
import java.util.List;

//퀴즈리스트 DAO 관한 인터페이스
public interface QuizList_DAO_Implements {

    // 퀴즈 리스트를 불러오기 위한 메소드
    public List<String> choicelist() throws SQLException;

    // 퀴즈 리스트에 목록 추가를 위한 메소드
    public int add_quizlist(String name) throws SQLException;

    // 퀴즈리스트에 목록 추가시 중복을 체크하기 위한 메소드
    public boolean quizname_check(String name) throws SQLException;
}
