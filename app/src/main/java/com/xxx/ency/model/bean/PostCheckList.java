package com.xxx.ency.model.bean;

import java.util.List;

public class PostCheckList {
    String userId;
    String grainStoreRoomId;
    String jobId;
    String currentStep;

    public String getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    List<checkItemValueList> checkItemValueList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGrainStoreRoomId() {
        return grainStoreRoomId;
    }

    public void setGrainStoreRoomId(String grainStoreRoomId) {
        this.grainStoreRoomId = grainStoreRoomId;
    }

    public void setCheckItemValueList(List<com.xxx.ency.model.bean.checkItemValueList> checkItemValueList) {
        this.checkItemValueList = checkItemValueList;
    }
}
