package com.example.healthyplus.Model;

import java.io.Serializable;
import java.util.Map;

public class Conservation implements Serializable {
    private String id;
    private String uid;
    private String eid;
    private Map<String, Object> chatData;

    public Conservation(){}
    public Conservation(String id, String uid, String eid, Map<String, Object> chatData) {
        this.id = id;
        this.uid = uid;
        this.eid = eid;
        this.chatData = chatData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public Map<String, Object> getChatData() {
        return chatData;
    }

    public void setChatData(Map<String, Object> chatData) {
        this.chatData = chatData;
    }

    public void addChatData(String time, String chatId) {
        this.chatData.put(time, chatId);
    }

    @Override
    public String toString() {
        return "Conservation{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", eid='" + eid + '\'' +
                ", chatData=" + chatData +
                '}';
    }
}
