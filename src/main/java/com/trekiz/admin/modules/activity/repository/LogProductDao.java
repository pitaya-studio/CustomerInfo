package com.trekiz.admin.modules.activity.repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.activity.entity.LogProduct;

import java.util.List;
import java.util.Map;

/**
 * Created by PF1WZLH on 2016/7/26.
 */
public interface LogProductDao extends BaseDao<LogProduct> {

    public void save(LogProduct logProduct);

    public List<Map<String, Object>> findLogProductList(Long groupId);

    public boolean checkPriceRecordExist(Long groupId);
}
