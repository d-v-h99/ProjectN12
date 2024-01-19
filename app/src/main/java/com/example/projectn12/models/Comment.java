package com.example.projectn12.models;

public class Comment {
    private String content, uid, uname, timestamp;

    public Comment() {
    }

    public Comment(String content, String uid, String uname, String timestamp) {
        this.content = content;
        this.uid = uid;
        this.uname = uname;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
