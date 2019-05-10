package com.xxx.ency.model.bean;

public class PostAreaManager {
    String areaId;
    String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    String userId;
    String abnormalType;

    public String getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(String isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    String isAbnormal;


    public String getAbnormalType() {
        return abnormalType;
    }

    public void setAbnormalType(String abnormalType) {
        this.abnormalType = abnormalType;
    }


    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }



    public void setUserId(String userId) {
        this.userId = userId;
    }
}
