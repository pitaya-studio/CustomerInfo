package com.trekiz.admin.modules.activity.web;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

@Controller
@RequestMapping(value="${adminPath}/activity/preview")
public class ActivityPreviewController extends BaseController {

	private static final Log logger = LogFactory.getLog(ActivityPreviewController.class);
	private String SWFTools_Windows = "F:/sortware/testingsoftware/SWFTools/pdf2swf.exe -s flashversion=9 ";

	private static final int environment = 1;// 环境1：windows,2:linux(涉及pdf2swf路径问题)
	private String fileString;
	private String outputPath = "";// 输入路径，如果不设置就输出在默认位置
	private String fileName;
	private File pdfFile;
	private File swfFile;
	private File docFile;
	private File odtFile;

	@Autowired
	private DocInfoService docInfoService;

//	public ActivityPreviewController(String fileString) {
//		ini(fileString);
//	}

	/*
	 * 重新设置 file @param fileString
	 */
//	public void setFile(String fileString) {
//		ini(fileString);
//	}

	/*
	 * 初始化 @param fileString
	 */
	private void ini(String fileString, HttpServletRequest request) {
		try {
			this.fileString = fileString;
			fileString = fileString.replaceAll("\\\\", "/");
			fileName = fileString.substring(0, fileString.lastIndexOf("/"));
			docFile = new File(fileString);
			String s = fileString.substring(fileString.lastIndexOf("/") + 1,fileString.lastIndexOf("."));
			fileName = fileName + "/" + s;
			// 用于处理TXT文档转化为PDF格式乱码,获取上传文件的名称（不需要后面的格式）
			String txtName = fileString.substring(fileString.lastIndexOf("."));
			// 判断上传的文件是否是TXT文件
			if (txtName.equalsIgnoreCase(".txt")) {
				// 定义相应的ODT格式文件名称
				odtFile = new File(fileName + ".odt");
				// 将上传的文档重新copy一份，并且修改为ODT格式，然后有ODT格式转化为PDF格式
				this.copyFile(docFile, odtFile);
				pdfFile = new File(fileName + ".pdf"); // 用于处理PDF文档
			} else if (txtName.equals(".pdf") || txtName.equals(".PDF")) {
				pdfFile = new File(fileName + ".pdf");
				this.copyFile(docFile, pdfFile);
			} else {
				pdfFile = new File(fileName + ".pdf");
			}

//			System.out.println(request.getSession().getServletContext().getRealPath(""));
//			System.out.println(request.getRequestURI());
			String basePath = new File(System.getProperty("user.dir")).getParentFile().getAbsolutePath();
			String webPath = request.getContextPath();
			File dir = new File((basePath + "/webapps" + webPath + "/upload").replaceAll("\\\\", "/"));
			if (!dir.exists()) {
				dir.mkdirs();
			}
			swfFile = new File((dir + "/" + s).replaceAll("\\\\", "/") + ".swf");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: copyFile
	 * @Description: TODO
	 * @param: @param docFile2
	 * @param: @param odtFile2
	 * @return: void
	 * @author: hl
	 * @time: 2014-4-17 下午9:41:52
	 * @throws
	 */
	private void copyFile(File sourceFile,File targetFile)throws Exception{
		//新建文件输入流并对它进行缓冲 
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);
		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff  = new BufferedOutputStream(output);
		// 缓冲数组 
		byte[]b = new byte[1024 * 5];
		int len;
		while((len = inBuff.read(b)) != -1){
			outBuff.write(b,0,len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();
		// 关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}

	/*
	 * 转为PDF @param file
	 */
	private void doc2pdf() throws Exception {
		if (docFile.exists()) {
			if (!pdfFile.exists()) {
				OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
				try {
					connection.connect();
					DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
					converter.convert(docFile, pdfFile);
					// close the connection
					connection.disconnect();
					System.out.println("****pdf转换成功，PDF输出：" + pdfFile.getPath() + "****");
				} catch (java.net.ConnectException e) {
					// ToDo Auto-generated catch block
					e.printStackTrace();
					System.out.println("****swf转换异常，openoffice服务未启动！****");
					throw e;
				} catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {
					e.printStackTrace();
					System.out.println("****swf转换器异常，读取转换文件失败****");
					throw e;
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			} else {
				System.out.println("****已经转换为pdf，不需要再进行转化****");
			}
		} else {
			System.out.println("****swf转换器异常，需要转换的文档不存在，无法转换****");
		}
	}

	/*
	 * 转换成swf
	 */
	@SuppressWarnings("unused")
	private void pdf2swf() throws Exception {
		Runtime r = Runtime.getRuntime();
		if (!swfFile.exists()) {
			if (pdfFile.exists()) {
				if (environment == 1){// windows环境处理
					try {
						// 这里根据SWFTools安装路径需要进行相应更改
						Process p = r.exec(SWFTools_Windows + pdfFile.getPath() + " -o " + swfFile.getPath() + " -T 9");
						System.out.print(loadStream(p.getInputStream()));
						System.err.print(loadStream(p.getErrorStream()));
						System.out.print(loadStream(p.getInputStream()));
						System.err.println("****swf转换成功，文件输出：" + swfFile.getPath() + "****");
						if (pdfFile.exists()) {
							pdfFile.delete();
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
				} else if (environment == 2){// linux环境处理
					try {
						Process p = r.exec("pdf2swf " + pdfFile.getPath() + " -o " + swfFile.getPath() + " -T 9");
						System.out.print(loadStream(p.getInputStream()));
						System.err.print(loadStream(p.getErrorStream()));
						System.err.println("****swf转换成功，文件输出：" + swfFile.getPath() + "****");
						if (pdfFile.exists()) {
							pdfFile.delete();
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException();
					}
				}
			} else {
				System.out.println("****pdf不存在，无法转换****");
			}
		} else {
			System.out.println("****swf已存在不需要转换****");
		}
	}

	static String loadStream(InputStream in) throws IOException {
		int ptr = 0;
		//把InputStream字节流 替换为BufferedReader字符流 2013-07-17修改
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder buffer = new StringBuilder();
		while ((ptr = reader.read()) != -1) {
			buffer.append((char) ptr);
		}
		return buffer.toString();
	}

	/*
	 * 转换主方法
	 */
	public boolean conver() {
		if (swfFile.exists()) {
			System.out.println("****swf转换器开始工作，该文件已经转换为swf****");
			return true;
		}

		// 暂时注释，因environment始终等于1
//		if (environment == 1) {
			System.out.println("****swf转换器开始工作，当前设置运行环境windows****");
//		}
//		else {
//			System.out.println("****swf转换器开始工作，当前设置运行环境linux****");
//		}

		try {
			doc2pdf();
			pdf2swf();
		} catch (Exception e) {
			// TODO: Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		if (swfFile.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 返回文件路径 @param s
	 */
	public String getswfPath() {
		if (swfFile.exists()) {
			String tempString = swfFile.getPath();
			tempString = tempString.replaceAll("\\\\", "/");
			return tempString;
		} else {
			return "";
		}
	}

//	/*
//	 * 设置输出路径
//	 */
//	public void setOutputPath(String outputPath) {
//		this.outputPath = outputPath;
//		if (!outputPath.equals("")) {
//			String realName = fileName.substring(fileName.lastIndexOf("/"), fileName.lastIndexOf("."));
//			if (outputPath.charAt(outputPath.length()) == '/') {
//				swfFile = new File(outputPath + realName + ".swf");
//			} else {
//				swfFile = new File(outputPath + realName + ".swf");
//			}
//		}
//	}

	@RequestMapping(value="view")
	public String convert2swf(Long docid, Model model, HttpServletRequest request) {
		DocInfo docInfo = docInfoService.getDocInfo(docid);
		String fileString = "";
		if(docInfo != null) {
			File downFile = new File(Global.getBasePath() + File.separator + docInfo.getDocPath());
			if (downFile.exists()) {
				fileString = downFile.getAbsolutePath();
				ini(fileString, request);
				conver();
			}
		}
		System.out.println(getswfPath());
		model.addAttribute("filePath", "upload" + getswfPath().substring(getswfPath().lastIndexOf("/")));
		return "modules/activity/documentView";
//		return null;
	}

}
