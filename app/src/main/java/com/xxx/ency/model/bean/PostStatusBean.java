package com.xxx.ency.model.bean;

public class PostStatusBean {
//    "auditorUserId": 1,
//            "id": 1,
//            "operatorUserId": 1,
//            "status": 1}
    String auditorUserId;
    String status;
    String id;
    String refuseCause;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRefuseCause() {
        return refuseCause;
    }

    public void setRefuseCause(String refuseCause) {
        this.refuseCause = refuseCause;
    }

    public String getAuditorUserId() {
        return auditorUserId;
    }

    public void setAuditorUserId(String auditorUserId) {
        this.auditorUserId = auditorUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
