package com.xxx.ency.model.bean;

public class CheckBean {
    /**
     * 检测子项目
     */
    String title;
    /**
     * 检测上线值
     */
    String maxValue;
    /**
     * 检测下线值
     *
     */
    String minValue;
    /**
     * 检测内容
     */
    String content;
    /**
     * 提示语
     */
    String hint;
    String checkid;

    public String getCheckid() {
        return checkid;
    }

    public void setCheckid(String checkid) {
        this.checkid = checkid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    String id;
    String name;
    String value;


    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getCheckID() {
        return checkID;
    }

    public void setCheckID(String checkID) {
        this.checkID = checkID;
    }

    /**
     *
     * 检测id
     */
    String checkID;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
