/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.input;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.preferential.entity.PreferentialDict;
import com.trekiz.admin.modules.preferential.entity.PreferentialDictUnitRel;


/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class PreferentialDictInput {
	//表单传递的参数
	private java.lang.String uuid;
	private java.lang.String name;//字典名称
	private java.lang.Integer dataType;//数据类型。0数值，1日期
	private java.lang.Integer type;//因果类型。0因，1果
	private java.lang.Integer relationalOperator;//关系运算符0：>;1:>=;2:<;3<=;4:=;5:!=
	private java.lang.String logicOperationUuid;//逻辑运算配置表UUID
	private java.lang.String[] unitUuids;//单位uuid数组
	
	private PreferentialDict dict ;
	public PreferentialDictInput(){
	}
	public PreferentialDictInput(PreferentialDict dict ){
		BeanUtil.copySimpleProperties(this, dict);
	}
	public PreferentialDictInput(PreferentialDict dict ,List<PreferentialDictUnitRel> dictUnitList){
		List<String> uuids = new ArrayList<String>();
		for(PreferentialDictUnitRel rel:dictUnitList){
			uuids.add(rel.getUnitUuid());
		}
		unitUuids= new String[uuids.size()];
		uuids.toArray(unitUuids);
		BeanUtil.copySimpleProperties(this, dict);
	}
	
	public PreferentialDict getDict() {
		dict = new PreferentialDict();
		BeanUtil.copySimpleProperties(dict, this);
		return dict;
	}
	public PreferentialDict getDict(PreferentialDict dict) {
		BeanUtil.copySimpleProperties(dict, this);
		return dict;
	}

	public java.lang.String getUuid() {
		return uuid;
	}
	public void setUuid(java.lang.String uuid) {
		this.uuid = uuid;
	}
	public java.lang.String getName() {
		return name;
	}
	public void setName(java.lang.String name) {
		this.name = name;
	}
	public java.lang.Integer getDataType() {
		return dataType;
	}
	public void setDataType(java.lang.Integer dataType) {
		this.dataType = dataType;
	}
	public java.lang.Integer getType() {
		return type;
	}
	public void setType(java.lang.Integer type) {
		this.type = type;
	}
	public java.lang.Integer getRelationalOperator() {
		return relationalOperator;
	}
	public void setRelationalOperator(java.lang.Integer relationalOperator) {
		this.relationalOperator = relationalOperator;
	}
	public java.lang.String getLogicOperationUuid() {
		return logicOperationUuid;
	}
	public void setLogicOperationUuid(java.lang.String logicOperationUuid) {
		this.logicOperationUuid = logicOperationUuid;
	}

	public java.lang.String[] getUnitUuids() {
		return unitUuids;
	}
	public java.lang.String getUnitUuidsString() {
		return ArrayUtils.toString(unitUuids);
	}
	public void setUnitUuids(java.lang.String[] unitUuids) {
		this.unitUuids = unitUuids;
	}
	
	
	
	
}

