/**
 * 
 */
package com.trekiz.admin.common.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.mapper.JsonMapper;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.ZipUtils;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.service.DocInfoService;

/**
 * 文件上传下载
 * 
 * @author zhaoshuli
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/fileUpd")
public class FileUpdController extends BaseController {

	@Autowired
	private DocInfoService docInfoService;

	/**
	 * 默认页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "default")
	public String defaultFileName() {
		return "common/fileUpdFile";
	}

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public String MulUploadFile(MultipartHttpServletRequest request,
			HttpServletResponse response) throws IOException {
//		System.out.println("开始上传文件");
		Map<String, MultipartFile> fileMap = request.getFileMap();
		String resultV = "";
		// 上传完成后，需要返回上传文件对象
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {

			try {
				MultipartFile file = entity.getValue();
				String originalName = file.getOriginalFilename();
				String ext = FilenameUtils.getExtension(originalName).toLowerCase(
						Locale.ENGLISH);
				int limitSize = Integer.parseInt(Global.getConfig("web.maxUploadSize"));
				 if(file.getSize()>limitSize){
					 throw new Exception("上传失败：文件大小不能超过10M");
				 }
				// 使用唯一标识码生成文件名
				String newName = UUID.randomUUID().toString() + "."
						+ (ext != null ? ext : "");
	
				File uploadFile = null;
				StringBuilder sb = new StringBuilder();
	
				sb.append(FileUtils.getUploadFilePath().get(1));
				uploadFile = new File(sb.toString());
				if (!uploadFile.exists()) {
					uploadFile.mkdirs();
				}
				uploadFile = new File(sb.toString(), newName);
				FileCopyUtils.copy(file.getBytes(), uploadFile);
				// 此处可进行数据库操作

				// 保存到DocInfo
				DocInfo doc = new DocInfo();
				doc.setDocName(originalName);
				doc.setDocPath(FileUtils.getUploadFilePath().get(0) + newName);
				Long docId = docInfoService.saveDocInfo(doc).getId();
				// System.out.println(sb.toString());
				doc.setId(docId);
				resultV= JsonMapper.getInstance().toJson(doc);
//				fileNameMap.put(docId.toString(), originalName + "="
//						+ FileUtils.getUploadFilePath().get(0) + newName);
				// System.out.println("docId=" + docId + "docInfo.id=" +
				// doc.getId().toString());
			} catch (IOException e) {
				resultV = "false";
				e.printStackTrace();
			} catch (Exception e) {
				resultV = "false";
				e.printStackTrace();
			}
		}
		return resultV;
		
	}


	@RequestMapping(value = "download/{docid}")
	public ResponseEntity<byte[]> download(@PathVariable("docid") Long docid,
			HttpServletResponse response) {
		File downFile;
		OutputStream os = null;
		try {
			DocInfo docInfo = docInfoService.getDocInfo(docid);
			if (docInfo != null) {
				downFile = new File(Global.getBasePath() + File.separator
						+ docInfo.getDocPath());
				if (downFile.exists()) {
					response.reset();
					response.setHeader(
							"Content-Disposition",
							"attachment; filename="
									+ new String(docInfo.getDocName().getBytes(
											"gb2312"), "ISO-8859-1"));
					response.setContentType("application/octet-stream; charset=utf-8");
					os = response.getOutputStream();
					os.write(FileUtils.readFileToByteArray(downFile));
					os.flush();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null)
				try {
					os.close();
				} catch (Exception e) {
				}
		}
		return null;
	}

	@RequestMapping(value = "zipdownload/{docids}/{zipname}")
	public void zipDownload(@PathVariable("docids") String docids,
			@PathVariable("zipname") String zipname,
			HttpServletResponse response) throws IOException {

		ZipUtils zip = new ZipUtils();
		OutputStream os = response.getOutputStream();
		List<Long> ids = Lists.newArrayList();
		List<DocInfo> docList = Lists.newArrayList();
		List<String> fileNameList = Lists.newArrayList();
		List<String> filePathList = Lists.newArrayList();
		try {

			if (StringUtils.isNotBlank(docids)) {
				String[] idarray = docids.split(",");
				if (idarray != null && idarray.length != 0) {
					for (String id : idarray) {
						if (StringUtils.isNotBlank(id))
							ids.add(Long.parseLong(id));
					}
				}
			}
			if (ids != null && ids.size() != 0) {
				docList = docInfoService.getDocInfoByIds(ids);
				if (docList != null && docList.size() != 0) {
					for (DocInfo doc : docList) {
						// filePathList.add(Global.getBasePath()+File.separator+doc.getDocPath());
						filePathList.add(Global.getBasePath() + File.separator
								+ doc.getDocPath());
						fileNameList.add(doc.getDocName());
					}
					zip.zip(Global.getBasePath() + File.separator
							+ java.net.URLDecoder.decode(zipname, "utf-8")
							+ ".zip", filePathList, fileNameList, true);
					response.reset();
					response.setHeader(
							"Content-Disposition",
							"attachment; filename="
									+ new String(new String(java.net.URLDecoder
											.decode(zipname, "utf-8") + ".zip")
											.getBytes("gb2312"), "ISO-8859-1"));
					response.setContentType("application/octet-stream; charset=utf-8");
					File downFile = new File(Global.getBasePath()
							+ File.separator
							+ java.net.URLDecoder.decode(zipname, "utf-8")
							+ ".zip");
					os.write(FileUtils.readFileToByteArray(downFile));
					os.flush();
					downFile.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (os != null)
				os.close();
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param docids
	 * @param zipname
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "fileExists/{docids}/{zipname}")
	public String fileExists(@PathVariable("docids") String docids,
			@PathVariable("zipname") String zipname,
			HttpServletResponse response) {
		String msg = "文件不存在";
		List<Long> ids = Lists.newArrayList();
		if (StringUtils.isNotBlank(docids)) {
			String[] idarray = docids.split(",");
			if (idarray != null && idarray.length != 0) {
				for (String id : idarray) {
					if (StringUtils.isNotBlank(id))
						ids.add(Long.parseLong(id));
				}
			}
		}

		List<DocInfo> docList = Lists.newArrayList();

		if (ids != null && ids.size() != 0) {
			docList = docInfoService.getDocInfoByIds(ids);
			if (docList != null && docList.size() != 0) {
				File file = null;
				for (DocInfo doc : docList) {
					file = new File(Global.getBasePath() + File.separator
							+ doc.getDocPath());
					if (file.exists()) {
						msg = "文件存在";
						break;
					}
				}

			}
		}

		return msg;
	}

}
