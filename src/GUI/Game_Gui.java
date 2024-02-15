package GUI;

import DAO.Member_DAO;
import DAO.Quiz_DAO;
import DAO.Quizpoint_DAO;
import DTO.Member_DTO;
import DTO.Quiz_DTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Game_Gui extends JFrame{
    Quiz_DAO q_dao = new Quiz_DAO();
    Member_DAO m_dao = new Member_DAO();
    Quizpoint_DAO qpoint_dao = new Quizpoint_DAO();

    private JPanel ui;

    private JPanel top_ui;
    private JLabel subject;
    private JLabel count_label;
    private int count=1;

    private JPanel content_p;
    private TextArea content;

    private JPanel timer_p;
    private JLabel timerNum;

    private JPanel center;
    private JTextField title;
    private JButton answer;

    private JPanel footer;
    private JLabel point_lable;
    private int point = 0;
    private int second = 100;
    private String gamesubject;
    Member_DTO m;

    public Game_Gui(String gamesubject, String id) throws SQLException {
        JOptionPane.showMessageDialog(null,"제한시간 = 100초\n총문제수 =  10문제\n시작합니다.");

        this.gamesubject = gamesubject;
        m = m_dao.user_seesoin(id);
        List<Quiz_DTO> q_dto = q_dao.randomselect(gamesubject);

        ui = new JPanel(new GridLayout(5,1));
        top_ui = new JPanel(new GridLayout(1,2,0,0));

        subject = titlemake(subject,gamesubject+" 퀴즈",35);
        count_label = labelmake(count_label,getCount()+"/ 10",20);
        top_ui.add(subject);
        top_ui.add(count_label);
        ui.add(top_ui);

        content_p = new JPanel(new GridLayout(1,1,0,0));
        content = new TextArea(q_dto.get(count-1).getContemt());
        content.setFont(new Font("Serif",Font.PLAIN,15));
        content.setEditable(false);
        content_p.add(content);
        ui.add(content_p);

        timer_p = new JPanel();
        timerNum = new JLabel();
        timer(timerNum);
        timer_p.add(timerNum);
        ui.add(timer_p);

        center = new JPanel();
        title = textmake(title,17);
        answer = makebtn(answer,"정답확인",15);
        center.add(title);
        center.add(answer);
        ui.add(center);


        footer = new JPanel(new GridLayout(1,2));
        point_lable = labelmake(point_lable,"점수 = "+getPoint()+"점",20);
        footer.add(point_lable);
        ui.add(footer);

        add(ui);
        lastsetting(3,5,"게임 화면",550,700);

        answer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String answer = title.getText();    // 입력한 정답 가져오기
                // List<Quiz_DTO> q_dto = q_dao.randomselect(gamesubject);
                // DB에서 랜덤으로 퀴즈를 가져와 DTO로 리스트화
                // 입력한 정답을 사전에 저장해놓은 dto의 값과 비교
                if (answer.equals(q_dto.get(count-1).getTitle())){
                    show("정답"); // 정답 알림창 메소드 호출
                    setPoint(getPoint()+10);    // 점수 10점 추가
                }
                else {
                    show("오답"); // 오답 알림창 메소드 호출
                }
                if (count==10){ // 총 10문제 다 풀었을경우
                    end_Game(); // 게임 종료 메소드 호출
                }
                setCount(getCount()+1); // 문제 번호 +1하기
                // 문제지를 다음 문제지로 변경
                content.setText(q_dto.get(count-1).getContemt());
                title.setText("");  // 정답입력칸 비우기
                count_label.setText(getCount()+"/ 10"); // 문제번호 다시세팅
                point_lable.setText("점수 = "+getPoint()+"점");  // 점수 다시 세팅
            }
        });
    }
    public void lastsetting(int ga, int se, String name, int size_w, int size_h){
        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension framesize = getSize();
        setLocation((scr.width-framesize.width)/ga,(scr.height-framesize.height)/se);
        setTitle(name);
        setSize(size_w,size_h);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public JLabel titlemake(JLabel label, String name, int num){
        label = new JLabel(name);
        label.setFont(new Font("Serif",Font.BOLD,num));
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }
    public JLabel labelmake(JLabel label, String name, int num){
        label = new JLabel(name);
        label.setFont(new Font("Serif",Font.BOLD,num));
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }
    public JTextField textmake(JTextField text,int y){
        text = new JTextField(15);
        text.setFont(new Font("Serif",Font.BOLD,y));
        text.setHorizontalAlignment(JTextField.CENTER);
        return text;
    }
    public JButton makebtn(JButton btn, String name, int num){
        btn = new JButton(name);
        btn.setFont(new Font("Serif",Font.BOLD,num));
        return btn;
    }
    public void show(String message){
        JOptionPane.showMessageDialog(null,message);
    }
    public void end_Game(){
        show("총점수 = "+getPoint()+"입니다.");
        try {
            qpoint_dao.gameover(m.getId(),m.getName(),gamesubject,getPoint());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        try {
            new Main_Gui(m.getId());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        dispose();
    }

    public void timer(JLabel timerNum){
        // 타이머 메소드, 적용시킬 라벨 변수 매개변수 받기
        // 타이머 쓰레드 클래스 생성
        Timer timer = new Timer();
        // int second = 100;
        // 적용시킬 매개변수 스타일 설정
        timerNum.setOpaque(true);
        timerNum.setForeground(Color.RED);
        timerNum.setText(second + "초");
        timerNum.setFont(new Font("Serif", Font.BOLD, 40));
        timerNum.setHorizontalAlignment(JLabel.CENTER);
        // 쓰레드 실행을 위한 추상클래스 선언 및 재정의
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() { //실행
                while (true) {  //반복
                    try {
                        Thread.sleep(1000);	// 1초단위
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (second > 0) {       // second가 0보다 클때
                        second -= 1;		// 1초씩 줄어들게 설정
                        timerNum.setText(second + "초");
                    } else {
                        end_Game(); // 0보다 작아지게 되면 종료 메소드 호출
                        break;  //반복문 종료
                    }
                }
            }
        };
        // 0.5초의 딜레이 후 재정의한 timerTask 실행
        timer.schedule(timerTask,500);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

}
