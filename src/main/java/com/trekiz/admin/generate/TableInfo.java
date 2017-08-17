package com.trekiz.admin.generate;

/**
 *  文件名: Generate.java 【Class TableInfo】
 *  功能:
 *  表相关信息对象
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2013-11-28 上午10:55:57
 *  @version 1.0
 */
class TableInfo {
    private String columnName;
    private String comments;
    private String dataType;
    private String maxlength;//CHARACTER_MAXIMUM_LENGTH
    private String minlength;//如果allowNumll 为YES  表示 0   为NO  则按照 1处理
    private String allowNull;//IS_NULLABLE
    
    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public String getMaxlength() {
        return maxlength;
    }
    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }
    public String getMinlength() {
        return minlength;
    }
    public void setMinlength(String minlength) {
        this.minlength = minlength;
    }
    public String getAllowNull() {
        return allowNull;
    }
    public TableInfo(String columnName, String comments, String dataType,
            String maxlength, String minlength, String allowNull) {
        super();
        this.columnName = columnName;
        this.comments = comments;
        this.dataType = dataType;
        this.maxlength = maxlength;
        this.minlength = minlength;
        this.allowNull = allowNull;
    }
    public void setAllowNull(String allowNull) {
        this.allowNull = allowNull;
    }
}
