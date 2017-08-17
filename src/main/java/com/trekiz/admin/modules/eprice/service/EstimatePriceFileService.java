package com.trekiz.admin.modules.eprice.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.trekiz.admin.modules.eprice.entity.EstimatePriceFile;
import com.trekiz.admin.modules.sys.entity.User;

/**
 * 询价相关文件service业务接口
 * @author lihua.xu
 * @时间 2014年9月17日
 *
 */
public interface EstimatePriceFileService {
	
	/**
	 * 询价相关文件存储的基础接口方法
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param epp
	 */
	public void save(EstimatePriceFile epf);
	
	/**
	 * 通过id获取询价相关文件对象
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param id 询价相关文件id
	 * @return EstimatePriceFile
	 */
	public EstimatePriceFile findById(Long id);
	
	/**
	 * 通过项目ID获取相关文件对象
	 * @param pid
	 * @return
	 */
	public EstimatePriceFile findByPId(Long pid);
	
	/**
	 * 通过id逻辑删除询价相关文件对象
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param id 询价相关文件id
	 */
	public void delById(Long id);
	
	/**
	 * 初始化保存一个无归属的临时行程文件,如果id不为null,则把file更新到id对应的EstimatePriceFile数据记录中
	 * @author lihua.xu
	 * @时间 2014年9月17日
	 * @param file
	 * @param id EstimatePriceFile的id
	 * @return Map<String,Object> {res:处理结果，model:保存后的EstimatePriceFile对象}
	 */
	public Map<String,Object> saveTemp(MultipartFile file,Long id,User user);
}
