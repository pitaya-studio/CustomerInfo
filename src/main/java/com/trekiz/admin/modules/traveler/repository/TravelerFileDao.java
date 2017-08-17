package com.trekiz.admin.modules.traveler.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.traveler.entity.TravelerFile;


/**
 * 游客上传文件信息DAO接口
 * @author haili.jiang
 *
 */
public interface TravelerFileDao extends TravelerFileDaoCustom,CrudRepository<TravelerFile, Long>{

	@Query("from TravelerFile  where srcDocId = ?1 and srcTravelerId = ?2 and delFlag='0'")
	public TravelerFile findByPDocIDAndTravelerId(Integer srcDocId,Integer srcTravelerId);
	
	@Query("from TravelerFile  where srcTravelerId = ?1 and delFlag='0'")
	public List<TravelerFile> findFileListByPid(Long srcTravelerId);
	
	@Query("from TravelerFile  where srcTravelerId = ?1 and delFlag = ?2")
	public List<TravelerFile> findBySrcTravelerIdAndDelFlag(Long srcTravelerId, String delFlag);
	
	@Query("from TravelerFile  where srcTravelerId = ?1 and fileType = ?2 and delFlag = ?3")
	public List<TravelerFile> findBySrcTravelerIdAndFileTypeAndDelFlag(Long srcTravelerId, Integer fileType, String delFlag);
	
	@Query("from TravelerFile  where srcTravelerId = ?1 and fileType = ?2 and delFlag = '0'")
	public List<TravelerFile> findBySrcTravelerIdAndFileType(Long srcTravelerId, Integer fileType);
	
	@Modifying
	@Query("update TravelerFile set delFlag = '1' where id = ?1")
	public void delTravelerFileById(Long id);
	
	@Modifying
	@Query("update TravelerFile set delFlag = '1' where srcTravelerId = ?1")
	public void del2FlagByTravelerId(Long id);
	
	@Modifying
	@Query("update TravelerFile set delFlag = '1' where srcDocId = ?1")
	public void delTravelerFileBySrcDocId(Long id);
	
	@Modifying
	@Query("delete TravelerFile where srcTravelerId = ?1")
	public void delTravelerFileByTravelerId(Long travelerId);
	
	/**
	 * 根据 附件类型及游客id 删除 数据
	 * @param fileType
	 * @param srcTravelerId
	 */
	@Modifying
	@Query("update TravelerFile set delFlag = '1' where fileType = ?1 and srcTravelerId = ?2 ")
	public void delTravelerFileByUninId(Integer fileType,Long srcTravelerId);
	
	@Modifying
	@Query("update TravelerFile set delFlag = '1' where id in ?1")
	public void delTravelerFileByIds(List<Long> ids);

	/**
	 * 删除。根据游客id，文件类型ids 字符串
	 * @param typesStr
	 * @param srcTravelerId
	 */
	@Modifying
	@Query(value="update travelerfile set delFlag = '1' where fileType in ?1 and srcTravelerId = ?2 ", nativeQuery=true)
	public void delByTypesAndTrvl(List<Integer> types, Long srcTravelerId);
	
}

/**
 * 自定义接口
 * @author taoxiaoyang
 *
 */
interface TravelerFileDaoCustom extends BaseDao<TravelerFile>{
	
	List<Map<String, Object>> findFilesByPid(Long srcTravelerId); 
}

/**
 * 自定义接口实现
 * @author taoxiaoyang
 *
 */
@Repository
class TravelerFileDaoImpl extends BaseDaoImpl<TravelerFile> implements TravelerFileDaoCustom{

	@Override
	public List<Map<String, Object>> findFilesByPid(Long srcTravelerId) {
		String sql = "SELECT tf.fileType,GROUP_CONCAT(tf.fileName SEPARATOR ' + ') fileName from travelerfile tf  where srcTravelerId = ? and delFlag='0' GROUP BY fileType";
		List<Map<String, Object>> list = this.findBySql(sql, Map.class, srcTravelerId);
		return list;
	}

}