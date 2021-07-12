package cn.lynu.model;

import java.io.Serializable;

public class Xzpf implements Serializable {

    private static final long serialVersionUID = 3195222382746908145L;

    private Integer id;

    private Integer yansouteamId;

    private String teacherId;

    private String studentId;

    private Integer score;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYansouteamId() {
        return yansouteamId;
    }

    public void setYansouteamId(Integer yansouteamId) {
        this.yansouteamId = yansouteamId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
