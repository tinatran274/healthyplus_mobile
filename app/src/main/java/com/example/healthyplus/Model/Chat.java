package com.example.healthyplus.Model;

public class Chat {

    private String content;
    private String time;
    private int sender;

    public Chat(String content, String time, int sender) {
        this.content = content;
        this.time = time;
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", sender=" + sender +
                '}';
    }
}
