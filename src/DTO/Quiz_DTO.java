package DTO;

public class Quiz_DTO {     //퀴즈 관리를 위한 DTO클래스
    private int num;
    private String subject;
    private String title;
    private String contemt;

    public Quiz_DTO(){}

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContemt() {
        return contemt;
    }

    public void setContemt(String contemt) {
        this.contemt = contemt;
    }

}

