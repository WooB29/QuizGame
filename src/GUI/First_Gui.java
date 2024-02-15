package GUI;

import DAO.Member_DAO;
import Security.AES;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class First_Gui extends JFrame implements ActionListener{
    AES aes = new AES();
    Member_DAO m_dao = new Member_DAO();
    private JLabel title;
    private JLabel idlable;
    private JLabel passlable;
    private JTextField idfield;
    private JTextField passfield;
    private JButton signinbtn;
    private JButton signupbtn;
    private JPanel ui;
    private String sessionId;

    public First_Gui() throws SQLException, IOException {

        ui = new JPanel();
        ui.setLayout(null);

        title = titlemake(title,"Quiz Game",new int[]{110,20,300,50});

        idlable = labelmake(idlable,"아 이 디",120);
        idfield = textmake(idfield,120);

        passlable = labelmake(passlable,"패 스 워 드",180);
        passfield = new JPasswordField(10);
        passfield.setBounds(162,180,130,30);
        ui.add(passfield);

        signinbtn = makebtn(signinbtn,"로그인",new int[]{80,260,70,40});
        signupbtn = makebtn(signupbtn,"회원가입",new int[]{220,260,100,40});

        add(ui);    // 패널을 화면에 추가하기

        btnaction(new JButton[]{signinbtn,signupbtn});
        lastsetting(3,5,"로그인",400,400);
    }

    // gui 세팅을 위한 메소드
    public void lastsetting(int ga, int se, String name, int size_w, int size_h){
        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();    //해상도 가져오기
        Dimension framesize = getSize();    // 프레임창 크기를 미멘션으로 가져오기
        //해상도와 프레임길이를 뺀뒤 1/2 작업
        setLocation((scr.width-framesize.width)/ga,(scr.height-framesize.height)/se);
        setTitle(name); // gui 프레임창 타이틀 이름
        setSize(size_w,size_h); // 프레임창 크기 설정
        setResizable(false);    //  프레임창 크기조절 불가 설정
        setVisible(true);   // 창이 눈에 보이게 하기 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창이 꺼지면 프로그램도 종료 설정
    }

    // 타이틀 라벨 만들기 위한 메소드 매개변수로 라벨, 이름, 위치 받기
    public JLabel titlemake(JLabel label, String name, int[]num){
        label = new JLabel(name);   // 라벨 선언
        label.setBounds(num[0],num[1],num[2],num[3]);   // 라벨의 위치 설정
        // 라벨 폰트 및 크기 조절
        label.setFont(new Font("Serif", Font.BOLD, 35));
        ui.add(label);  // 패널에 라벨 추가 설정
        return label;   // 라벨 반환
    }

    // 일반 라벨을 만들기 위한 메소드 매개변수로 라벨, 이름, 위치의 y값 받기
    public JLabel labelmake(JLabel label, String name, int y){
        label = new JLabel(name);   // 라벨 선언
        // 라벨 폰트 크기 조절
        label.setFont(new Font("Serif", Font.BOLD, 15));
        label.setBounds(70,y,122,30);   // 라벨의 위치 설정
        ui.add(label);  // 패널에 라벨 추가 설정
        return label;   // 라벨 반환
    }

    // 텍스트필드를 만들기 위한 메소드 -> JTextField, 위치의 y값 매개변수 받기
    public JTextField textmake(JTextField text,int y){
        text = new JTextField(10);  // 텍스트 라벨 선언
        text.setBounds(162,y,130,30);   // 위치 설정
        ui.add(text);   // 패널에 추가 설정
        return text;    // 텍스트필드 반환
    }

    // 버튼 만들기 위한 메소드 ->  JButton, 이름, 위치값 배열 매개변수 받기
    public JButton makebtn(JButton btn, String name, int[]num){
        btn = new JButton(name);    // 버튼 선언
        btn.setBounds(num[0],num[1],num[2],num[3]); // 받은 매개변수로 버튼 위치 설정
        btn.setBackground(Color.BLACK); //버튼의 배경색 설정
        btn.setForeground(Color.white); // 버튼의 글자색 설정
        ui.add(btn);    // 패널에 추가 설정
        return btn; // 버튼 반환
    }

    // 버튼에 액션을 주기 위한 메소드 ->  JButton 배열 매배변수 받기
    public void btnaction(JButton[] btn){
        for (int i =0; i< btn.length; i++){ // 받은 버튼들의 숫자만큼 돌면서
            btn[i].addActionListener(this); // 버튼에 액션을 넣는다.
        }
    }
    // 알림창 메소드 -> 메세지 내용 매개변수 받기
    public void show(String message){
        JOptionPane.showMessageDialog(null, message);
    }



    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void loginCheck(){
        String id = idfield.getText();      //아이디 입력창에 입력한 값 받아오기
        String pass = passfield.getText();  // 비밀번호 입력창에 입력한 값 받아오기
        String name=null;       // 사용자 확인 체크를 위해 String 선언
        //관리자 로그인을 위한 설정
        if (id.equals("admin") && pass.equals("123456")){
            show("관리자 로그인 성공"); // 알림창 메소드 띄우기
            try {
                new Manager_Gui();  // 관리자 화면 이동
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            dispose();  //현재페이지 닫기
        }
        // 입력한 비밀번호를 DB와 비교하기 위해 암호화 작업
        pass = aes.encrypt_AES(passfield.getText());
        try {
            // 아이디 확인후 DB에 있으면 name에 해당 사용자 이름 넣어주기
            name = m_dao.member_check(id,pass);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        if (name != null){  //아이디 값이 db에 있어서 name에 값이 들어갔을때
            show("환영합니다."+name+"님"); // 알림창 메소드 띄우기
            setSessionId(id);    // 로그인한 id 입력받아오기
            try {
                // 메인화면 이동(로그인한 아이디 넘겨주기)
                new Main_Gui(getSessionId());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            dispose();  //현재창 닫기
        }
        else {
            show("회원정보가 일치하지 않습니다."); // 알림창 메소드 띄우기
        }

    }

    @Override
    public void actionPerformed(ActionEvent e){ //ActionListener 오버라이딩 -> 액션이 일어났을때 할일
        if (e.getSource() == signupbtn){    // 만약 눌려진 값이 회원가입 버튼일 때
            try {
                new Signup_Gui();   // 회원가입 gui 이동
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            dispose();  // 현재 열려져 있던 창 닫기
        }
        if (e.getSource() == signinbtn){    // 만약 눌려진 버튼이 로그인 버튼일 때
            loginCheck();
        }
    }
}
