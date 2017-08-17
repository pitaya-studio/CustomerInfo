/**
 *
 */
package com.trekiz.admin.modules.sys.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.ZipUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.pay.entity.PayHotelOrder;
import com.trekiz.admin.modules.pay.entity.PayIslandOrder;
import com.trekiz.admin.modules.pay.service.PayHotelOrderService;
import com.trekiz.admin.modules.pay.service.PayIslandOrderService;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 区域Controller
 * @author zj
 * @version 2013-11-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/docinfo")
public class DocInfoController extends BaseController {

	@Autowired
	private DocInfoService docInfoService;
	@Autowired
	private PayIslandOrderService payIslandOrderService;
	@Autowired
	private PayHotelOrderService payHotelOrderService;
	
    @RequestMapping(value="download/{docid}")
    public ResponseEntity<byte[]> download(@PathVariable("docid") Long docid,HttpServletResponse response) throws Exception {
    	File downFile;
    	OutputStream os = null;
    	try {
    		DocInfo docInfo = docInfoService.getDocInfo(docid);
    		if(docInfo!=null){
    			downFile = new File(Global.getBasePath() +File.separator+docInfo.getDocPath());
    			if(downFile.exists()){
    				response.reset();
					//解决文件名中包含空格问题
    				response.setHeader("Content-Disposition", "attachment; filename=\""+new String(docInfo.getDocName().getBytes("gb2312"), "ISO-8859-1")+"\"");
    				response.setContentType("application/octet-stream; charset=utf-8");
    		    	os = response.getOutputStream();
    				os.write(FileUtils.readFileToByteArray(downFile));
    	            os.flush();
    			}else{
    				throw new FileNotFoundException();
    			}
    		}else{
    			throw new FileNotFoundException();
    		}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(os!=null)
				try {
					os.close();
				} catch (Exception e) {
				}
		}
		return null;
    }
    
    @RequestMapping(value="zipdownload/{docids}/{zipname}")
    public void zipDownload(@PathVariable("docids")String docids,@PathVariable("zipname")String zipname,HttpServletResponse response) throws IOException {
    	   	
    	ZipUtils zip = new ZipUtils();
    	OutputStream os = response.getOutputStream();
    	List<Long> ids = Lists.newArrayList();
    	List<DocInfo> docList = Lists.newArrayList();
    	List<String> fileNameList = Lists.newArrayList();
    	List<String> filePathList = Lists.newArrayList();
    	try {
    		
    		if(StringUtils.isNotBlank(docids)){
    			String[] idarray = docids.split(",");
    			if(idarray!=null && idarray.length!=0){
    				for(String id:idarray){
    					if(StringUtils.isNotBlank(id))
    						ids.add(Long.parseLong(id));
    				}
    			}
    		}
    		if(ids!=null && ids.size()!=0){
    			docList = docInfoService.getDocInfoByIds(ids);
    			if(docList!=null && docList.size()!=0){
    				for(DocInfo doc:docList){
    					//filePathList.add(Global.getBasePath()+File.separator+doc.getDocPath());
    					filePathList.add(Global.getBasePath() +File.separator+doc.getDocPath());
    					fileNameList.add(doc.getDocName());
    				}
    				zip.zip(Global.getBasePath()+File.separator+java.net.URLDecoder.decode(zipname, "utf-8")+".zip", filePathList, fileNameList, true);
    				response.reset();
    				response.setHeader("Content-Disposition", "attachment; filename="+new String(new String(java.net.URLDecoder.decode(zipname, "utf-8")+".zip").getBytes("gb2312"), "ISO-8859-1"));
    				response.setContentType("application/octet-stream; charset=utf-8");
    				File downFile = new File(Global.getBasePath()+File.separator+java.net.URLDecoder.decode(zipname, "utf-8")+".zip");
    				os.write(FileUtils.readFileToByteArray(downFile));
    	            os.flush();
    	            downFile.delete();
        		}
    		}else{
    			throw new FileNotFoundException();
    		}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (os != null)
              os.close();
		}
    }
    
    /*
     * 确认单批量下载
     * @param orderIds   订单号
     * @param docIds     确认单号
     * added by xianglei.dong
     * updated by xianglei.dong 2016-03-30  优化
     */
    @RequestMapping(value="zipconfirmdownload/{orderIds}/{docIds}")
    public void zipConfirmDownload(@PathVariable("orderIds")String orderIds, @PathVariable("docIds")String docIds, HttpServletResponse response) throws IOException {    	
    	if(StringUtils.isBlank(docIds)) {
    		return;
    	}
    	//去除逗号
    	String order_ids = orderIds.substring(1);
    	String doc_ids = docIds.substring(1);
    	    	
    	Map<Long, String> docId_OrderId = Maps.newHashMap();
    	List<Long> ids = Lists.newArrayList();
    	if(StringUtils.isNotBlank(doc_ids)) {
			String[] docIdArray = doc_ids.split(",");
			if (docIdArray != null && docIdArray.length > 0) {
				String[] orderIdArray = null;
				if(StringUtils.isNotBlank(order_ids)) {
					orderIdArray = order_ids.split(",");
				}
				for (int i = 0; i < docIdArray.length; i++) {
					if (StringUtils.isNotBlank(docIdArray[i])) {
						Long docId = Long.parseLong(docIdArray[i]);
						ids.add(docId);
						if (orderIdArray != null && orderIdArray.length > i) {
							docId_OrderId.put(docId, orderIdArray[i]);
						} else {
							docId_OrderId.put(docId, "");
						}
					}
				}
			}
    	}
    	//获取新的文件名和文件路径
    	List<String> fileNameList = Lists.newArrayList();    //记录待压缩的文件名
    	List<String> filePathList = Lists.newArrayList();    //记录待压缩的文件存储路径
    	if(ids.size()>0) {
        	List<DocInfo> docList = docInfoService.getDocInfoByIds(ids);
        	if(docList!=null && docList.size()>0) {
        		for(DocInfo doc : docList) {
        			String orderId = docId_OrderId.get(doc.getId());					
					String fileName = "";      //文件名称
					String dotName = "";       //文件扩展名
					String fileFullName = doc.getDocName();
					int dot = fileFullName.lastIndexOf(".");
					if ((dot>-1) && (dot<(fileFullName.length()-1))) { 
						fileName = fileFullName.substring(0, dot);
						dotName = fileFullName.substring(dot + 1);
					}
					fileFullName = fileName + "-" + orderId + "." + dotName;
					//解决文件重名问题
					if(fileNameList.contains(fileFullName)) {
						fileFullName = fileName + "-" + orderId + "-" + doc.getId().toString() + "." + dotName; //文件重名加上docId
					}
					fileNameList.add(fileFullName);
					filePathList.add(Global.getBasePath()+File.separator+doc.getDocPath());   
            	}
        	}        	
    	}
    	
    	//下载
    	if(fileNameList.size()>0 && filePathList.size()>0) {
    		ZipUtils zip = new ZipUtils();
        	OutputStream os = response.getOutputStream();
        	try{
        		
        		//压缩包文件名：确认单+下载日期
				String zipName = "确认单-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				//压缩
        		zip.zip(Global.getBasePath()+File.separator+java.net.URLDecoder.decode(zipName, "utf-8")+".zip", filePathList, fileNameList, true);
				response.reset();
				response.setHeader("Content-Disposition", "attachment; filename="+new String(new String(java.net.URLDecoder.decode(zipName, "utf-8")+".zip").getBytes("gb2312"), "ISO-8859-1"));
				response.setContentType("application/octet-stream; charset=utf-8");
				File downFile = new File(Global.getBasePath()+File.separator+java.net.URLDecoder.decode(zipName, "utf-8")+".zip");
				os.write(FileUtils.readFileToByteArray(downFile));
	            os.flush();
	            downFile.delete();
        	}catch (Exception e) {
    			e.printStackTrace();
    		} finally{			
    			if (os != null)
                  os.close();
    		}
    	}else{
    		throw new FileNotFoundException();
    	}
    }
    
    /**
     * 判断文件是否存在
     * @param docids
     * @param zipname
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value="fileExists/{docids}/{zipname}")
    public String fileExists(@PathVariable("docids")String docids,@PathVariable("zipname")String zipname,HttpServletResponse response){
    	String msg = "文件不存在";
    	List<Long> ids = Lists.newArrayList();
    	if(StringUtils.isNotBlank(docids)){
			String[] idarray = docids.split(",");
			if(idarray!=null && idarray.length!=0){
				for(String id:idarray){
					if(StringUtils.isNotBlank(id))
						ids.add(Long.parseLong(id));
				}
			}
		}
    	
    	List<DocInfo> docList = Lists.newArrayList();
    	
    	if(ids!=null && ids.size()!=0){
			docList = docInfoService.getDocInfoByIds(ids);
			if(docList!=null && docList.size()!=0){
				File file = null;
				for(DocInfo doc:docList){
					file = new File(Global.getBasePath() +File.separator+doc.getDocPath());
					if(file.exists()){
						msg = "文件存在";
						break;
					}
				}
			
    		}
		}   		
    	
    	return msg;
    }
    
    
    @ResponseBody
    @RequestMapping(value ="payVoucherList/{orderId}")
	public List<DocInfo> orderDetail(@PathVariable String orderId,Model model,HttpServletRequest request) {
    	String orderType = request.getParameter("orderType");
	    List<DocInfo> list = new ArrayList<DocInfo>();
	    //海岛游或酒店
	    if (StringUtils.isNotBlank(orderType)) {
	    	//海岛游
	    	if (Context.ORDER_TYPE_ISLAND.toString().equals(orderType)) {
	    		PayIslandOrder orderPay = payIslandOrderService.getById(Integer.parseInt(orderId));
	    		if (orderPay != null && StringUtils.isNotBlank(orderPay.getPayVoucher())) {
	    			List<Long> docList = Lists.newArrayList();
	    			for (String docId : orderPay.getPayVoucher().split(",")) {
	    				if (StringUtils.isNotBlank(docId)) {
	    					docList.add(Long.parseLong(docId));
	    				}
	    			}
	    			list = docInfoService.getDocInfoByIds(docList);
	    		}
	    	} 
	    	//酒店
	    	else if (Context.ORDER_TYPE_HOTEL.toString().equals(orderType)) {
	    		PayHotelOrder orderPay = payHotelOrderService.getById(Integer.parseInt(orderId));
	    		if (orderPay != null && StringUtils.isNotBlank(orderPay.getPayVoucher())) {
	    			List<Long> docList = Lists.newArrayList();
	    			for (String docId : orderPay.getPayVoucher().split(",")) {
	    				if (StringUtils.isNotBlank(docId)) {
	    					docList.add(Long.parseLong(docId));
	    				}
	    			}
	    			list = docInfoService.getDocInfoByIds(docList);
	    		}
	    	}
	    } else {
	    	if (StringUtils.isNotBlank(orderId)) {
		        list = docInfoService.getDocInfoList(Long.parseLong(orderId));
		    }
	    }
	    model.addAttribute("docInfo",list);
	    return list;
	}
    
    @RequestMapping(value="getFile")
    public String getFile(HttpServletRequest request,HttpServletResponse response,Model model){
    	List<Long> docList = Lists.newArrayList();//附件Id
    	List<DocInfo> list = new ArrayList<DocInfo>();
    	String id = request.getParameter("docId");
    	if(StringUtils.isNotBlank(id)){
    		for(String docId:id.split(",")){
    			docList.add(Long.valueOf(docId));
    		}
    	}
    	if(CollectionUtils.isNotEmpty(docList)){
    		list = docInfoService.getDocInfoByIds(docList);
    	}
    	model.addAttribute("docInfo", list);
    	return "modules/order/docInfo";
    }
}
