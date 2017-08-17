package com.trekiz.admin.modules.activity.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.activity.entity.GroupControlBoard;

/**
 * 团控板DAO接口
 * @author tao.liu
 *
 */
public interface GroupControlBoardDao extends GroupControlBoardDaoCustom,CrudRepository<GroupControlBoard, Long>{


}

/**
* 自定义DAO接口
*/
interface GroupControlBoardDaoCustom extends BaseDao<GroupControlBoard>{


}

/**
* 自定义DAO接口实现
*/
@Repository
class GroupControlBoardDaoImpl extends BaseDaoImpl<GroupControlBoard> implements GroupControlBoardDaoCustom{


}

