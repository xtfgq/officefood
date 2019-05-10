package com.xxx.ency.model.bean;



public class CheckListBean {
    /**
     * 检测父标题
     */
    String parentTitle;
    String parentTip;

    public String getParentTip() {
        return parentTip;
    }

    public void setParentTip(String parentTip) {
        this.parentTip = parentTip;
    }

    /**
     * 子标题
     */
    String childTitle;
    /**
     * 提示语
     */
    String childHints;
    String name;
    /**
     * 父检测项目
     */
    String checkId;
    /**
     * 检测项目
     */
    String id;
    /**
     * 知识库系统
     */
    String tip;

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
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

    /**
     * 范围或者输入内容限制规则
     */
    String[] strValues;
    String[] stritems;

    public String[] getStritems() {
        return stritems;
    }

    public void setStritems(String[] stritems) {
        this.stritems = stritems;
    }

    /**
     * 范围是数字还是文本
     *1，描述
     * 2，单选
     * 4，多选
     * 5.表示是否有异常，
     */
    int type;
    /**
     * 显示内容
     */
    String showText;

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String[] getStrValues() {
        return strValues;
    }

    public void setStrValues(String[] strValues) {
        this.strValues = strValues;
    }

    public String getTxtValue() {
        return txtValue;
    }

    public void setTxtValue(String txtValue) {
        this.txtValue = txtValue;
    }

    String txtValue;

    public String getParentTitle() {
        return parentTitle;
    }

    public void setParentTitle(String parentTitle) {
        this.parentTitle = parentTitle;
    }

    public String getChildTitle() {
        return childTitle;
    }

    public void setChildTitle(String childTitle) {
        this.childTitle = childTitle;
    }

    public String getChildHints() {
        return childHints;
    }

    public void setChildHints(String childHints) {
        this.childHints = childHints;
    }
}
