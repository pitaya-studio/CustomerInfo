package com.trekiz.admin.review.rebates.singleGroup.vo;

/**
 * 返佣对象vo
 * @author yanzhenxing
 * @date 2016/1/18
 */
public class RebatesObjectVo {
    //返佣对象类型：渠道、供应商,默认为渠道
    private String rebatesObjectType;
    //返佣对象id
    private String  rebatesObjectId;
    //返佣对象名称
    private String 	rebatesObjectName;
    //返佣对象账号id
    private String  rebatesObjectAccountId;
    //返佣对象账号类型：境内、境外
    private String  rebatesObjectAccountType;
    //返佣对象账号开户行名称
    private String  rebatesObjectAccountBank;
    //返佣对象账号号码
    private String  rebatesObjectAccountCode;

    public RebatesObjectVo() {
    }

    public String getRebatesObjectType() {
        return rebatesObjectType;
    }

    public void setRebatesObjectType(String rebatesObjectType) {
        this.rebatesObjectType = rebatesObjectType;
    }

    public String getRebatesObjectId() {
        return rebatesObjectId;
    }

    public void setRebatesObjectId(String rebatesObjectId) {
        this.rebatesObjectId = rebatesObjectId;
    }

    public String getRebatesObjectName() {
        return rebatesObjectName;
    }

    public void setRebatesObjectName(String rebatesObjectName) {
        this.rebatesObjectName = rebatesObjectName;
    }

    public String getRebatesObjectAccountId() {
        return rebatesObjectAccountId;
    }

    public void setRebatesObjectAccountId(String rebatesObjectAccountId) {
        this.rebatesObjectAccountId = rebatesObjectAccountId;
    }

    public String getRebatesObjectAccountType() {
        return rebatesObjectAccountType;
    }

    public void setRebatesObjectAccountType(String rebatesObjectAccountType) {
        this.rebatesObjectAccountType = rebatesObjectAccountType;
    }

    public String getRebatesObjectAccountBank() {
        return rebatesObjectAccountBank;
    }

    public void setRebatesObjectAccountBank(String rebatesObjectAccountBank) {
        this.rebatesObjectAccountBank = rebatesObjectAccountBank;
    }

    public String getRebatesObjectAccountCode() {
        return rebatesObjectAccountCode;
    }

    public void setRebatesObjectAccountCode(String rebatesObjectAccountCode) {
        this.rebatesObjectAccountCode = rebatesObjectAccountCode;
    }
}
