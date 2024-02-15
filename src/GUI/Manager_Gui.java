package GUI;

import DAO.Member_DAO;
import DAO.QuizList_DAO;
import DAO.Quiz_DAO;
import DAO.Quizpoint_DAO;
import DTO.Quiz_DTO;
import DTO.Quizpoint_DTO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Manager_Gui extends JFrame implements ActionListener {
    Member_DAO m_dao = new Member_DAO();
    Quiz_DAO q_dao = new Quiz_DAO();
    Quizpoint_DAO qp_dao = new Quizpoint_DAO();
    QuizList_DAO ql_dao = new QuizList_DAO();
    private JPanel ui;
    private JLabel title;
    private JButton user;
    private JButton quiz;
    private JButton logout;


    private JPanel user_panel;
    private DefaultTableModel u_tmodel;
    private JTable user_table;

    private JPanel userbtn;
    private JButton user_modify;
    private JButton user_delete;


    private JPanel quiz_panel;
    private JTabbedPane tab;
    private DefaultTableModel[] dt;
    private JTable[] tables;

    private JPanel quizbtn;
    private JButton quiz_add;
    private JButton quiz_modify;
    private JButton quiz_delete;

    public Manager_Gui() throws SQLException {

        ui = new JPanel();

        title = titlemake(title,"관리자 페이지");

        user = makebtn(user,"유 저",80,30);
        quiz = makebtn(quiz,"퀴 즈",80,30);
        logout = makebtn(logout,"로그아웃",100,30);
        ui.add(user);
        ui.add(quiz);
        ui.add(logout);

        user_panel = new JPanel();
        String[] user_title = {"아이디","이름","종류","점수"};
        ArrayList<Quizpoint_DTO> u_list = qp_dao.all_user();
        u_tmodel = new DefaultTableModel(user_title,0);
        for (int i = 0; i<u_list.size(); i++){
            String id = u_list.get(i).getId();
            String name = u_list.get(i).getName();
            String subject = u_list.get(i).getSubject();
            int point = u_list.get(i).getPoint();
            Object[] user_data = {id,name,subject,point};
            u_tmodel.addRow(user_data);
        }
        user_table = new JTable(u_tmodel){
            public boolean isCellEditable(int row, int colum){
                return false;
            }
        };
        user_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        user_table.setPreferredSize(new Dimension(600,500));
        user_table.setFont(new Font("Serif",Font.PLAIN,15));
        user_table.setRowHeight(30);
        JScrollPane u_sp = new JScrollPane(user_table);
        u_sp.setPreferredSize(new Dimension(620,500));
        user_panel.add(u_sp);
        ui.add(user_panel);

        userbtn = new JPanel();
        user_modify = makebtn(user_modify,"이름 수정",110,35);
        user_delete = makebtn(user_delete,"점수 삭제",110,35);
        userbtn.add(user_modify);
        userbtn.add(user_delete);
        ui.add(userbtn);


        quiz_panel = new JPanel();
        tab = new JTabbedPane(JTabbedPane.TOP);
        List<String> gamecart = ql_dao.choicelist();
        dt = new DefaultTableModel[gamecart.size()];
        String[] top = {"번호","정답","내용"};
        for (int i = 0; i < gamecart.size(); i++) {
            dt[i] = new DefaultTableModel(top,0);
            List<Quiz_DTO> list = q_dao.print_quiz(gamecart.get(i));
            for (int j = 0; j<list.size(); j++){
                int num = list.get(j).getNum();
                String title = list.get(j).getTitle();
                String contemt = list.get(j).getContemt();
                Object[] gamedata = {num,title,contemt};
                dt[i].addRow(gamedata);
            }
        }
        tables = new JTable[gamecart.size()];
        JScrollPane[] sp = new JScrollPane[gamecart.size()];
        for (int i = 0; i< tables.length; i++){
            tables[i] = new JTable(dt[i]){
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };
            tables[i].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tables[i].setPreferredSize(new Dimension(600,500));
            tables[i].setFont(new Font("Serif",Font.PLAIN,15));
            tables[i].setRowHeight(25);
            tables[i].getColumn("번호").setPreferredWidth(5);
            tables[i].getColumn("정답").setPreferredWidth(10);
            tables[i].getColumn("내용").setPreferredWidth(250);
            sp[i] = new JScrollPane(tables[i]);
            sp[i].setPreferredSize(new Dimension(620,500));
        }
        for (int i = 0; i<gamecart.size(); i++){
            tab.addTab(gamecart.get(i),sp[i]);
        }
        quiz_panel.add(tab);
        ui.add(quiz_panel);

        quizbtn = new JPanel();
        quiz_add = makebtn(quiz_add,"추 가",90,35);
        quiz_modify = makebtn(quiz_modify,"수 정",90,35);
        quiz_delete = makebtn(quiz_delete,"삭 제",90,35);

        quizbtn.add(quiz_add);
        quizbtn.add(quiz_modify);
        quizbtn.add(quiz_delete);
        ui.add(quizbtn);


        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        dtcr.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel u_tcm = user_table.getColumnModel();
        for (int i = 0; i< u_tcm.getColumnCount(); i++){
            u_tcm.getColumn(i).setCellRenderer(dtcr);;
        }
        TableColumnModel[] tcm = new TableColumnModel[tables.length];
        for (int i = 0; i< tables.length; i++){
            tables[i].getTableHeader().setResizingAllowed(false);
            tcm[i] = tables[i].getColumnModel();
            for (int j =0; j<tcm[i].getColumnCount(); j++){
                tcm[i].getColumn(j).setCellRenderer(dtcr);
            }
            tables[i].getTableHeader().setReorderingAllowed(false);
            tables[i].getTableHeader().setResizingAllowed(false);
        }
        user_table.getTableHeader().setReorderingAllowed(false);
        user_table.getTableHeader().setResizingAllowed(false);


        btnaction(new JButton[]{user,quiz,logout,user_modify,user_delete,quiz_add,quiz_modify,quiz_delete});


        add(ui);
        lastsetting(3,7,"관리자 페이지",700,800);
        quiz_panel.setVisible(false);
        quizbtn.setVisible(false);
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
    public JLabel titlemake(JLabel label, String name){
        label = new JLabel(name);
        label.setFont(new Font("Serif", Font.BOLD, 30));
        label.setPreferredSize(new Dimension(300,50));
        ui.add(label);
        return label;
    }
    public JButton makebtn(JButton btn, String name, int w, int h){
        btn = new JButton(name);
        btn.setPreferredSize(new Dimension(w,h));
        btn.setFont(new Font("굴림체",Font.BOLD,15));
        return btn;
    }
    public void btnaction(JButton[] btn) {
        for (int i = 0; i < btn.length; i++) {
            btn[i].addActionListener(this);
        }
    }
    public void show(String message){
        JOptionPane.showMessageDialog(null,message);
    }
    public int yes_no_show(String message, String title){
        return JOptionPane.showConfirmDialog(null,message,title,JOptionPane.YES_NO_OPTION);
    }
    // 입력을 할 수 있는 알림창 메소드 -> 메세지 내용 매개변수 받기
    public String input_show(String message){
        // 리턴값 - 사용자 입력
        return JOptionPane.showInputDialog(null,message);
    }
    public String select_show(){
        String[] menu = {"num", "title", "contemt"};
        String select = (String) JOptionPane.showInputDialog(null, "수정하실 목록을 선택해주세요.", "목록 선택", JOptionPane.WARNING_MESSAGE, null, menu, menu[0]);
        return select;
    }

    public void user_Action(Object user) throws SQLException{
        Object id = null;    // 아이디값을 가져올 Object value 선언
        Object subject = null;   // 퀴즈종류를 가져올 Object num 선언
        int row = user_table.getSelectedRow();  // 선택된 row 가지고오기
        try{
            id = user_table.getValueAt(row,0); // 선택된 0번째 열(id) 가져오기
            subject = user_table.getValueAt(row,2); // 선택된 2번째 열(종류) 가져오기
        }
        catch (ArrayIndexOutOfBoundsException ae){
            show("선택된 유저가 없습니다.");  // 선택된 항목이 없습니다.
        }
        if (id != null){    // 가져온 아이디가 있을 경우
            try {
                if (user == user_modify) {  // 클릭된 버튼이 수정버튼일 경우
                    String name = input_show("변경할 이름 입력");  //변경할 이름 입력
                    if (name != null || !(name.isEmpty())) {    // 이름이 비어있지 않은 경우
                        m_dao.member_modify(name, (String) id); // DB수정 명령
                        new Manager_Gui();  // 창 새로고침
                        dispose();
                    }
                }
            }
            catch (NullPointerException e){ // 취소 후 변경이름이 null일 경우
                show("취소되었습니다.");   // 취소 알림창 메소드 호출
            }
            if (user == user_delete){   // 클릭된 버튼이 삭제버튼일 경우
                // 삭제 확인 알림창 메소드 호출 및 리턴값 받기
                int check = yes_no_show(id+" - "+subject+"점수를 삭제하시겠습니까?","점수 삭제");
                if (check == 0){    // 알림창에서 확인을 클릭했을 경우
                    int d_check = qp_dao.deletepoint(id,subject); // DB 삭제후 확인 리턴값 받기
                    if (d_check == 1){  // 값이 1이 넘어올 경우
                        show(id+" - "+subject+"점수가 삭제되었습니다."); // 삭제 알림창 문구 메소드 호출
                        new Manager_Gui();  // 창 새로고침
                        dispose();
                    }
                }
            }
        }
    }

    public void quiz_Action(Object quiz) throws SQLException{
        Object value = null;    // 아이디값을 가져올 Object value 선언
        int seltab = tab.getSelectedIndex(); // 선택된 퀴즈룰 가지고 오기 위한 인덱스 번호 가져오기
        int row = tables[seltab].getSelectedRow();  // 선택된 row 가지고오기
        try{
            value = tables[seltab].getValueAt(row,1); // 선택된 1번째 퀴즈 정답 가져오기
        }catch (ArrayIndexOutOfBoundsException ae){
            show("선택된 퀴즈가 없습니다.");}  // 선택된 항목이 없습니다.
        if (value != null){ // 가져온 항목이 있을 경우
            if (quiz == quiz_modify) {  // 퀴즈 수정 버튼을 클릭했을 경우
                int sel_check = yes_no_show("해당 퀴즈를 수정하시겠습니까?", "퀴즈 수정");
                if (sel_check == 0) {   // 수정확인 알림창에서 예를 클릭했을 경우
                    String select = select_show();  // 수정항목 선택 메소드 호출 및 결과값 반환
                    if (select != null) {   // 결과값이 들어있을 경우
                        boolean check = true;   // 입력값 확인을 위해 boolean check 선언
                        String modify_content = input_show("수정할 내용 입력"); // 수정내용 입력 및 결과값 반환 메소드
                        if (modify_content == null || modify_content.isEmpty()) {   // 수정내용이 비어있을경우
                            show("잘못된 입력값 입니다.");   // 알림창 메소드 호출
                            check = false;  // check를 false로 변경
                        }
                        if (check) {    // check가 true 일경우
                            int update_check = q_dao.update_quiz(value, select, modify_content); // DB 수정 명령
                            if (update_check == 1) {    // 명령이 실행되었을 경우
                                show("해당 퀴즈가 수정되었습니다.");    // 알림창 메소드 호출
                                new Manager_Gui();  // 새로고침
                                dispose();  // 전에 페이지 닫기
                            }
                        }
                    }
                }
            }
            if (quiz == quiz_delete) { // 퀴즈 수정 버튼을 클릭했을 경우
                int delete_check = yes_no_show("해당 퀴즈 삭제하시겠습니까?","퀴즈 삭제");
                if (delete_check == 0) { // 확인을 클릭했을 경우
                    int d_check = q_dao.delete_quiz(value); // DB 삭제 명령
                    if (d_check == 1) { // 명령이 실행되었을 경우
                        show(value + "- 해당 퀴즈가 삭제되었습니다."); // 알림창 메소드 호출
                        new Manager_Gui();  // 새로고침
                        dispose();  // 전에 페이지 닫기
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==user){
            user_panel.setVisible(true);
            userbtn.setVisible(true);
            quiz_panel.setVisible(false);
            quizbtn.setVisible(false);
        }
        if (e.getSource()==quiz){
            user_panel.setVisible(false);
            userbtn.setVisible(false);
            quiz_panel.setVisible(true);
            quizbtn.setVisible(true);
        }
        if (e.getSource()==logout){
            int check = yes_no_show("로그아웃 하시겠습니까?","로그아웃");
            if (check == 0){
                try {
                    new First_Gui();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        }
        if (e.getSource()==user_modify || e.getSource()==user_delete) {
            try {
                user_Action(e.getSource());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getSource()==quiz_add){
            String quizname = null;
            String[] title = {"리스트 추가","퀴즈 내용 추가"} ;
            int choice = JOptionPane.showOptionDialog(null,"선택해주세요","선택",0,0,null,title,title[0]);
            if (choice==0){
                quizname = input_show("퀴즈 제목 입력");
                boolean overlab;
                try {
                    overlab = ql_dao.quizname_check(quizname);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if (quizname == null || quizname.isEmpty() || quizname.length()>10 || overlab){
                    show("잘못된 이름입니다.");
                }
                else {
                    int addsel;
                    try {
                        addsel = ql_dao.add_quizlist(quizname);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (addsel == 1){
                        show("생성완료");
                        try {
                            new Manager_Gui();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        dispose();
                    }
                }
            }
            if (choice == 1){
                try {
                    new AddQuiz_Gui();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        }

        if (e.getSource()==quiz_modify || e.getSource()==quiz_delete){
            try {
                quiz_Action(e.getSource());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}