package com.trekiz.admin.modules.activity.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.activity.entity.IslandGroupCostView;

interface IslandGroupCostViewDaoCustom extends BaseDao<IslandGroupCostView> {
}

public interface IslandGroupCostViewDao extends IslandGroupCostViewDaoCustom, CrudRepository<IslandGroupCostView, Long> {
	   //散拼库存切位的业务类型=1
	/*
	   @Query("from IslandGroupCostView" )
	    public  List<IslandGroupCostView> findIslandGroupCostView();	 
	   @Query("from IslandGroupCostView where flowType=?1 and ProductGroupId=?2 order by id")
	    public  List<IslandGroupCostView> findIslandGroupCostView( Integer flowType, Long productGroupId);	   
	  
	   //散拼库存切位的业务类型=1
	   @Query("from IslandGroupCostView where flowType=?1 and productId=?2 order by id")
	    public  List<IslandGroupCostView> findIslandGroupCostViewProduct(Integer flowType, Long productId);	
	   
	   @Query("from TargetArea")
	    public  List<TargetArea> findTargetArea();	
	   
	   @Query("from TargetArea where srcActivityId=?1")
	    public  List<TargetArea> findTargetAreaById(Long id); */	

}

@Repository
class IslandGroupCostViewDaoImpl extends BaseDaoImpl<IslandGroupCostView> implements IslandGroupCostViewDaoCustom {
	
}