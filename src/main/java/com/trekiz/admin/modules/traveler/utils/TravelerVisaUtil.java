package com.trekiz.admin.modules.traveler.utils;

import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.grouphandle.dao.GroupHandleVisaDao;
import com.trekiz.admin.modules.grouphandle.entity.GroupHandleVisa;
import com.trekiz.admin.modules.traveler.entity.TravelerVisa;
import com.trekiz.admin.modules.traveler.repository.TravelerVisaDao;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;

/**
 * 游客签证信息工具类
 * @author yanzhenxing
 * @date 2016/1/27
 */
public class TravelerVisaUtil {

    private static TravelerVisaDao travelerVisaDao=SpringContextHolder.getBean(TravelerVisaDao.class);

    private static GroupHandleVisaDao groupHandleVisaDao=SpringContextHolder.getBean(GroupHandleVisaDao.class);

    /**
     * 获取游客自备签列表
     * @param travelerId
     * @return
     */
    public static List<Map<String,Object>> obtainTravelerProvidedVisaList(Long travelerId){
        if(travelerId==null){
            return new ArrayList<>();
        }
        List<TravelerVisa> travelerVisas=travelerVisaDao.findProvidedVisaByTravelerId(travelerId);
        if(travelerVisas==null||travelerVisas.size()==0){
            return new ArrayList<>();
        }else{
            List<Map<String,Object>> result=new ArrayList<>();
            for (TravelerVisa travelerVisa : travelerVisas) {
                Map<String,Object> visaMap=new HashMap<>();

                visaMap.put("id",travelerVisa.getId());
//                visaMap.put("countryId",travelerVisa.getApplyCountry()==null?"":travelerVisa.getApplyCountry().getId());
//                visaMap.put("countryName",travelerVisa.getApplyCountry()==null?"":travelerVisa.getApplyCountry().getName());
                visaMap.put("countryId",travelerVisa.getApplyCountryNew()==null?"":travelerVisa.getApplyCountryNew().getId());
                visaMap.put("countryName",travelerVisa.getApplyCountryNew()==null?"":travelerVisa.getApplyCountryNew().getCountryName_cn());
                visaMap.put("manorId",travelerVisa.getManorId()==null?"":travelerVisa.getManorId());
                visaMap.put("manorName",travelerVisa.getManorName()==null?"":travelerVisa.getManorName());
                visaMap.put("visaType","0");//自备签类型为0
                visaMap.put("contractDate",travelerVisa.getContractDate());
                result.add(visaMap);
            }
            return result;
        }
    }

    /**
     * 获取游客申请签证列表
     * @param orderId
     * @param travelerId
     * @return
     */
    public static List<Map<String,Object>> obtainTravelerAppliedVisaList(Integer orderId,Integer travelerId){
        if(orderId==null||travelerId==null){
            return new ArrayList<>();
        }
        List<GroupHandleVisa> travelerVisas=groupHandleVisaDao.findByOrderIdAndTravelerId(orderId,travelerId);
        if(travelerVisas==null||travelerVisas.size()==0){
            return new ArrayList<>();
        }else{
            List<Map<String,Object>> result=new ArrayList<>();
            for (GroupHandleVisa travelerVisa : travelerVisas) {
                Map<String,Object> visaMap=new HashMap<>();

                visaMap.put("id",travelerVisa.getId());
                visaMap.put("countryId",travelerVisa.getVisaCountryId());
                visaMap.put("countryName",travelerVisa.getVisaCountryName());
                visaMap.put("manorId",travelerVisa.getVisaConsularDistricId());
                visaMap.put("manorName",travelerVisa.getVisaConsularDistricName());
                visaMap.put("visaType",travelerVisa.getVisaTypeId());//自备签类型为0
                visaMap.put("contractDate",travelerVisa.getAboutSigningTime());
                result.add(visaMap);
            }
            return result;
        }
    }

    /**
     * 获取游客所有的签证列表，包括自备和申请的
     * @param orderId
     * @param travelerId
     * @return
     */
    public static List<Map<String,Object>> obtainTravelerAllVisaList(Integer orderId,Integer travelerId) {
        if (orderId == null || travelerId == null) {
            return new ArrayList<>();
        }

        List<Map<String,Object>> result=new ArrayList<>();

        result.addAll(obtainTravelerAppliedVisaList(orderId,travelerId));
        result.addAll(obtainTravelerProvidedVisaList(Long.parseLong(travelerId.toString())));

        if (CollectionUtils.isEmpty(result)) {
        	result.add(generateDefaultTravelerVisaMap());
		}

        return result;
    }

    /**
     * 产生默认的数据
     * @return
     */
    private static Map<String,Object> generateDefaultTravelerVisaMap(){
        Map<String,Object> result=new HashMap<>();
        result.put("id","");
        result.put("countryId","-1");
        result.put("countryName","");
        result.put("manorId","-1");
        result.put("manorName","");
        result.put("visaType","-1");//自备签类型为0
        result.put("contractDate",null);
        return result;
    }
}
