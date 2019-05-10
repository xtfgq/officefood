package com.xxx.ency.model.bean;

public class ServicePost {
    String userId;
    String isDeal;
    String pageSize;
    String currentPage;

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
}
