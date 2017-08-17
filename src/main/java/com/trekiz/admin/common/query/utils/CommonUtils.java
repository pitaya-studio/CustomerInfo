package com.trekiz.admin.common.query.utils;

import com.trekiz.admin.common.query.entity.SelectOption;
import com.trekiz.admin.common.utils.Collections3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 工具类
 * @author shijun.liu
 * @date   2016.04.09
 */
public class CommonUtils {

    /**
     * 将list的数据转换成SelectOption对象
     * @param list
     * @return
     * @author  shijun.liu
     * @date    2016.04.09
     */
    public static List<SelectOption> toListSelectOption(List<Map<String, Object>> list){
        List<SelectOption> dataList = new ArrayList<SelectOption>();
        if(Collections3.isEmpty(list)){
            return dataList;
        }
        for (Map<String, Object> map : list){
            SelectOption option = new SelectOption();
            String id = map.get("id") == null ? "": String.valueOf(map.get("id"));
            String text = map.get("text") == null ? "": String.valueOf(map.get("text"));
            option.setId(id);
            option.setText(text);
            dataList.add(option);
        }
        return dataList;
    }
}
