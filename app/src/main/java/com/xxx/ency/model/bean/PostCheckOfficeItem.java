package com.xxx.ency.model.bean;

public class PostCheckOfficeItem {
    String userId;
    String isDeal;
    String pageSize;
    String currentPage;
    String grainStoreRoomid;
    String result;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getGrainStoreRoomid() {
        return grainStoreRoomid;
    }

    public void setGrainStoreRoomid(String grainStoreRoomid) {
        this.grainStoreRoomid = grainStoreRoomid;
    }

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

    public String getIsDeal() {
        return isDeal;
    }

    public void setIsDeal(String isDeal) {
        this.isDeal = isDeal;
    }
}