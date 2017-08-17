package com.trekiz.admin.modules.activity.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.modules.activity.entity.ProductBrowseNum;
import com.trekiz.admin.modules.activity.repository.ProductBrowseNumDao;

@Service
@Transactional(readOnly = true)
public class IProductBrowseNumService implements ProductBrowseNumService{
	
	@Autowired
	private ProductBrowseNumDao productBrowseNumDao;
	
	@Override
	public void updateBrowseNum(ProductBrowseNum browseNum) {
		// TODO Auto-generated method stub
		Long num = browseNum.getBrowseNumber()+1;
		Date nowTime = new Date(); 
		productBrowseNumDao.updateBrowseNum(num, nowTime, browseNum.getProductId());
	}
	/**
	 * 根据传入的productId，查询是否存在该商品的浏览记录，如果没有则初始化记录，如果有则取出ProductBrowseNum
	 * @return
	 */
	@Override
	public ProductBrowseNum getProductBrowseNumById(Long productId) {
		// TODO Auto-generated method stub
		List<ProductBrowseNum> list = productBrowseNumDao.findBrowseNumByProductId(productId);
		//根据productId判断该产品是否被浏览过,如果没有浏览过还需初始化实体
		if(list.size()>0){
			return list.get(0);
		}else{
			ProductBrowseNum browseNum =new ProductBrowseNum();
			browseNum.setProductId(productId);
			browseNum.setUpdateTime(new Date());
			browseNum.setBrowseNumber(new Long(0));
			productBrowseNumDao.save(browseNum);
			
			List<ProductBrowseNum> browselist = productBrowseNumDao.findBrowseNumByProductId(productId);
			return browselist.get(0);
		}
		
	}

	@Override
	public void saveProductBrowseNum(ProductBrowseNum browseNum) {
		// TODO Auto-generated method stub
		productBrowseNumDao.save(browseNum);
	}
	/**
	 * 更新产品浏览次数
	 */
	@Override
	public void updateBrowseNumByProductId(Long productId) {
		// TODO Auto-generated method stub
		List<ProductBrowseNum> list = productBrowseNumDao.findBrowseNumByProductId(productId);
		//根据productId判断该产品是否被浏览过,如果没有浏览过还需初始化实体
		if(list.size()>0){
			ProductBrowseNum productBrowseNum = list.get(0);
			productBrowseNum.setBrowseNumber(productBrowseNum.getBrowseNumber()+1);
			productBrowseNum.setUpdateTime(new Date());
			updateBrowseNum(productBrowseNum);
		}else{
			ProductBrowseNum browseNum =new ProductBrowseNum();
			browseNum.setProductId(productId);
			browseNum.setUpdateTime(new Date());
			browseNum.setBrowseNumber(new Long(1));
			productBrowseNumDao.save(browseNum);
		}
	}
	
}
