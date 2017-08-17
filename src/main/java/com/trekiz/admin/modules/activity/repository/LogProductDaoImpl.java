package com.trekiz.admin.modules.activity.repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.activity.entity.LogProduct;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by PF1WZLH on 2016/7/26.
 */
@Repository
public class LogProductDaoImpl extends BaseDaoImpl<LogProduct> implements LogProductDao {
    @Override
    public void save(LogProduct logProduct) {
        this.getSession().save(logProduct);
    }

    @Override
    public List<Map<String, Object>> findLogProductList(Long groupId) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select id,uuid,product_type,activity_id,group_id,business_type,field_name, " +
                "content,create_by,create_date,company_id,is_read from log_product where group_id = ?");
        List<Map<String,Object>> LogProductList = findBySql(stringBuffer.toString(), Map.class, groupId);
        return LogProductList;
    }

    /**
     * 查找log_product中是否有groupId的记录，存在返回true，否则返回false
     * @param groupId
     * @return
     */
    public boolean checkPriceRecordExist(Long groupId){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select id,uuid,product_type,activity_id,group_id,business_type,field_name, " +
                "content,create_by,create_date,company_id,is_read from log_product where group_id = ?");
        List<Map<String,Object>> logProductList = findBySql(stringBuffer.toString(), Map.class, groupId);
        if(logProductList == null || logProductList.isEmpty()){
            return  false;
        }
        return true;
    }
}
