package com.example.project1v1;

public class messages {
    String userid;
    String message;
    String time;

    public messages(String userid, String message, String time) {
        this.userid = userid;
        this.message = message;
        this.time = time;
    }

    public messages(){

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
