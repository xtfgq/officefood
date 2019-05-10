package com.xxx.ency.view.work;

public class PostAdd {
//    "createUserId": 1,
//            "grainStoreRoomId": 1,
//            "title": 1,
//            "checkId": 1,
//            "status": 1
    String createUserId;
    String grainStoreRoomId;
    String title;
    String type;
    String step;
    String auditorUserId;


    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String status;

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getGrainStoreRoomId() {
        return grainStoreRoomId;
    }

    public void setGrainStoreRoomId(String grainStoreRoomId) {
        this.grainStoreRoomId = grainStoreRoomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuditorUserId() {
        return auditorUserId;
    }

    public void setAuditorUserId(String auditorUserId) {
        this.auditorUserId = auditorUserId;
    }
}
