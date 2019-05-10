package com.xxx.ency.view.send;

public class SendMessage {
//    "sendUserId": "42",
//            "receiveUserId": "2",
//            "title": "你好8",
//            "content": "欢迎"
    String sendUserId;
    String receiveUserId;
    String title;
    String content;

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
