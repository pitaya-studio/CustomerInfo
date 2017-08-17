package com.trekiz.admin.review.configuration.service;

import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.extend.condition.ConditionAssigneeResolver;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.review.money.repository.NewProcessMoneyAmountDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *产品发布者条件办理人解析器
 * @author zhenxing.yan
 * @date 2015/11/24
 */
public class ProductCreatorResolver implements ConditionAssigneeResolver{

    private static Logger logger= LoggerFactory.getLogger(ProductCreatorResolver.class);

    private static final Map<String ,String> productTypeMap=new HashMap<String,String>();

    static {
        /**
         * 单团类产品
         */
        productTypeMap.put(Context.ProductType.PRODUCT_SINGLE.toString(),Context.ProductType.PRODUCT_SINGLE.toString());
        productTypeMap.put(Context.ProductType.PRODUCT_LOOSE.toString(),Context.ProductType.PRODUCT_SINGLE.toString());
        productTypeMap.put(Context.ProductType.PRODUCT_STUDY.toString(),Context.ProductType.PRODUCT_SINGLE.toString());
        productTypeMap.put(Context.ProductType.PRODUCT_BIG_CUSTOMER.toString(),Context.ProductType.PRODUCT_SINGLE.toString());
        productTypeMap.put(Context.ProductType.PRODUCT_FREE.toString(),Context.ProductType.PRODUCT_SINGLE.toString());
        productTypeMap.put(Context.ProductType.PRODUCT_CRUISE.toString(),Context.ProductType.PRODUCT_SINGLE.toString());

        /**
         * 机票产品
         */
        productTypeMap.put(Context.ProductType.PRODUCT_AIR_TICKET.toString(),Context.ProductType.PRODUCT_AIR_TICKET.toString());

        /**
         * 签证产品
         */
        productTypeMap.put(Context.ProductType.PRODUCT_VISA.toString(),Context.ProductType.PRODUCT_VISA.toString());

        /**
         * 酒店产品
         */
        productTypeMap.put(Context.ProductType.PRODUCT_HOTEL.toString(),Context.ProductType.PRODUCT_HOTEL.toString());

        /**
         * 海岛游产品
         */
        productTypeMap.put(Context.ProductType.PRODUCT_ISLAND.toString(),Context.ProductType.PRODUCT_ISLAND.toString());
    }

    /**
     * 借用dao
     */
    @Autowired
    private NewProcessMoneyAmountDao newProcessMoneyAmountDao;

    @Override
    public List<String> resolve(Map<String, Object> variables) {
        List<String> result=new ArrayList<String>();
        /**
         * 获取所需的参数，如果没有，返回空
         */
        if (variables==null||variables.size()==0){
            logger.error("variables is empty!");
            return result;
        }

        String productId=null;
        if (variables.containsKey(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID)){
            productId=variables.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID)==null?null:variables.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID).toString();
        }

        String productUuid=null;
        if (variables.containsKey(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_UUID)){
            productUuid=variables.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_UUID)==null?null:variables.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_UUID).toString();
        }

        if (productId==null&&productUuid==null){
            logger.error("both productId and productUuid are null!");
            return result;
        }

        if (!variables.containsKey(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE)){
            logger.error("productType is null!");
            return result;
        }

        String productType=variables.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE).toString();
        if (StringUtils.isBlank(productType)){
            logger.error("productType is null!");
            return result;
        }

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("select creator from product_creator where (");
        boolean hasUuid=false;
        if (StringUtils.isNotBlank(productUuid)){
            hasUuid=true;
            stringBuilder.append(" product_uuid = ");
            stringBuilder.append(productUuid);
        }

        if (StringUtils.isNotBlank(productId)){
            if (hasUuid){
                stringBuilder.append(" or ");
            }
            stringBuilder.append(" product_id = ");
            stringBuilder.append(productId);
        }

        stringBuilder.append(" ) and product_type = ");
        stringBuilder.append(productType);

        List<?> creators=newProcessMoneyAmountDao.findBySql(stringBuilder.toString());

        if (creators==null||creators.size()>1){
            logger.error("no creator or more than one creators have found!");
            return result;
        }
        result.add(creators.get(0).toString());
        return result;
    }
}
