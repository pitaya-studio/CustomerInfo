package com.trekiz.admin.modules.activity.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.activity.entity.HotelGroupCostView;

interface HotelGroupCostViewDaoCustom extends BaseDao<HotelGroupCostView> {
}

public interface HotelGroupCostViewDao extends HotelGroupCostViewDaoCustom, CrudRepository<HotelGroupCostView, Long> {
	   //散拼库存切位的业务类型=1
	   /*
	   @Query("from HotelGroupCostView" )
	    public  List<HotelGroupCostView> findHotelGroupCostView();	 
	   @Query("from HotelGroupCostView where flowType=?1 and ProductGroupId=?2 order by id")
	    public  List<HotelGroupCostView> findHotelGroupCostView( Integer flowType, Long productGroupId);	   
	  
	   //散拼库存切位的业务类型=1
	   @Query("from HotelGroupCostView where flowType=?1 and productId=?2 order by id")
	    public  List<HotelGroupCostView> findHotelGroupCostViewProduct(Integer flowType, Long productId);	
	   
	   @Query("from TargetArea")
	    public  List<TargetArea> findTargetArea();	
	   
	   @Query("from TargetArea where srcActivityId=?1")
	    public  List<TargetArea> findTargetAreaById(Long id);	
        */
}

@Repository
class HotelGroupCostViewDaoImpl extends BaseDaoImpl<HotelGroupCostView> implements HotelGroupCostViewDaoCustom {
	
}