package com.trekiz.admin.common.servlet;


import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Servlet工具类
 * @author shijun.liu
 *
 */
public class ServletUtil extends HttpServlet{

	private static final long serialVersionUID = 1L;

	/**
	 * 向客户端输出Json格式的数据
	 * @param response
	 * @param json
	 * @author shijun.liu
	 */
	public static void print(HttpServletResponse response, String json){
		PrintWriter out = null;
		if(null == json){
			throw new RuntimeException("json 不能为空");
		}
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json;charset=utf-8");
			out = response.getWriter();
			out.write(json);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			IOUtils.closeQuietly(out);
		}
	}
	
	/**
	 * 下载Excel文件(文件比较小)
	 * @param response
	 * @param fileName		下载之后的文件名称
	 * @param workBook      Excel文件
	 * @author shijun.liu
	 */
	public static void downLoadExcel(HttpServletResponse response, String fileName, Workbook workBook){
		if(StringUtils.isBlank(fileName) || StringUtils.isEmpty(fileName)){
			throw new RuntimeException("文件名称不能为空");
		}
		response.reset();
		try {
			response.setHeader("Content-Disposition", "attachment; filename="
								+new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
			response.setContentType("application/octet-stream; charset=utf-8");
			workBook.write(response.getOutputStream());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("系统不支持该文件名称的编码");
		}catch (IOException e) {
			throw new RuntimeException("文件下载异常");
		}
	}
	
	/**
	 * 下载文件
	 * @param response
	 * @param downloadFileName	下载之后文件名称
	 * @param filePath			要下载的文件路径
	 * @author shijun.liu
	 */
	public static void downLoadFile(HttpServletResponse response, String downloadFileName, String filePath){
		if(StringUtils.isBlank(downloadFileName) || StringUtils.isEmpty(downloadFileName)){
			throw new RuntimeException("文件名称不能为空");
		}
		if(StringUtils.isBlank(filePath) || StringUtils.isEmpty(filePath)){
			throw new RuntimeException("所下载的文件不存在");
		}
		File file = new File(filePath);
		if(!file.exists()){
			throw new RuntimeException("所下载的文件不存在");
		}
		response.reset();
		BufferedInputStream in = null;
		ServletOutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[1024 * 1000];
			int index = 0;
			response.setHeader("Content-Disposition", "attachment; filename="
					+new String(downloadFileName.getBytes("gb2312"), "ISO-8859-1"));
			response.setContentType("application/octet-stream; charset=utf-8");
			out = response.getOutputStream();
			while((index = in.read(buffer)) != -1){
				out.write(buffer, 0, index);
				out.flush();
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("系统不支持该文件名称的编码");
		}catch (IOException e) {
			throw new RuntimeException("文件下载异常");
		}finally{
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
	}


	/**
	 * 下载文件
	 * @param response
	 * @param downloadFile  要下载的文件
	 * @author shijun.liu
	 */
	public static void downLoadFile(HttpServletResponse response, File downloadFile){
		if(!downloadFile.exists()){
			throw new RuntimeException("所下载的文件不存在");
		}
		response.reset();
		BufferedInputStream in = null;
		ServletOutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(downloadFile));
			response.setHeader("Content-Disposition", "attachment; filename="
					+new String(downloadFile.getName().getBytes("gb2312"), "ISO-8859-1"));
			response.setContentType("application/octet-stream; charset=utf-8");
			out = response.getOutputStream();
			byte[] buffer = new byte[1024 * 1000];
			int index = -1;
			while((index = in.read(buffer)) != -1){
				out.write(buffer, 0, index);
				out.flush();
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("系统不支持该文件名称的编码");
		}catch (IOException e) {
			throw new RuntimeException("文件下载异常");
		}finally{
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
	}
}
