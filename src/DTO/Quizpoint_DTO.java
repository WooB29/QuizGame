package DTO;

public class Quizpoint_DTO {    //퀴즈 점수를 관리하기 위한 DTO클래스
    private String id;
    private String name;
    private String subject;
    private int point;

    public Quizpoint_DTO(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
