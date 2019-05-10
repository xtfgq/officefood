package com.xxx.ency.model.bean;

public class PostReaportDeatil {
//    {
//        "userId": "42",
//            "type": "1",
//            "grainStoreRoomId": "1",
//            "createDate": "2019-02-09"
//
//    }
    String userId;
    String type;
    String grainStoreRoomId;
    String createDate;
    String jobId;
    String step;

    public String getGrainStoreRoomId() {
        return grainStoreRoomId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGrainStoreRoomId(String roomId) {
        return grainStoreRoomId;
    }

    public void setGrainStoreRoomId(String grainStoreRoomId) {
        this.grainStoreRoomId = grainStoreRoomId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
