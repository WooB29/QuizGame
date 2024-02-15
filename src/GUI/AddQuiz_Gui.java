package GUI;

import DAO.QuizList_DAO;
import DAO.Quiz_DAO;
import DTO.Quiz_DTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class AddQuiz_Gui extends JFrame implements ActionListener {
    QuizList_DAO ql_dao = new QuizList_DAO();
    Quiz_DAO q_dao = new Quiz_DAO();
    private JPanel ui;

    private JLabel title;

    private JLabel subject_l;
    private Choice subject_c;

    private JLabel num_l;
    private JTextField num_t;

    private JLabel title_l;
    private JTextField title_t;
    private JButton title_c;

    private JLabel contemt_l;
    private JTextArea contemt_t;

    private JButton add;
    private JButton cancel;
    boolean check;

    public AddQuiz_Gui() throws SQLException {
        List<String> list = ql_dao.choicelist();
        ui = new JPanel();
        ui.setLayout(null);

        title = titlemake(title,"퀴 즈 추 가",new int[]{120,5,300,50});

        subject_l = labelmake(subject_l,"주 제",100);
        subject_c = new Choice();
        subject_c.setFont(new Font("Serif",Font.BOLD,15));
        subject_c.setBounds(120,100,130,30);
        for (int i =0; i<list.size(); i++){
            subject_c.add(list.get(i));
        }
        ui.add(subject_c);

        num_l = labelmake(num_l,"번 호",150);
        num_t = textmake(num_t,150);

        title_l = labelmake(title_l,"정 답",200);
        title_t = textmake(title_t,200);
        title_c = makebtn(title_c,"중복 확인", new int[]{270,200,100,30});

        contemt_l = labelmake(contemt_l,"내 용",250);
        contemt_t = new JTextArea();
        contemt_t.setLineWrap(true);
        contemt_t.setBounds(120,250,200,60);
        ui.add(contemt_t);

        add = makebtn(add,"추 가", new int[]{60,370,100,40});
        cancel = makebtn(cancel,"취 소", new int[]{230,370,100,40});
        add(ui);

        btnaction(new JButton[]{title_c,add,cancel});

        lastsetting(3,5,"퀴즈 추가",400,500);
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

    public JLabel titlemake(JLabel label, String name, int[]num){
        label = new JLabel(name);
        label.setBounds(num[0],num[1],num[2],num[3]);
        label.setFont(new Font("Serif", Font.BOLD, 35));
        ui.add(label);
        return label;
    }

    public JLabel labelmake(JLabel label, String name, int y){
        label = new JLabel(name);
        label.setFont(new Font("Serif", Font.BOLD, 15));
        label.setBounds(30,y,90,30);
        ui.add(label);
        return label;
    }
    public JTextField textmake(JTextField text,int y){
        text = new JTextField(10);
        text.setBounds(120,y,130,30);
        ui.add(text);
        return text;
    }
    public JButton makebtn(JButton btn, String name, int[]num){
        btn = new JButton(name);
        btn.setBounds(num[0],num[1],num[2],num[3]);
        ui.add(btn);
        return btn;
    }
    public void btnaction(JButton[] btn){
        for (int i =0; i< btn.length; i++){
            btn[i].addActionListener(this);
        }
    }
    public void show(String message){
        JOptionPane.showMessageDialog(null,message);
    }
    public int yes_no_show(String message, String title){
        return JOptionPane.showConfirmDialog(null,message,title,JOptionPane.YES_NO_OPTION);
    }
    public void add_Quiz() throws SQLException{
        //boolean check = false
        //중복체크 했을경우 true 로 변경되어있다.
        if (check){
            // 입력받은 내용을 DTO에 넣어서 DB에 넣는 작업
            Quiz_DTO q = new Quiz_DTO();  // 퀴즈 DTO 선언
            q.setNum(Integer.parseInt(num_t.getText())); // 번호
            q.setSubject(subject_c.getSelectedItem());  // 주제
            q.setTitle(title_t.getText());  // 정답
            q.setContemt(contemt_t.getText());  // 문제
            q_dao.insert_q(q);  // DB에 입력내용을 넣어준 DTO를 가지고 insert 작업 실행
            // 추가등록 확인 알림창 메소드 호출
            int more = yes_no_show("더 등록하시겠습니까?","추가등록");
            if (more==0){   // 추가하겠다고 선택하였을 경우
                new AddQuiz_Gui(); // 새로고침
                dispose();  // 전에 창 닫기
            }
            else {
                new Manager_Gui();  // 매니저관리창으로 이동
                dispose();  // 전에 창 닫기
            }
        }
        else {  // check -> 중복확인이 되지않아 false일 경우
            show("중복 확인이 되지 않았습니다.");   // 알림창 메소드 호출
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == title_c){
            try {
                check = q_dao.quizname_check(title_t.getText());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            if (!check){
                show("사용 불가능");
            }
            else {
                show("사용 가능");
            }
        }
        if (e.getSource() == add){
            try {
                add_Quiz();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getSource() == cancel){
            try {
                new Manager_Gui();
                dispose();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
