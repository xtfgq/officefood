package com.xxx.ency.model.bean;

public class PostBrow {

    String userId;
    String type;
    String grainStoreRoomId;
    String pageSize;
    String currentPage;

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
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

    public String getGrainStoreRoomId() {
        return grainStoreRoomId;
    }

    public void setGrainStoreRoomId(String grainStoreRoomId) {
        this.grainStoreRoomId = grainStoreRoomId;
    }
}
