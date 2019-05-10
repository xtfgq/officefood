package com.xxx.ency.model.bean;

public class JobPostBean {
    String userId;

    public String getUserId() {
        return userId;
    }
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String status;
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



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
