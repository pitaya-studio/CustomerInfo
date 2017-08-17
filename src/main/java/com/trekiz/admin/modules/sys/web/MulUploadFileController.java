package com.trekiz.admin.modules.sys.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.sun.star.uno.RuntimeException;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.ImageUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.service.DocInfoService;

/*
 * author xiaobing wen
 * 
 * 2014-09-16
 * 
 */
@Controller
@RequestMapping(value="${adminPath}/MulUploadFile")
public class MulUploadFileController{

	@Autowired
	private DocInfoService docInfoService;
	
//	private static final String mulUploadFileServer=Global.getConfig("mulUploadFileServer");
//	private static final String mulUploadFileFolder=Global.getConfig("mulUploadFileFolder");
	
	@RequestMapping(value="upload",method=RequestMethod.POST)
	@ResponseBody
	public void MulUploadFile (MultipartHttpServletRequest request,HttpServletResponse response) throws IOException{
//		 System.out.println("开始上传文件");
		 Map<String, MultipartFile> fileMap=request.getFileMap();
		 //List<String> newFileNameList=new ArrayList<String>();
		 Map<String,String> fileNameMap=new HashMap<String,String>();
		 PrintWriter out = response.getWriter();
		 //上传完成后，需要返回上传文件对象
		 // 是否为图片文件添加水印
		 boolean waterLogo = "addFlag".equals(request.getParameter("addFlag"));
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()){
			 
			 MultipartFile file = entity.getValue();
			 String originalName = file.getOriginalFilename();  
	         String ext = FilenameUtils.getExtension(originalName).toLowerCase(Locale.ENGLISH); 
	         
	         //使用唯一标识码生成文件名
	         String newName = UUID.randomUUID().toString()+"."+(ext!=null?ext:"");
	         
	         File uploadFile = null;
	         try {
	        	 if (!waterLogo) {
					StringBuilder sb = new StringBuilder();
					sb.append(FileUtils.getUploadFilePath().get(1));
					uploadFile = new File(sb.toString());
					if (!uploadFile.exists()) {
						uploadFile.mkdirs();
					}
					uploadFile = new File(sb.toString(), newName);
					FileCopyUtils.copy(file.getBytes(), uploadFile);
				} else {
					if(!ext.equals("png") && !ext.equals("jpg") && !ext.equals("gif")){
						throw new RuntimeException("请上传图片格式");
					}
					String path = request.getSession().getServletContext().getRealPath("/") + "/static/logo/logo-water.png";
					// 添加水印
					ImageUtils.markImageByIcon(path, file, 1, FileUtils.getUploadFilePath().get(1), newName);
				}
				//此处可进行数据库操作
                 //保存到DocInfo
                 DocInfo doc = new DocInfo();
                 doc.setDocName(originalName);
                 doc.setDocPath(FileUtils.getUploadFilePath().get(0) + newName);
                 Long docId = docInfoService.saveDocInfo(doc).getId();
//                 System.out.println(sb.toString());
                 fileNameMap.put(docId.toString(), originalName + "=" + FileUtils.getUploadFilePath().get(0) + newName);
 //                 System.out.println("docId=" + docId + "docInfo.id=" + doc.getId().toString());
	         } catch (IOException e) {
	        	 out.println("false");
	             e.printStackTrace();  
	         } catch (Exception e) {
	        	 out.println("false");
	        	 e.printStackTrace();
	         }
		 }
		 response.getWriter().println(fileNameMap);
		 try{
			 out.close();
		 }catch(Exception e){
			 e.printStackTrace();
		 }finally{
			 if(out!=null){
				 out.close();
			 }
		 }
	}


	@RequestMapping(value="ajaxFileUpload",method=RequestMethod.POST)
	@ResponseBody
	public void ajaxFileUpload (MultipartHttpServletRequest request,HttpServletResponse response){
		Map<String, MultipartFile> fileMap = request.getFileMap();
		Map<String,String> fileNameMap = new HashMap<String,String>();
		PrintWriter out = null;
		try{
			out = response.getWriter();
			response.setContentType("text/json;charset=utf-8");
			//上传完成后，需要返回上传文件对象
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()){
				MultipartFile file=entity.getValue();
				String originalName = file.getOriginalFilename();
				String ext = FilenameUtils.getExtension(originalName).toLowerCase(Locale.ENGLISH);
				//使用唯一标识码生成文件名
				String newName = UuidUtils.generUuid()+"."+(ext!=null?ext:"");

				StringBuilder sb = new StringBuilder();

				sb.append(FileUtils.getUploadFilePath().get(1));
				File uploadFile = new File(sb.toString());
				if(!uploadFile.exists()){
					uploadFile.mkdirs();
				}
				uploadFile = new File(sb.toString(), newName);
				FileCopyUtils.copy(file.getBytes(), uploadFile);
				//保存到DocInfo
				DocInfo doc = new DocInfo();
				doc.setDocName(originalName);
				doc.setDocPath(FileUtils.getUploadFilePath().get(0) + newName);
				Long docId = docInfoService.saveDocInfo(doc).getId();
				fileNameMap.put("id", docId.toString());
				fileNameMap.put("fileName", originalName);
			}
			String json = JSON.toJSONString(fileNameMap);
			out.println(json);
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(null != out){
				out.close();
			}
		}
	}

	/**
	 * 多文件上传
	 */
	@RequestMapping("uploadFilesPage")
	public String getUploadFilesPage(@RequestParam(value="isSimple",required=false)String isSimple) {
		if(StringUtils.isNotBlank(isSimple) && "true".equals(isSimple)) {
			return "include/mulUploadFileSimple";
		}else{
			return "include/mulUploadFile";
		}
	}
	
	@RequestMapping("uploadFilesPage1")
	public String getUploadFilesPage1(Model model) {
			model.addAttribute("addFlag", "addFlag");
			return "include/mulUploadFile";
	}

	/**
	 * 获取前端可以剪裁插件传来的图片文件。为两个文件，一个是剪裁过的文件，一个原图
	 * @param request
	 * @return
	 * @throws IOException
	 * @author yudong.xu 2016.9.28
     */
	@RequestMapping(value="uploadPicture",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> uploadPicture(MultipartHttpServletRequest request) throws IOException{
		Map<String, MultipartFile> fileMap = request.getFileMap();
		Map<String,Object> fileNameMap = new HashMap<>();

		String originalName = null;
		MultipartFile wantedFile = null;

		// 遍历前端传来的2个文件，一个是经过剪裁后的，一个是原图。获取原图的文件名和剪裁后的文件。
		for (MultipartFile file : fileMap.values()) {
			if (file.getName().equals("__source")){
				originalName = file.getOriginalFilename();
				originalName = FilenameUtils.removeExtension(originalName) + ".jpg";
			}else {
				wantedFile = file;
			}
		}

		//使用唯一标识码生成文件名
		String newName = UUID.randomUUID().toString()+".jpg";
		String parentPath = FileUtils.getUploadFilePath().get(1);
		File uploadFile = new File(parentPath);
		if(!uploadFile.exists()){
			uploadFile.mkdirs();
		}
		uploadFile = new File(parentPath, newName);

		// 复制文件到目标文件夹，并把相关数据保存到数据库，最终返回保存的信息到前端
		try {
			FileCopyUtils.copy(wantedFile.getBytes(), uploadFile);
			//保存到DocInfo
			DocInfo doc = new DocInfo();
			doc.setDocName(originalName);
			doc.setDocPath(FileUtils.getUploadFilePath().get(0) + newName);
			Long docId = docInfoService.saveDocInfo(doc).getId();
			fileNameMap.put("result",docId + "=" + originalName + "=" + FileUtils.getUploadFilePath().get(0) + newName);
			fileNameMap.put("success",true);
		} catch (IOException e) {
			fileNameMap.put("success",false);
			e.printStackTrace();
		} finally {
			return fileNameMap;
		}
	}
}
