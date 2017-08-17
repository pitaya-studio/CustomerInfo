package com.trekiz.admin.modules.mtourCommon.transfer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.trekiz.admin.modules.mtourCommon.jsonbean.SupplierInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.SupplierInfoTypeJsonBean;
import com.trekiz.admin.modules.supplier.entity.SupplierInfo;
import com.trekiz.admin.modules.sys.entity.Dict;

public class SupplierTransfer {

	/**
	 * 后台对象转换成前端对象
	 * @param info
	 * @return
	 */
	public static SupplierInfoTypeJsonBean supplierInfo2InputJsonBean(Dict info){
		if(info!=null){
			SupplierInfoTypeJsonBean bean = new SupplierInfoTypeJsonBean();
			bean.setTourOperatorTypeCode(info.getUuid());
			bean.setTourOperatorTypeName(info.getLabel());
			return bean;
		}
		return null;
	}
	
	/**
	 * 后台集合转换成前端集合
	 * @param infoList
	 * @return
	 */
	public static List<SupplierInfoTypeJsonBean> supplierInfo2InputJsonBean(List<Dict> infoList){
		if(CollectionUtils.isNotEmpty(infoList)){
			List<SupplierInfoTypeJsonBean> beanList = new ArrayList<SupplierInfoTypeJsonBean>();
			
			for(Dict info:infoList){
				SupplierInfoTypeJsonBean bean = supplierInfo2InputJsonBean(info);
				if(bean!=null){
					beanList.add(bean);
				}
			}
			return beanList;
		}
		return null;
	}
	
	
	public static List<SupplierInfoJsonBean> supplierInfo2JsonBean(List<SupplierInfo> infoList){
		if(CollectionUtils.isNotEmpty(infoList)){
			List<SupplierInfoJsonBean> beanList = new ArrayList<SupplierInfoJsonBean>();
			for(SupplierInfo info : infoList){
				SupplierInfoJsonBean bean = new SupplierInfoJsonBean();
				bean.setTourOperatorUuid(info.getId().toString());
				bean.setTourOperatorName(info.getSupplierName());
				beanList.add(bean);
			}
			return beanList;
		}
		return null;
	}
}
