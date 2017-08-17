/**
 *
 */
package com.trekiz.admin.common.persistence;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

/**
 * Entity支持类
 * @author zj
 * @version 2013-11-19
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	// 显示/隐藏
	public static final String SHOW = "1";
	public static final String HIDE = "0";
	
	// 是/否
	public static final String YES = "1";
	public static final String NO = "0";

	// 删除标记（0：正常；1：删除；2：审核；）
	public static final String DEL_FLAG = "delFlag";
	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";
	public static final String DEL_FLAG_AUDIT = "2";
	public static final String DEL_FLAG_TO_GENERATE = "4";//待生成订单，用于散拼优惠
	public static final String DEL_FLAG_NOT_GENERATE = "5";//未生成订单，用于散拼优惠

	//优先扣减（系统常量：1控票数2非控票数）
	public static final Integer PRIORITY_CONTROL = 1;
	public static final Integer PRIORITY_UNCONTROL = 2;
}
