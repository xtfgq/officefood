package com.xxx.ency.model.bean;

public class JobBean {
    /**
     * 作业日期
     */
    String data;

    /**
     * 作业内容标题
     */
    String title;
    /**
     * 作业id
     */
    String id;
    String type;
    String refuseCause;
    String name;
    String content;
    int status;
    String currentStep;

    public String getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //    if("1".equals(type)) {
//        jobBean1.setTitle("名称:"+object.getString("name") + "\n" +
//                "类型："+object.getString("type") + "\n"+
//                "委托人:" +
//                object.getString("realName"));
//    }else {
//        jobBean1.setTitle("名称:"+object.getString("name") + "\n" +
//                "类型："+object.getString("type") + "\n"+"受托人:" +
//                object.getString("toRealName"));
//    }
    public String getRefuseCause() {
        return refuseCause;
    }

    public void setRefuseCause(String refuseCause) {
        this.refuseCause = refuseCause;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String grainStoreRoomName;
    String strCreateDate;
    String areaName;
    String isAbnormal;
    String des;
    String createUserId;
    String repairTime;
    String repairUserId;
    String censorshipTime;
    String result;
    String sendUserId;
    String receiveUserId;

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }




    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCensorshipTime() {
        return censorshipTime;
    }

    public void setCensorshipTime(String censorshipTime) {
        this.censorshipTime = censorshipTime;
    }

    public String getCensorshipUserId() {
        return censorshipUserId;
    }

    public void setCensorshipUserId(String censorshipUserId) {
        this.censorshipUserId = censorshipUserId;
    }

    String censorshipUserId;
    int sDeali;

    public String getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(String repairTime) {
        this.repairTime = repairTime;
    }

    public String getRepairUserId() {
        return repairUserId;
    }

    public void setRepairUserId(String repairUserId) {
        this.repairUserId = repairUserId;
    }

    public int getsDeali() {
        return sDeali;
    }

    public void setsDeali(int sDeali) {
        this.sDeali = sDeali;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getAbnormalTypeName() {
        return abnormalTypeName;
    }

    public void setAbnormalTypeName(String abnormalTypeName) {
        this.abnormalTypeName = abnormalTypeName;
    }

    String abnormalTypeName;


    public String getStrCreateDate() {
        return strCreateDate;
    }

    public void setStrCreateDate(String strCreateDate) {
        this.strCreateDate = strCreateDate;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(String isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public String getGrainStoreRoomName() {
        return grainStoreRoomName;
    }

    public void setGrainStoreRoomName(String grainStoreRoomName) {
        this.grainStoreRoomName = grainStoreRoomName;
    }

    String grainStoreRoomId;
    String auditorUserRealName;
    String createUserRealName;

    String auditorUserId;

    public String getAuditorUserRealName() {
        return auditorUserRealName;
    }

    public void setAuditorUserRealName(String auditorUserRealName) {
        this.auditorUserRealName = auditorUserRealName;
    }

    public String getCreateUserRealName() {
        return createUserRealName;
    }

    public void setCreateUserRealName(String createUserRealName) {
        this.createUserRealName = createUserRealName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getAuditorUserId() {
        return auditorUserId;
    }

    public void setAuditorUserId(String auditorUserId) {
        this.auditorUserId = auditorUserId;
    }

    public String getGrainStoreRoomId() {
        return grainStoreRoomId;
    }

    public void setGrainStoreRoomId(String grainStoreRoomId) {
        this.grainStoreRoomId = grainStoreRoomId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
