package com.xxx.ency.model.bean;

import java.io.Serializable;
import java.util.List;

public class CmsResult implements Serializable {
    private int rtnCode;

    public int getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(int rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getRtnMsg() {
        return rtnMsg;
    }

    public void setRtnMsg(String rtnMsg) {
        this.rtnMsg = rtnMsg;
    }

    private List<Res> res;

    public List<Res> getRes() {
        return res;
    }

    public void setRes(List<Res> res) {
        this.res = res;
    }

    private String rtnMsg;

    public static class Res {


        private String id;
        private String title;
        private String img;
        private String datetime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }
    }
}
