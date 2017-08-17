package com.trekiz.admin.common.utils.word;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.common.utils.FileUtils;

/**
 * 
 * Word文档下载工具类
 * @author shijun.liu
 * @date   2015.04.17
 */
public class WordDownLoadUtils {

	private static final String FILENAME_ENCODING = "gb2312";
	
	/**
	 * 以附件的形式下载word文档
	 * @param downLoadFile       需要下载的文件
	 * @param fileName           下载后文件名称
	 * @param response           response对象
	 * @author shijun.liu
	 * @date   2015.04.17
	 */
	public static void downLoadWordByAttachment(File downLoadFile, String fileName, HttpServletResponse response){
		OutputStream os = null;
		if(null == downLoadFile){
			throw new RuntimeException("文件不存在");
		}
		if(StringUtils.isBlank(fileName) || StringUtils.isEmpty(fileName)){
			throw new RuntimeException("文件名称不能为空");
		}
		response.reset();
		try {
			response.setHeader("Content-Disposition", "attachment; filename="
								+new String(fileName.getBytes(FILENAME_ENCODING), "ISO-8859-1"));
			response.setContentType("application/octet-stream; charset=utf-8");
			os = response.getOutputStream();
			os.write(FileUtils.readFileToByteArray(downLoadFile));
			os.flush();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("系统不支持该文件名称的编码");
		}catch (IOException e) {
			throw new RuntimeException("文件下载异常");
		}finally{
			if(null != os){
				try {
					os.close();
				} catch (IOException e) {
					throw new RuntimeException("文件流关闭异常");
				}
			}
		}
	}
}
