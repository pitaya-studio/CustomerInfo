package com.trekiz.admin.agentToOffice.T2.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.agentToOffice.T2.entity.Rate;
import com.trekiz.admin.agentToOffice.T2.service.QuauqServiceFeeService;
import com.trekiz.admin.agentToOffice.quauqstrategy.entity.QuauqGroupStrategy;
import com.trekiz.admin.agentToOffice.quauqstrategy.service.QuauqGroupStrategyService;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 *
 */
public class RateUtils {

    private static ActivityGroupService activityGroupService = SpringContextHolder.getBean(ActivityGroupService.class);
    private static OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
    private static QuauqGroupStrategyService quauqGroupStrategyService = SpringContextHolder.getBean(QuauqGroupStrategyService.class);
    private static AgentinfoService agentinfoService = SpringContextHolder.getBean(AgentinfoService.class);
    private static QuauqServiceFeeService quauqServiceFeeService = SpringContextHolder.getBean(QuauqServiceFeeService.class);

    /**
     * 查询该团期设置的Quauq费率和渠道费率，如果该团期没有设置，则取该产品所对应批发商设置的Quauq费率和渠道费率
     * @param activityId   产品或者团期ID
     * @param productType    产品类型
     * @return
     * @author  shijun.liu
     * @date    2016.08.10
     */
    public static Rate getRate(Long activityId, Integer productType){
        if(null == activityId){
            throw new RuntimeException("团期或者产品ID不能为空");
        }
        if(null == productType){
            throw new RuntimeException("产品类型不能为空");
        }
        // 1. 首先查看该团期是否设置费率.
        if(Context.ORDER_TYPE_QZ == productType){
            return getVisaRate(activityId, productType, null);
        }else if(Context.ORDER_TYPE_JP == productType){
            return getAirticketRate(activityId, productType, null);
        }else{
            return getActivityRate(activityId, productType);
        }
    }


    /**
     * 查询团期类产品的Quauq服务费和渠道服务费
     * @param activityId    团期ID
     * @param productType   产品类型
     * @return
     * @author  shijun.liu
     * @date    2016.08.10
     */
    private static Rate getActivityRate(Long activityId, Integer productType){
        //团期信息
        ActivityGroup activityGroup = activityGroupService.findById(activityId);
        Long companyId = activityGroup.getTravelActivity().getProCompany();
        Office office = officeService.findWholeOfficeById(companyId);
        String companyUuid = office.getUuid();
        //查询批发商设置的汇率,渠道类型默认为1，因为任何一种渠道类型，在批发商设置那Quauq费率肯定是相同的
        Rate companyRate = quauqServiceFeeService.getCompanyRate(companyUuid, 1);
        if(null == companyRate.getQuauqRate() || -1 == companyRate.getQuauqRateType()){
            throw new RuntimeException("批发商未设置默认Quauq费率，请检查");
        }
        return companyRate;
    }

    /**
     * 查询该团期设置的Quauq费率和渠道费率，如果该团期没有设置，则取该产品所对应批发商设置的Quauq费率和渠道费率
     * @param activityId   产品或者团期ID
     * @param productType    产品类型
     * @param agentId      下单时选择的渠道ID
     * @return
     * @author  shijun.liu
     * @date    2016.08.10
     */
    public static Rate getRate(Long activityId, Integer productType, Long agentId){
        if(null == activityId){
            throw new RuntimeException("团期或者产品ID不能为空");
        } 
        if(null == productType){ 
            throw new RuntimeException("产品类型不能为空");
        }
        if(null == agentId){
            throw new RuntimeException("渠道ID不能为空");
        }
        // 1. 首先查看该团期是否设置费率.
        if(Context.ORDER_TYPE_QZ == productType){
            return getVisaRate(activityId, productType, agentId);
        }else if(Context.ORDER_TYPE_JP == productType){
            return getAirticketRate(activityId, productType, agentId);
        }else{
            return getActivityRate(activityId, productType, agentId);
        }
    }

    /**
     * 查询团期类产品的Quauq服务费和渠道服务费
     * @param activityId    团期ID
     * @param productType   产品类型
     * @param agentId       下单时的渠道ID
     * @return
     * @author  shijun.liu
     * @date    2016.08.10
     */
    private static Rate getActivityRate(Long activityId, Integer productType, Long agentId){
        //团期信息
        ActivityGroup activityGroup = activityGroupService.findById(activityId);
        Long companyId = activityGroup.getTravelActivity().getProCompany();
        Office office = officeService.findWholeOfficeById(companyId);
        String companyUuid = office.getUuid();
        Agentinfo agentinfo = agentinfoService.findOne(agentId);
        if(null == agentinfo){
            throw new RuntimeException("下单渠道不存在，请检查");
        }
        String agentType = agentinfo.getAgentType();
        Integer agentTypeVal = -9999;
        if(StringUtils.isNotBlank(agentType)){
            agentTypeVal = Integer.parseInt(agentType);
            // 如果该门店存在父级，汇率则取其父级渠道设置的汇率
            if ("1".equals(agentType) && StringUtils.isNotBlank(agentinfo.getAgentParent()) && Integer.parseInt(agentinfo.getAgentParent()) != -1) {
            	agentinfo = agentinfoService.findOne(Long.parseLong(agentinfo.getAgentParent()));
            	agentId = agentinfo.getId();
            }
        }
        //查询对应团期设置的费率
        Rate rate = quauqGroupStrategyService.getGroupRate(companyUuid, activityId, productType, agentId);
        rate.setAgentType(agentTypeVal);
        boolean b = needQueryCompany(rate);
        if(b){
            //查询批发商设置的汇率
            Rate companyRate = quauqServiceFeeService.getCompanyRate(companyUuid, agentTypeVal);
            if(null == companyRate.getQuauqRate() || -1 == companyRate.getQuauqRateType()){
                throw new RuntimeException("批发商未设置默认Quauq费率，请检查");
            }
            BigDecimal quauqRate = rate.getQuauqRate();
            if(null == quauqRate){
                rate.setQuauqRateType(companyRate.getQuauqRateType());
                rate.setQuauqRate(companyRate.getQuauqRate());
            }

            /**
             *  如果团期Quauq其他费率为0，则取批发商设置的Quauq其他费率
             *  如果批发商设置的Quauq其他费率类型为-1，说明批发商也未设置，则最终取值为0
             */
            BigDecimal quauqOtherRate = rate.getQuauqOtherRate();
            if(null == quauqOtherRate || quauqOtherRate.compareTo(new BigDecimal("0")) == 0){
                if(-1 == companyRate.getQuauqOtherRateType() || null == companyRate.getQuauqOtherRate()){
                    rate.setQuauqOtherRateType(-1);
                    rate.setQuauqOtherRate(new BigDecimal(0));
                }else{
                    rate.setQuauqOtherRateType(companyRate.getQuauqOtherRateType());
                    rate.setQuauqOtherRate(companyRate.getQuauqOtherRate());
                }
            }
            /**
             *  如果团期的渠道费率为0，则取批发商设置的渠道费率
             *  如果批发商设置的渠道费率类型为-1，说明批发商也未设置，则最终取值为0
             */
            BigDecimal agentRate = rate.getAgentRate();
            if(null == agentRate || agentRate.compareTo(new BigDecimal("0")) == 0){
                if(-1 == companyRate.getAgentRateType() || null == companyRate.getAgentRate()){
                    rate.setAgentRateType(-1);
                    rate.setAgentRate(new BigDecimal(0));
                }else{
                    rate.setAgentRateType(companyRate.getAgentRateType());
                    rate.setAgentRate(companyRate.getAgentRate());
                }
            }
            /**
             *  如果团期渠道其他费率为0，则取批发商设置的渠道其他费率
             *  如果批发商设置的渠道其他费率类型为-1，说明批发商也未设置，则最终取值为0
             */
            BigDecimal agentOtherRate = rate.getAgentOtherRate();
            if(null == agentOtherRate || agentOtherRate.compareTo(new BigDecimal("0")) == 0){
                if(-1 == companyRate.getAgentOtherRateType() || null == companyRate.getAgentOtherRate()){
                    rate.setAgentOtherRateType(-1);
                    rate.setAgentOtherRate(new BigDecimal(0));
                }else{
                    rate.setAgentOtherRateType(companyRate.getAgentOtherRateType());
                    rate.setAgentOtherRate(companyRate.getAgentOtherRate());
                }
            }
        }
        Rate companyRate = quauqServiceFeeService.getCompanyRate(companyUuid, agentTypeVal);
        rate.setChouchengRate(companyRate.getChouchengRate());
        rate.setChouchengRateType(companyRate.getChouchengRateType());
        return rate;
    }

    /**
     * 判断是否需要再查询批发商设置的汇率，默认false，表示不需要，如果有如下情形则需要查询批发商设置的汇率
     *      如果quauq_rate,quauq_other_rate,agent_rate, agent_other_rate 全为空， 则需要查询
     *      否则全部按照团期设置的汇率来取。
     *
     * @param rate
     * @return
     * @author  shijun.liu
     * @date    2016.08.11
     */
    private static boolean needQueryCompany(Rate rate){
        boolean b = false;
        //Quauq费率
        BigDecimal quauqRate = rate.getQuauqRate();
        //Quauq其他费率
        BigDecimal quauqOtherRate = rate.getQuauqOtherRate();
        //渠道费率
        BigDecimal agentRate = rate.getAgentRate();
        // 渠道其他费率
        BigDecimal agentOtherRate = rate.getAgentOtherRate();
        //表示未设置团期费率
        if(null == quauqRate && null == quauqOtherRate && null == agentRate && null == agentOtherRate){
            b = true;
            return b;
        }
        return b;
    }

    /**
     * 将Map中的数据转换为Rate对象
     * @param map   map数据对象
     * @return
     * @author  shijun.liu
     * @date    2016.08.11
     */
    public static Rate assignmentRate(Map<String, Object> map){
        Rate rate = new Rate();
        if(null == map){
            return rate;
        }
        Object quauqRateType = map.get("quauq_rate_type");
        Object quauqRate = map.get("quauq_rate");
        Object quauqOtherRateType = map.get("quauq_other_rate_type");
        Object quauqOtherRate = map.get("quauq_other_rate");
        Object agentRateType = map.get("agent_rate_type");
        Object agentRate = map.get("agent_rate");
        Object agentOtherRateType = map.get("agent_other_rate_type");
        Object agentOtherRate = map.get("agent_other_rate");
        Object chouchengRateType = map.get("chouchengRateType");
        Object chouchengRate = map.get("chouchengRate");
        rate.setQuauqRateType(Integer.valueOf(quauqRateType.toString()));
        rate.setQuauqRate(new BigDecimal(quauqRate.toString()));
        rate.setQuauqOtherRateType(Integer.valueOf(quauqOtherRateType.toString()));
        rate.setQuauqOtherRate(new BigDecimal(quauqOtherRate.toString()));
        rate.setAgentRateType(Integer.valueOf(agentRateType.toString()));
        rate.setAgentRate(new BigDecimal(agentRate.toString()));
        rate.setAgentOtherRateType(Integer.valueOf(agentOtherRateType.toString()));
        rate.setAgentOtherRate(new BigDecimal(agentOtherRate.toString()));
        if(chouchengRate != null){
        	rate.setChouchengRate(new BigDecimal(chouchengRate.toString()));
        }
        if(chouchengRateType != null){
        	rate.setChouchengRateType(Integer.parseInt(chouchengRateType.toString()));
        }
        return rate;
    }
    
    /**
     * 将Map中的数据转换为Rate对象
     * @param map   map数据对象
     * @return
     * @author  xin.li
     * @date    2016.11.17
     */
    public static Rate assignmentRate(QuauqGroupStrategy quauqGroupStrategy){
        Rate rate = new Rate();
        if(null == quauqGroupStrategy){
            return rate;
        }
        Object quauqRateType = quauqGroupStrategy.getQuauqRateType();
        Object quauqRate = quauqGroupStrategy.getQuauqRate();
        Object quauqOtherRateType = quauqGroupStrategy.getQuauqOtherRateType();
        Object quauqOtherRate = quauqGroupStrategy.getQuauqOtherRate();
        Object agentRateType = quauqGroupStrategy.getAgentRateType();
        Object agentRate = quauqGroupStrategy.getAgentRate();
        Object agentOtherRateType = quauqGroupStrategy.getAgentOtherRateType();
        Object agentOtherRate = quauqGroupStrategy.getAgentOtherRate();
        rate.setQuauqRateType(Integer.valueOf(quauqRateType.toString()));
        rate.setQuauqRate(new BigDecimal(quauqRate.toString()));
        rate.setQuauqOtherRateType(Integer.valueOf(quauqOtherRateType.toString()));
        rate.setQuauqOtherRate(new BigDecimal(quauqOtherRate.toString()));
        rate.setAgentRateType(Integer.valueOf(agentRateType.toString()));
        rate.setAgentRate(new BigDecimal(agentRate.toString()));
        rate.setAgentOtherRateType(Integer.valueOf(agentOtherRateType.toString()));
        rate.setAgentOtherRate(new BigDecimal(agentOtherRate.toString()));
        return rate;
    }

    /**
     * 查询签证产品的Quauq服务费和渠道服务费
     * @param activityId    产品ID
     * @param productType   产品类型
     * @param agentId       下单时的渠道ID
     * @return
     * @author  shijun.liu
     * @date    2016.08.10
     */
    private static Rate getVisaRate(Long activityId, Integer productType, Long agentId){
        //签证扩展需要
        return null;
    }

    /**
     * 查询机票产品的Quauq服务费和渠道服务费
     * @param activityId    产品ID
     * @param productType   产品类型
     * @param agentId       下单时的渠道ID
     * @return
     * @author  shijun.liu
     * @date    2016.08.10
     */
    private static Rate getAirticketRate(Long activityId, Integer productType, Long agentId){
        //机票扩展需要
        return null;
    }


	public static Rate getMaxRate(Long groupId, Integer productType,
			BigDecimal quauqAdultPrice) {
		return quauqGroupStrategyService.getMaxRate(groupId,productType,quauqAdultPrice);
	}
}
