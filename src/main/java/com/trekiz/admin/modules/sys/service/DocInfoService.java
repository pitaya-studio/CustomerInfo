package com.trekiz.admin.modules.sys.service;

import com.google.common.collect.Lists;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件上传附件Service
 * @author liangjingming
 */
@Service
@Transactional(readOnly = true)
public class DocInfoService {

	@Autowired
	private DocInfoDao docInfoDao;
	
	public DocInfo getDocInfo(Long id){
	   return docInfoDao.findOne(id);
	}
	
	public DocInfo saveDocInfo(DocInfo docinfo){
	   return docInfoDao.save(docinfo);
	}
	
	public List<DocInfo> getDocInfoList(Long payOrderId){
		return docInfoDao.findDocInfoByPayOrderId(payOrderId);
	}
	
	/**
	 * 根据ID集合查询所有的文档    
	 * 创建人：Administrator   
	 * 创建时间：2014-1-28 下午2:06:25     
	 * @version    
	 *
	 */
	public List<DocInfo> getDocInfoByIds(List<Long> ids){
		if(ids != null && ids.size()>0){
			return (List<DocInfo>) docInfoDao.findAll(ids);
		}else{
			return null;
		}
		
	}

	/**
	 * 根据字符串类型的id查询出DocInfo
	 * @param separator 指定的分隔符
	 * @param ids 字符串id
     * @return
	 * @author yudong.xu 2016.9.21
     */
	public List<DocInfo> getDocInfoByStringIds(String separator,String ids) {

		if (StringUtils.isBlank(ids)) {
			return Lists.newArrayList();
		}

		String[] idArray = ids.split(separator);
		if (idArray == null || idArray.length == 0) {
			return Lists.newArrayList();
		}

		List<Long> idList = Lists.newArrayList();
		for (String id : idArray) {
			if (StringUtils.isNotBlank(id)) {
				idList.add(Long.parseLong(id));
			}
		}

		return getDocInfoByIds(idList);
	}
	
	public List<DocInfo> getDocInfoBydocids(String docids) {

		List<DocInfo> docList = Lists.newArrayList();

		List<Long> ids = Lists.newArrayList();
		if (StringUtils.isBlank(docids)) {
			return docList;
		}
		String[] idarray = docids.split(",");
		if (idarray == null || idarray.length == 0) {
			return docList;
		}

		for (String id : idarray) {
			if (StringUtils.isNotBlank(id)) {
				ids.add(Long.parseLong(id));
			}
		}

		docList = getDocInfoByIds(ids);

		return docList;
	}
	
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public void delDocInfoById(Long id){
		docInfoDao.delDocInfoById(id);
	}
	
	/**
	 * 批量保存文件对象
	 * 
	 * @param list
	 * @return
	 */
	public Iterable<DocInfo> saveDocInfoList(ArrayList<DocInfo> list){
		Iterable<DocInfo> backList = new ArrayList<DocInfo>();
		if(list!=null && list.size()>0){
			backList=docInfoDao.save(list);
		}
		return backList;
	}

    /**
     * 根据名称获取文档对象
     * @return
     */
	public DocInfo getDocInfoByName() {
		String sql = "select * from docinfo where docName = 'T1区域表.xlsx' or docName = 'T1区域表.xls' order by id desc";
		List<DocInfo> docInfos = docInfoDao.findBySql(sql, DocInfo.class);
		if(docInfos != null && docInfos.size() > 0) {
			return docInfos.get(0);
		}
		return null;
	}
	
	public DocInfo getDocInfoByNameForLine(){
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT * FROM docinfo WHERE docName = 'T2游玩线路.xlsx' OR docName = 'T2游玩线路.xls'  ORDER BY id DESC ");
		List<DocInfo> docInfos = docInfoDao.findBySql(sbf.toString(), DocInfo.class);
		if(docInfos != null && docInfos.size() > 0) {
			return docInfos.get(0);
		}
		return null;
	}
}
