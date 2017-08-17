package com.trekiz.admin.modules.airticket.utils;

/**
 * 机票产品工具类
 */
public class AirTicketUtils {

    /**
     * 返回机票类型
     * @param type
     * @return
     * @author shijun.liu
     * @date 2016.07.27
     */
    public static String getAirType(int type){
        String typeValue = "";
        if(1 == type){
            typeValue = "多段";
        }else if(2 == type){
          typeValue = "往返";
        }else if(3 == type){
          typeValue = "单程";
        }
        return typeValue;
    }
}
