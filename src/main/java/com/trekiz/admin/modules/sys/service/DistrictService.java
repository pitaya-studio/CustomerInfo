package com.trekiz.admin.modules.sys.service;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.District;
import com.trekiz.admin.modules.sys.repository.AreaDao;
import com.trekiz.admin.modules.sys.repository.DistrictDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zzk on 2016/10/13.
 */
@Service
@Transactional(readOnly = true)
public class DistrictService extends BaseService {

    @Autowired
    private DistrictDao districtDao;
    @Autowired
    private AreaDao areaDao;

    /**
     * 根据名称获取区域对象
     * @param name
     * @return
     */
    public District getDistrictByName(String name) {
        return districtDao.getByName(name);
    }

    /**
     * 根据id获取区域对象
     * @param id
     * @return
     */
    public District getDistrictById(Long id) {
        return districtDao.getById(id);
    }

    /**
     * 所有区域信息
     * @param page
     * @return
     */
    public List<District> getAllDistrict(Page<District> page, String name, String inout) {
        DetachedCriteria dc = districtDao.createDetachedCriteria();
        dc.add(Restrictions.eq(District.DEL_FLAG, Context.DEL_FLAG_NORMAL));
        if (StringUtils.isNotBlank(name)) {
            dc.add(Restrictions.like("name", "%"+name+"%"));
        }
        if (StringUtils.isNotBlank(inout)) {
            dc.add(Restrictions.eq("tourInOut", Integer.parseInt(inout)));
        }
        dc.addOrder(Order.desc("createDate"));
        return districtDao.find(dc);
    }

    /**
     * 保存或更新
     * @param district
     */
    public void saveOrUpdate(District district) {
        if (district.getId() == null) {
            districtDao.save(district);
        } else {
            districtDao.updateObj(district);
        }
    }

    /**
     * 删除
     * @param id
     */
    public void deleteDistrict(Long id) {
        districtDao.deleteDistrict(id);
    }

    /**
     * 检查区域名称是否重复
     * @param name
     * @param id
     * @return
     */
    public boolean checkDistrictName(String name, Long id) {
        List<District> districts = districtDao.checkDistrictName(name, id);
        if (districts != null && districts.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 检查区域是否被使用
     * @param id
     * @return
     */
    public boolean checkDistrict(Long id) {
        List<Area> areas = areaDao.checkDistrict(id);
        if (areas != null && areas.size() > 0) {
            return true;
        }
        return false;
    }

}
