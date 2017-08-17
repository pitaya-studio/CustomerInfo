package com.trekiz.admin.common.persistence;

import java.util.List;


 /**
 *  文件名: Pager.java
 *  功能:
 *      页面pojo  不提供数据处理功能
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2013-12-3 上午11:50:59
 *  @version 1.0
 *  @param <T>
 */
public class Pager<T> {
    
    public Pager(String pagenumber, String pagecount, String pageRecordNum,String recordNum,
            List<T> list) {
        super();
        this.pagenumber = pagenumber;
        this.pagecount = pagecount;
        this.pageRecordNum = pageRecordNum;
        this.recordNum = recordNum;
        this.list = list;
    }

    public Pager(){};
    private String pagenumber;
    
    private String pagecount;
    
    private String pageRecordNum;
    
    private String recordNum;
    
    public String getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(String recordNum) {
        this.recordNum = recordNum;
    }
    private List<T> list;// 数据对象
    
    public String getPagenumber() {
        return pagenumber;
    }
    
    public void setPagenumber(String pagenumber) {
        this.pagenumber = pagenumber;
    }
    
    public String getPagecount() {
        return pagecount;
    }
    
    public void setPagecount(String pagecount) {
        this.pagecount = pagecount;
    }
    
    public String getPageRecordNum() {
        return pageRecordNum;
    }
    
    public void setPageRecordNum(String pageRecordNum) {
        this.pageRecordNum = pageRecordNum;
    }
    
    public List<T> getList() {
        return list;
    }
    
    public void setList(List<T> list) {
        this.list = list;
    }
    
}
