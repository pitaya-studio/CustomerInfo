package com.trekiz.admin.modules.log.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * T1搜索日志
 * @author chao.zhang
 * @date 2016-09-29
 */
@Entity
@Table(name = "search_log")
public class SearchLog {
		private Long id;
		private String message;//搜索内容
		private String productType;//产品类型
		private String bugetType;//境内境外类型 100000:出境游 200000：国内游
		private String searchType;//搜索类型
		//注意当为多条件时,此值是所有条件结合后产生的条数
		private Long count;//匹配到的总数
		private Integer createBy;//搜索人
		private Date createDate;//搜索时间
		//=========searchType==========
		//搜索输入框输入
		public static final String INPUT_TYPE = "1";
		//目的地
		public static final String TARGET_TYPE = "2";
		//出发城市
		public static final String FROM_TYPE = "3";
		//抵达城市
		public static final String  ARRIVAL_TYPE = "4";
		//供应商supply
		public static final String  SUPPLY_TYPE = "5";
		//出团日期
		public static final String  OPENDATE_TYPE = "6";
		//行程天数
		public static final String  DAYCOUNT_TYPE = "7";
		//价格区间
		public static final String PRICE_TYPE = "8";
		//余位
		public static final String SEAT_TYPE = "9";
		
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id", unique = true, nullable = false)
		public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}
		
		 @Column(name="message", nullable = false)
		public String getMessage() {
			return message;
		}
		 
		public void setMessage(String message) {
			this.message = message;
		}
		
		@Column(name="productType", nullable = false)
		public String getProductType() {
			return productType;
		}
		
		public void setProductType(String productType) {
			this.productType = productType;
		}
		
		 @Column(name="bugetType", nullable = false)
		public String getBugetType() {
			return bugetType;
		}
		 
		public void setBugetType(String bugetType) {
			this.bugetType = bugetType;
		}
		
		@Column(name="searchType", nullable = false)
		public String getSearchType() {
			return searchType;
		}
		
		public void setSearchType(String searchType) {
			this.searchType = searchType;
		}
		
		 @Column(name="count", nullable = false)
		public Long getCount() {
			return count;
		}
		 
		public void setCount(Long count) {
			this.count = count;
		}
		
		@Column(name="createBy", nullable = false)
		public Integer getCreateBy() {
			return createBy;
		}
		
		public void setCreateBy(Integer createBy) {
			this.createBy = createBy;
		}
		
		@Column(name="createDate", nullable = false)
		public Date getCreateDate() {
			return createDate;
		}
		
		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}
		
		
}
