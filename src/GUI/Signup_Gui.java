package GUI;

import DAO.Member_DAO;
import DTO.Member_DTO;
import Security.AES;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.SQLException;

public class Signup_Gui extends JFrame implements ActionListener{
    AES aes = new AES();
    Member_DAO m_dao = new Member_DAO();
    private JPanel ui;

    private JLabel title;

    private JLabel idlable;
    private JTextField idfield;
    private JButton idcheck;

    private JLabel passlable;
    private JTextField passfield;

    private JLabel namelable;
    private JTextField namefield;

    private JButton signupbtn;
    private JButton cancelbtn;

    private boolean check;

    public Signup_Gui() throws SQLException {

        ui = new JPanel();
        ui.setLayout(null);

        title = titlemake(title,"회 원 가 입",new int[]{110,5,300,50});

        idlable = labelmake(idlable,"아 이 디",100);
        idfield = textmake(idfield,100);
        idcheck = makebtn(idcheck,"중복 확인", new int[]{270,100,90,35});

        passlable = labelmake(passlable,"패 스 워 드",150);
        passfield = textmake(passfield,150);


        namelable = labelmake(namelable,"이 름",200);
        namefield = textmake(namefield,200);


        signupbtn = makebtn(signupbtn,"가입 신청", new int[]{60, 300, 100, 40});
        cancelbtn = makebtn(cancelbtn,"취소", new int[]{230,300,100,40});


        add(ui);

        btnaction(new JButton[]{idcheck,signupbtn,cancelbtn});
        lastsetting(3,5,"회원 가입",400,430);
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
        label.setBounds(30,y,122,30);
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
    public void signup(){
        String id = idfield.getText();  //아이디 입력값 받아오기
        String pass = passfield.getText();  // 비밀번호 입력값 받아오기
        String name = namefield.getText();  // 이름 입력값 받아오기
        // boolean check -> 아이디 중복체크 안할 시 false
        if (check){ // 아이디 중복체크 통과시
            // 비밀번호와 이름 입력값이 비었을 경우
            if (pass.isEmpty() || name.isEmpty()){
                show("잘못된 입력 양식이 있습니다."); // 알림창 메소드 불러오기
            }
            else {
                //입력받은 비밀번호 DB에 넣기 위해 암호화 작업
                pass = aes.encrypt_AES(pass);
                // DTO 선언 및 입력 받은 값들 생성자로 넣어주기
                Member_DTO m = new Member_DTO(id,pass,name);
                try {
                    m_dao.insert_M(m);  // DB에 추가 메소드
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                show("회원가입 완료");    // 추가 완료 후 완료 메세지 알림창
                try {
                    new First_Gui();    // 첫화면 돌아가기
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                dispose();  // 현재창 닫기
            }
        }
        else {
            show("아이디 중복체크를 해주세요."); // 중복 확인 알림창 메소드
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == idcheck){
            String id = idfield.getText();
            boolean m_check;
            try {
                m_check = m_dao.member_check(id);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (id.equals("") || (id.length()>10)){
                show("아이디 양식이 잘못되었습니다.");
                check = false;
            }
            else {
                if (m_check){
                    show("사용 가능한 아이디입니다.");
                    check = true;
                }
                else {
                    show("아이디가 중복되었습니다.");
                    check = false;
                }
            }
        }
        if (e.getSource() == cancelbtn){
            try {
                new First_Gui();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        }
        if (e.getSource() == signupbtn){
            signup();
        }
    }
}
