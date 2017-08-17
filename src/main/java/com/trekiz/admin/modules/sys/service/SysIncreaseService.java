package com.trekiz.admin.modules.sys.service;

import org.mozilla.javascript.EcmaError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.ChineseToEnglish;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.sys.entity.SysIncrease;
import com.trekiz.admin.modules.sys.repository.SysIncreaseDao;

/**
 * 自增sequence Service
 * @author liangjingming
 * @version 2014-01-17
 */
@Service
public class SysIncreaseService extends BaseService{

	/**最多更新的次数*/
	private static final int INCTIMES = 3;
	
	@Autowired
	private SysIncreaseDao sysIncreaseDao;
	
	/**
	 * 获取产品编号
	 * @param proCompanyId 批发商ID
	 * @param codeType 编码类型
	 * @return
	 */
	public SysIncrease findNextProductNum(Long proCompanyId,Integer codeType){
		
		return sysIncreaseDao.findNumByProidCode(proCompanyId, codeType);
	}
	
	/**
	 * 获取渠道商等级编号
	 * @param codeName 渠道商等级
	 * @param proCompanyId 批发商ID
	 * @param codeType 编码类型
	 * @return
	 */
	public SysIncrease findNextAgentLevelNum(String codeName,Long proCompanyId,Integer codeType){
		
		return sysIncreaseDao.findNumByProidCodeName(proCompanyId, codeType,codeName);
	}
	
	/**
	 * 获取订单编号编号
	 * @param proCompanyId 批发商ID
	 * @param codeType 编码类型
	 * @return
	 */
	public SysIncrease findNextOrderNum(Long proCompanyId,Integer codeType){
		
		return sysIncreaseDao.findNumByProidCode(proCompanyId, codeType);
	}
	
	/**
	 * 获取团号编码
	 * @param proCompanyId 批发商ID
	 * @param codeType 编码类型
	 * @return
	 */
	public SysIncrease findNextGroupNum(Long proCompanyId,Integer codeType){
		
		return sysIncreaseDao.findNumByProidCode(proCompanyId, codeType);
	}
	
	/**
	 * 更新自增sequence
	 * @param codeName 编码名称
	 * @param codeType 编码类型
	 * @return
	 */
	@Transactional(readOnly=false,rollbackFor={Exception.class})
	public synchronized String updateSysIncrease(String proCompanyName,Long proCompanyId,String codeName,Integer codeType){
		
		SysIncrease sysIncrease = new SysIncrease();
		String extra = "";
		String preffix = "";
		//产品编号规则
		if(codeType == Context.PRODUCT_NUM_TYPE){
			sysIncrease = findNextProductNum(proCompanyId,codeType);
			if(sysIncrease==null){
				throw new RuntimeException("当前用户没有定义订单编号策略，不能发布产品！");
			}
			Integer k = sysIncrease.getCodeNum();
			for(int i=0;i<INCTIMES;i++){
				sysIncrease.setCodeNum(sysIncrease.getCodeNum()+1);
				//bug17575，更新sysIncrease的CodeNum时要同时更新numlength
				sysIncrease.setNumLen(sysIncrease.getCodeNum().toString().length());
				sysIncrease = sysIncreaseDao.save(sysIncrease);
				//如果更新成功则跳出
				if(sysIncrease.getCodeNum()-k==1)
					break;
				else if(i==INCTIMES-1){
					//超过获取编号的次数,则抛出异常
					throw new ServiceException("获取产品编号出错,请联系管理员");
				}else{
					//并发对同一个version进行更新抛出异常,则继续循环
					sysIncrease = findNextProductNum(proCompanyId,codeType);
					continue;
				}							
			}
			preffix = ChineseToEnglish.getPinYinHeadChar(proCompanyName);
		}
		//团号规则
		else if(codeType == Context.GROUP_NUM_TYPE){
			sysIncrease = findNextGroupNum(proCompanyId,codeType);
			Integer k = sysIncrease.getCodeNum();
			for(int i=0;i<INCTIMES;i++){
				sysIncrease.setCodeNum(sysIncrease.getCodeNum()+1);
				//bug17575，更新sysIncrease的CodeNum时要同时更新numlength
				sysIncrease.setNumLen(sysIncrease.getCodeNum().toString().length());
				sysIncrease = sysIncreaseDao.save(sysIncrease);
				//如果更新成功则跳出
				if(sysIncrease.getCodeNum()-k==1)
					break;
				else if(i==INCTIMES-1){
					//超过获取编号的次数,则抛出异常
					throw new ServiceException("获取团号出错,请联系管理员");
				}else{
					//并发对同一个version进行更新抛出异常,则继续循环
					sysIncrease = findNextGroupNum(proCompanyId,codeType);
					continue;
				}							
			}
			preffix = "KCE";
		}
		//订单编号规则
		else if(codeType == Context.ORDER_NUM_TYPE){
			String curDate = DateUtils.getDate();
			sysIncrease = findNextOrderNum(proCompanyId,codeType);
			if(!curDate.equals(sysIncrease.getDateMark())){
				sysIncrease.setCodeNum(1);
				sysIncrease.setDateMark(curDate);
				sysIncrease = sysIncreaseDao.save(sysIncrease);
			}else{
				Integer k = sysIncrease.getCodeNum();
				for(int i=0;i<INCTIMES;i++){
					sysIncrease.setCodeNum(sysIncrease.getCodeNum()+1);
					//bug17575，更新sysIncrease的CodeNum时要同时更新numlength
					sysIncrease.setNumLen(sysIncrease.getCodeNum().toString().length());
					sysIncrease = sysIncreaseDao.save(sysIncrease);
					//如果更新成功则跳出
					if(sysIncrease.getCodeNum()-k==1)
						break;
					else if(i==INCTIMES-1){
						//超过获取编号的次数,则抛出异常
						throw new ServiceException("获取订单编号出错,请联系管理员");
					}else{
						//并发对同一个version进行更新抛出异常,则继续循环
						sysIncrease = findNextOrderNum(proCompanyId,codeType);
						continue;
					}							
				}
			}
			preffix = ChineseToEnglish.getPinYinHeadChar(proCompanyName);
			extra = sysIncrease.getDateMark().replace("-", "");
			if(extra.startsWith("20")){
			    extra = extra.substring(2);
			};
		}
		//渠道商等级编号
		else if(codeType == Context.PRICESTRATEGY_NUM_TYPE){
			sysIncrease = findNextAgentLevelNum(codeName,proCompanyId,codeType);
			Integer k = sysIncrease.getCodeNum();
			for(int i=0;i<INCTIMES;i++){
				sysIncrease.setCodeNum(sysIncrease.getCodeNum()+1);
				//bug17575，更新sysIncrease的CodeNum时要同时更新numlength
				sysIncrease.setNumLen(sysIncrease.getCodeNum().toString().length());
				sysIncrease = sysIncreaseDao.save(sysIncrease);
				//如果更新成功则跳出
				if(sysIncrease.getCodeNum()-k==1)
					break;
				else if(i==INCTIMES-1){
					//超过获取编号的次数,则抛出异常
					throw new ServiceException("获取订单编号出错,请联系管理员");
				}else{
					//并发对同一个version进行更新抛出异常,则继续循环
					sysIncrease = findNextAgentLevelNum(codeName,proCompanyId,codeType);
					continue;
				}							
			}
			preffix = codeName;
		}
			
		return preffix+extra+formatCodeNum(sysIncrease.getCodeNum(),sysIncrease.getNumLen());
	}
	
	/**
	 * 格式化4位产品编号,返回 编码+编号
	 * @param codeNum
	 * @return
	 */
	public String formatCodeNum(Integer codeNum,Integer numLen){
		
		String template = "000000000000000000000000";
		String s = codeNum.toString();
		int len = numLen-s.length();
		//try {
			s = template.substring(0, len)+s;
		//} catch (Exception e){
		//	e.printStackTrace();
		//}

		return s;
	}
}
