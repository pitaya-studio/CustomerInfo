package com.trekiz.admin.modules.wholesalerbase.service;

import java.util.List;

import com.trekiz.admin.modules.wholesalerbase.entity.Qualifications;

/**
 * 批发商资质附件关联
 * @author gao
 *  2015年4月14日
 */
public interface QualificationsService {
	
	/**
	 * 根据附件id 进行查找 对应的 Qualifications 对象
	 * @param salerTripFileId
	 * @return
	 * @author yunpeng.zhang
	 */
	public Qualifications getBySalerTripFileId(Long salerTripFileId);

	/**
	 * 保存附件
	 * @author gao
	 * @param qualifications
	 * @return
	 */
	public Qualifications save(Qualifications qualifications);
	/**
	 * 根据批发商ID查询附件列表
	 * @author gao
	 * @param companyId
	 * @return
	 */
	public List<Qualifications> getQualificationsByCompanyId(Long companyId);
	/**
	 * 根据批发商ID查询 除“其他文件”外的上传文件
	 * @author gao
	 * @param companyId
	 * @return
	 */
	public List<Qualifications> getQualificationsByCompanyIdWithOutOther(Long companyId);
	/**
	 * 根据批发商ID查询 “其他文件”
	 * @author gao
	 * @param companyId
	 * @return
	 */
	public List<Qualifications> getQualificationsByCompanyIdOther(Long companyId);
	/**
	 * 根据UUID 查询附件
	 * @author gao
	 * @param UUID
	 * @return
	 */
	public Qualifications getQualificationsByUUID(String UUID);
}
