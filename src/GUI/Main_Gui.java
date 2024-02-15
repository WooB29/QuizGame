package GUI;

import DAO.Member_DAO;
import DAO.QuizList_DAO;
import DAO.Quiz_DAO;
import DAO.Quizpoint_DAO;
import DTO.Member_DTO;
import DTO.Quizpoint_DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class Main_Gui extends JFrame implements ActionListener {
    Member_DAO m_dao = new Member_DAO();
    Quiz_DAO q_dao = new Quiz_DAO();
    Quizpoint_DAO qt_dao = new Quizpoint_DAO();
    QuizList_DAO ql_dao = new QuizList_DAO();

    private JPanel ui;
    private JPanel menu_ui;
    private JLabel myname;
    private JButton logout;

    private JPanel center;
    private JTabbedPane tab;


    private JPanel footer;
    private Choice gamelist;
    private JButton gamestart;
    Member_DTO m;
    public Main_Gui(String id) throws SQLException {
        m = m_dao.user_seesoin(id);
        ui = new JPanel(new BorderLayout());

        menu_ui = new JPanel(new GridLayout(1,3,60,0));
        menu_ui.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
        myname = new JLabel(m.getName()+"님");
        myname.setFont(new Font("Serif",Font.BOLD,20));
        myname.setHorizontalAlignment(JLabel.CENTER);
        JLabel black = new JLabel();
        logout = makebtn(logout,"로그아웃",14);
        menu_ui.add(myname);
        menu_ui.add(black);
        menu_ui.add(logout);

        center = new JPanel();
        List<String> gamecart = ql_dao.choicelist();
        tab = make_Table(tab, gamecart);
        center.add(tab);

        footer = new JPanel();
        gamelist = new Choice();
        gamelist.setFont(new Font("Serif",Font.BOLD,20));
        for (int i =0; i<gamecart.size(); i++){
            gamelist.addItem(gamecart.get(i));
        }
        footer.add(gamelist);
        gamestart = makebtn(gamestart,"게임 시작",20);
        footer.add(gamestart);

        ui.add(menu_ui, BorderLayout.NORTH);
        ui.add(center, BorderLayout.CENTER);
        ui.add(footer, BorderLayout.SOUTH);
        add(ui);

        btnaction(new JButton[]{logout,gamestart});

        lastsetting(3,5,"메인 화면",450,550);
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
    public JButton makebtn(JButton btn, String name, int num){
        btn = new JButton(name);
        btn.setFont(new Font("Serif",Font.BOLD,num));
        btn.setHorizontalAlignment(JButton.CENTER);
        return btn;
    }
    public void table_setting(JTable[] tables){
        // DefaultTableCellRenderer 선언
        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        // 중앙정렬 설정
        dtcr.setHorizontalAlignment(SwingConstants.CENTER);
        // 정렬할 테이블의 컬럼모델을 가져오기
        TableColumnModel[] tcm = new TableColumnModel[tables.length];
        for (int i = 0; i< tables.length; i++){ // 모든 테이블 반복 하면서 중앙정렬
            tables[i].getTableHeader().setResizingAllowed(false);
            tcm[i] = tables[i].getColumnModel();
            for (int j =0; j<tcm[i].getColumnCount(); j++){
                tcm[i].getColumn(j).setCellRenderer(dtcr);
            }
            // 테이블 이동불가 설정
            tables[i].getTableHeader().setReorderingAllowed(false);
            tables[i].getTableHeader().setResizingAllowed(false);
        }
    }
    public void btnaction(JButton[] btn){
        for (int i =0; i< btn.length; i++){
            btn[i].addActionListener(this);
        }
    }
    // Yes와 NO 옵션을 선택할 수 있는 알림창 메소드 -> 메세지 내용과 제목 매개변수 받기
    public int yes_no_show(String message,String title){
        // 리턴값 -  Yes -> 0 / No -> 1 / 팝업 종료 -> -1
        return JOptionPane.showConfirmDialog(null,message,title,JOptionPane.YES_NO_OPTION);
    }

    public JTabbedPane make_Table(JTabbedPane table, List<String> gamecart) throws SQLException {
        // gamecart = ql_dao.choicelist() -> 데이터베이스의 게임리스트를 String List 형식으로 불러오기
        table = new JTabbedPane(JTabbedPane.TOP);   // 여러 패널을 담는 테이블팬(상단) 선언
        // 추가삭제 가능한 DefaultTableModel 배열 선언
        DefaultTableModel[] dt = new DefaultTableModel[gamecart.size()];
        String[] title = {"id","이름","점수"};  // 상단 제목 배열 설정
        for (int i = 0; i < gamecart.size(); i++) { // 게임 종류 수 만큼 반복하기
            dt[i] = new DefaultTableModel(title,0); // 상단 제목 설정
            // 데이터베이스에서 퀴즈 점수를 DTO List 형식으로 불러오기
            List<Quizpoint_DTO> list = qt_dao.print_quizpoint(gamecart.get(i));
            for (int j = 0; j<list.size(); j++){    // 리스트 사이즈 만큼 반복
                String aid = list.get(j).getId();   // 리스트의 아이디
                String aname = list.get(j).getName();   // 리스트의 이름
                int apoint = list.get(j).getPoint();    // 리스트의 점수
                Object[] gamedata = {aid,aname,apoint}; // 오브젝트 배열로 저장
                dt[i].addRow(gamedata); // 테이블에 addRow를 통한 추가 작업
            }
        }
        JTable[] tables = new JTable[gamecart.size()];  // 게임 종류 수 테이블 배열 생성
        JScrollPane[] sp = new JScrollPane[gamecart.size()];    // 테이블 스크롤 생성
        for (int i = 0; i< tables.length; i++){ // 테이블의 길이만큼 반복
            tables[i] = new JTable(dt[i]){  // DefaultTableModel를 삽입한 테이블 배열 생성
                public boolean isCellEditable(int row, int column){ // 테이블모델 인터페이스 선언 및 재정의
                    return false; // 테이블 값 수정변경 불가
                }
            };
            tables[i].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);    // 테이블 컬럼길이 변경 불가
            //테이블 세팅작업
            tables[i].setPreferredSize(new Dimension(350,300));
            tables[i].setFont(new Font("Serif",Font.PLAIN,17));
            tables[i].setRowHeight(25);
            //테이블 스크롤 배열 생성
            sp[i] = new JScrollPane(tables[i]);
            sp[i].setPreferredSize(new Dimension(370,300));
        }
        for (int i = 0; i<gamecart.size(); i++){
            table.addTab(gamecart.get(i),sp[i]);    // (타이틀,스크롤들)을 테이블팬에 추가
        }
        table_setting(tables);  // 테이블 글자 중앙정렬, 이동불가 메소드 호출
        return table;   //만든 테이블 리턴
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logout){
            int logoutcheck = yes_no_show("로그아웃 하시겠습니까?","로그아웃");
            if (logoutcheck==0){
                try {
                    new First_Gui();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        }
        if (e.getSource() == gamestart){
            String gameslect = gamelist.getSelectedItem();
            boolean point_check;
            int gamestartcheck = yes_no_show(gameslect+" 게임을 시작하시겠습니까?","게임시작");
            if (gamestartcheck == 0){
                try {
                    point_check = qt_dao.quizpoint_check(m.getId(),gameslect);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if (point_check){
                    try {
                        new Game_Gui(gameslect,m.getId());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(null,"이미 게임을 완료했습니다.");
                }
            }
        }
    }
}