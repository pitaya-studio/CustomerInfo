/**
 *
 */
package com.trekiz.admin.modules.sys.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.persistence.Pager;
import com.trekiz.admin.common.web.BaseController;

/**
 *DEVELOP_DEMO
 */

 /**
 *  文件名: PagerController.java
 *  功能:
 *      分页示例
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2013-12-2 上午10:04:48
 *  @version 1.0
 */
@Controller
public class PagerController extends BaseController{
    
    private static Logger logger = LoggerFactory.getLogger(PagerController.class);
	
	 /**
	 *  功能:
	 * 请求分页测试页面
	 *  @author xuziqian
	 *  @DateTime 2013-12-2 上午10:08:13
	 *  @param request
	 *  @param response
	 *  @param model
	 *  @return
	 */
	@RequestMapping(value = "${adminPath}/page", method = RequestMethod.GET)
	public String doPage(HttpServletRequest request, HttpServletResponse response, Model model) {
	    return "modules/sys/pageIndex";
	}
	
	 /**
	 *  功能:
	 *   获取分页数据   返回pager对象
	 *  @author xuziqian
	 *  @DateTime 2013-11-28 下午8:41:28
	 *  @param pagenumber  当前页面
	 *  @param pagecount   总页数
	 *  @param pageRecordNum  每页记录条数
	 *  @return
	 */
	@RequestMapping(value = "${adminPath}/getData")
	@ResponseBody
	public Object doGetData(@RequestParam("pagenumber") String pagenumber,
	        @RequestParam("pagecount") String pagecount,
	        @RequestParam("pageRecordNum") String pageRecordNum,HttpServletRequest request) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        int i=9;
        while(i>0){
            Map<String, String> map = new HashMap<String, String>();
            map.put("pagenumber", pagenumber+i);
            map.put("pagecount", pagecount+i);
            map.put("pageRecordNum", pageRecordNum+i);
            list.add(map);
            i--;
        }
        logger.debug("分页测试");
        return new Pager<Map<String, String>>(pagenumber, "99", pageRecordNum,"88", list);
	}
	
}
