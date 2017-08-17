package com.trekiz.admin.modules.mtourCommon.transfer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.entity.SysDict;
import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;
import com.trekiz.admin.modules.mtourCommon.jsonbean.SysCompanyDictViewJsonBean;

public class SysCompanyDictViewTransfer {
	
	/**
	 * 字典数据库对象转为字典jsonbean
	     * <p>@Description TODO</p>
		 * @Title: dictBean2DictJsonBean
	     * @return SysCompanyDictViewJsonBean
	     * @author majiancheng       
	     * @date 2015-11-05 下午2:56:12
	 */
	public static SysCompanyDictViewJsonBean sysdefineDictBean2DictJsonBean(Sysdefinedict sysdefineDict) {
		if(sysdefineDict != null) {
			SysCompanyDictViewJsonBean jsonBean = new SysCompanyDictViewJsonBean();
			jsonBean.setId(sysdefineDict.getId().toString());
			jsonBean.setCode(sysdefineDict.getValue());
			jsonBean.setName(sysdefineDict.getLabel());
			jsonBean.setType(sysdefineDict.getType());
			jsonBean.setUuid(sysdefineDict.getUuid());
			return jsonBean;
		}
		
		return null;
	}
	
	/**
	 * 字典数据库对象集合转为字典jsonbean前端集合
	     * <p>@Description TODO</p>
		 * @Title: dictBeans2DictJsonBeans
	     * @return List<SysCompanyDictViewJsonBean>
	     * @author WangXK       
	     * @date 2015-11-05 下午2:59:56
	 */
	public static List<SysCompanyDictViewJsonBean> SysdefinedictBeans2DictJsonBeans(List<Sysdefinedict> sysdefinedict) {
		if(CollectionUtils.isNotEmpty(sysdefinedict)) {
			List<SysCompanyDictViewJsonBean> jsonBeans = new ArrayList<SysCompanyDictViewJsonBean>();
			for(Sysdefinedict dictView : sysdefinedict) {
				if(dictView != null) {
					jsonBeans.add(sysdefineDictBean2DictJsonBean(dictView));
				}
			}
			return jsonBeans;
		}
		
		return null;
	}
	
	/**
	 * 字典数据库对象转为字典jsonbean
	     * <p>@Description TODO</p>
		 * @Title: dictBean2DictJsonBean
	     * @return SysCompanyDictViewJsonBean
	     * @author majiancheng       
	     * @date 2015-10-14 下午2:56:12
	 */
	public static SysCompanyDictViewJsonBean dictBean2DictJsonBean(SysDict sysCompanyDictView) {
		if(sysCompanyDictView != null) {
			SysCompanyDictViewJsonBean jsonBean = new SysCompanyDictViewJsonBean();
			jsonBean.setId(sysCompanyDictView.getId().toString());
			jsonBean.setCode(sysCompanyDictView.getValue());
			jsonBean.setName(sysCompanyDictView.getLabel());
			jsonBean.setType(sysCompanyDictView.getType());
			jsonBean.setUuid(sysCompanyDictView.getUuid());
			return jsonBean;
		}
		
		return null;
	}
	/**
	 * 字典数据库对象集合转为字典jsonbean前端集合
	     * <p>@Description TODO</p>
		 * @Title: dictBeans2DictJsonBeans
	     * @return List<SysCompanyDictViewJsonBean>
	     * @author majiancheng       
	     * @date 2015-10-14 下午2:59:56
	 */
	public static List<SysCompanyDictViewJsonBean> dictBeans2DictJsonBeans(List<SysDict> sysCompanyDictViews) {
		if(CollectionUtils.isNotEmpty(sysCompanyDictViews)) {
			List<SysCompanyDictViewJsonBean> jsonBeans = new ArrayList<SysCompanyDictViewJsonBean>();
			for(SysDict dictView : sysCompanyDictViews) {
				if(dictView != null) {
					jsonBeans.add(dictBean2DictJsonBean(dictView));
				}
			}
			return jsonBeans;
		}
		
		return null;
	}
	/**
	 * 字典数据库对象转为字典jsonbean
	     * <p>@Description TODO</p>
		 * @Title: dictBean2DictJsonBean
	     * @return SysCompanyDictViewJsonBean
	     * @author majiancheng       
	     * @date 2015-10-14 下午2:56:12
	 */
	public static SysCompanyDictViewJsonBean sysCompanyDictBeans2DictJsonBean(SysCompanyDictView sysCompanyDictView) {
		if(sysCompanyDictView != null) {
			SysCompanyDictViewJsonBean jsonBean = new SysCompanyDictViewJsonBean();
			jsonBean.setId(sysCompanyDictView.getId().toString());
			jsonBean.setCode(sysCompanyDictView.getValue());
			jsonBean.setName(sysCompanyDictView.getLabel());
			jsonBean.setType(sysCompanyDictView.getType());
			jsonBean.setUuid(sysCompanyDictView.getUuid());
			return jsonBean;
		}
		
		return null;
	}
	/**
	 * 字典数据库对象集合转为字典jsonbean前端集合
	     * <p>@Description TODO</p>
		 * @Title: dictBeans2DictJsonBeans
	     * @return List<SysCompanyDictViewJsonBean>
	     * @author majiancheng       
	     * @date 2015-10-14 下午2:59:56
	 */
	public static List<SysCompanyDictViewJsonBean> SysCompanyDictBeans2DictJsonBeans(List<SysCompanyDictView> sysCompanyDictViews) {
		if(CollectionUtils.isNotEmpty(sysCompanyDictViews)) {
			List<SysCompanyDictViewJsonBean> jsonBeans = new ArrayList<SysCompanyDictViewJsonBean>();
			for(SysCompanyDictView dictView : sysCompanyDictViews) {
				if(dictView != null) {
					jsonBeans.add(sysCompanyDictBeans2DictJsonBean(dictView));
				}
			}
			return jsonBeans;
		}
		
		return null;
	}
}
