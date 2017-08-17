package com.trekiz.admin.modules.mtourCommon.transfer;

import com.trekiz.admin.modules.mtourCommon.entity.MenuInfoTempBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.SecondLevelMenuInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.ThirdLevelMenuInfoJsonBean;
import com.trekiz.admin.modules.mtourCommon.jsonbean.TopLevelMenuInfoJsonBean;

/**
 * Menu后台结构 到 前端结构转换类
 * @author ning.zhang@quauq.com
 * @date 2015年10月22日18:06:33
 */
public final class MenuInfoTransfer {
	
	private MenuInfoTransfer(){}
	
	/**
	 * 把Menu后台结构 到 前端menu第一层结构
	 * @author ning.zhang@quauq.com
	 * @date 2015年10月22日18:06:33
	 */
	public static TopLevelMenuInfoJsonBean MenuInfo2TopLevelMenuJsonBean(MenuInfoTempBean menuInfo){
		TopLevelMenuInfoJsonBean jBean = new TopLevelMenuInfoJsonBean();
		jBean.setMenuName(menuInfo.getName());
		jBean.setMenuCode(String.valueOf(menuInfo.getId()));
		jBean.setIsShow(menuInfo.getIsShow());
		jBean.setIcon(menuInfo.getIcon());
		jBean.setSort(menuInfo.getSort());
		return jBean;
	}
	
	/**
	 * 把Menu后台结构 到 前端menu第二层结构
	 * @author ning.zhang@quauq.com
	 * @date 2015年10月22日18:06:33
	 */
	public static SecondLevelMenuInfoJsonBean MenuInfo2SecondLevelMenuJsonBean(MenuInfoTempBean menuInfo){
		SecondLevelMenuInfoJsonBean jBean = new SecondLevelMenuInfoJsonBean();
		jBean.setSubMenuName(menuInfo.getName());
		jBean.setSubMenuCode(String.valueOf(menuInfo.getId()));
		jBean.setSubMenuUrl(menuInfo.getHref());
		jBean.setIsShow(menuInfo.getIsShow());
		jBean.setIcon(menuInfo.getIcon());
		jBean.setSort(menuInfo.getSort());
		return jBean;
	}
	
	/**
	 * 把Menu后台结构 到 前端menu第三层结构
	 * @author ning.zhang@quauq.com
	 * @date 2015年10月22日18:06:33
	 */
	public static ThirdLevelMenuInfoJsonBean MenuInfo2ThirdLevelMenuJsonBean(MenuInfoTempBean menuInfo){
		ThirdLevelMenuInfoJsonBean jBean = new ThirdLevelMenuInfoJsonBean();
		jBean.setRoleName(menuInfo.getName());
		jBean.setRoleCode(menuInfo.getPermission());
		jBean.setLevel(menuInfo.getLevel());
		jBean.setIsShow(menuInfo.getIsShow());
		jBean.setIcon(menuInfo.getIcon());
		jBean.setSort(menuInfo.getSort());
		return jBean;
	}
			
}
