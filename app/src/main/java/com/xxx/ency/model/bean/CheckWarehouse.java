package com.xxx.ency.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CheckWarehouse {
    @Id(autoincrement = true)
    private Long id;
    /**
     * 用户id
     */
    private String userid;
    /**
     * 仓库编号
     */
    private String WarehouseNo;
    /**
     * 检测存储json
     */
    private String resultJson;
    /**
     * 检测日期
     */
    private String date;
    /**
     * 作业类型
     */
    private String type;
    @Generated(hash = 619865194)
    public CheckWarehouse(Long id, String userid, String WarehouseNo,
            String resultJson, String date, String type) {
        this.id = id;
        this.userid = userid;
        this.WarehouseNo = WarehouseNo;
        this.resultJson = resultJson;
        this.date = date;
        this.type = type;
    }
    @Generated(hash = 860640943)
    public CheckWarehouse() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserid() {
        return this.userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getWarehouseNo() {
        return this.WarehouseNo;
    }
    public void setWarehouseNo(String WarehouseNo) {
        this.WarehouseNo = WarehouseNo;
    }
    public String getResultJson() {
        return this.resultJson;
    }
    public void setResultJson(String resultJson) {
        this.resultJson = resultJson;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
