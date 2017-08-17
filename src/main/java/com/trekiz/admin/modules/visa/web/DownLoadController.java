package com.trekiz.admin.modules.visa.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.visa.service.VisaOrderService;

/**
 * 下载文件护照领取单和导出约签表Controller
 * @author wenjianye
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/order/download")
public class DownLoadController extends BaseController {
	@Autowired
	private VisaOrderService visaOrderService;
	/**
	 * 下载护照领取单
	 * 
	 * */
	@ResponseBody
	@RequestMapping(value={"download"})
	 public HttpServletResponse hzlqdDownload(  HttpServletResponse response,HttpServletRequest request) {
	        try {
	        	String fileName = request.getParameter("fileName");
	        	//HttpSession session = request.getSession();      
	        	//ServletContext  application  = session.getServletContext();    
	        	//String serverRealPath = application.getRealPath("/") ;
	        	
	        	//String path = serverRealPath+"\\download\\"+fileName;
	        	//String path = serverRealPath+"\\userfiles\\activity\\"+fileName;
	        	String path = DownLoadController.class.getResource("/").getPath()+"word"+File.separator+fileName;
	            // path是指欲下载的文件的路径。
	            File file = new File(path);
	            // 取得文件名。
	            String filename = file.getName();
	            // 以流的形式下载文件。
	            InputStream fis = new BufferedInputStream(new FileInputStream(path));
	            byte[] buffer = new byte[fis.available()];
	            fis.read(buffer);
	            fis.close();
	            // 清空response
	            response.reset();
	            // 设置response的Header
	            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
	            response.addHeader("Content-Length", "" + file.length());
	            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
	            response.setContentType("application/octet-stream");
	            toClient.write(buffer);
	            toClient.flush();
	            toClient.close();
	            return null;
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	        return response;
	    }
	/**
	 * 导出预审表
	 * @param group_id
	 */
	@ResponseBody
	@RequestMapping(value={"exportExcel"})
	public HttpServletResponse exportExcel( HttpServletRequest req, HttpServletResponse resp) {
		String orderId = req.getParameter("orderId");
		if(null == orderId){
			return null;
		}
		try {
			
			List<Object[]> resultList = visaOrderService.findOrderTableByOrderId(orderId);
			
			List<Object[]> list = new ArrayList<Object[]>();
			for(int i=0;i<resultList.size();i++){
				Object[] o = resultList.get(i);
				
				String sex = "";
				if(null == o[2] || "".equals(o[2])){
					sex ="";
				}else if("1".equals(o[2].toString())){
					sex ="男";
				}else{
					sex ="女";
				}
				
				Object riqi ="";
				String shijian= "";
				if(null != o[3] && !"".equals(o[3])){
					if(null != ((Object[])o[3])[0] && !"".equals(((Object[])o[3])[0])){
						riqi=((Object[])o[3])[0];
					}
					if(null != ((Object[])o[3])[1] && !"".equals(((Object[])o[3])[1])){
						shijian = ((String)(((Object[])o[3])[1]));
						shijian =shijian.trim().substring(0, 8);
					}
				}

				Object[] obj = new Object[] {
						o[0],//姓名
						"",//ID
						o[1],//英文名
						sex,//性别
						riqi,//日期
						shijian,//时间
						o[4],//办签国家
						o[6],//领区
						o[5],//签证类型
						"",
						"护照",
						o[7],
						o[8],
						"","","","","","","","",
						o[9],
						"","",
						o[11],
						o[12]};
				list.add(obj);
			}
			//文件名称
			String fileName = "预审表";
			//Excel各行名称
			String[] cellTitle =  {"姓名","ID","英文名","性别","约签日期","约签时间","约签国家","领区","签证类型","签证费收据号","证件","护照号","出生日期","工作单位部门","职务","英文职务名称","去过国家","是否拒签","是否有亲属在约签国家","移动电话","关系","销售","送签人","签证结果","AA码","制表人"};
			//文件首行标题
			String firstTitle = "";
			ExportExcel.createExcle(fileName, list, cellTitle, firstTitle, req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 导出预约表
	 * @param group_id
	 */
	@ResponseBody
	@RequestMapping(value={"exportYuyueExcel"})
	public HttpServletResponse exportYuyueExcel( HttpServletRequest req, HttpServletResponse resp) {
		String orderId = req.getParameter("orderId");
		if(null == orderId){
			return null;
		}
		try {
			
			List<Object[]> resultList = visaOrderService.findOrderTableByOrderId(orderId);
			
			List<Object[]> list = new ArrayList<Object[]>();
			for(int i=0;i<resultList.size();i++){
				Object[] o = resultList.get(i);
				
				String sex = "";
				if(null == o[2] || "".equals(o[2])){
					sex ="";
				}else if("1".equals(o[2].toString())){
					sex ="男";
				}else{
					sex ="女";
				}
				
				Object riqi ="";
				String shijian= "";
				if(null != o[3] && !"".equals(o[3])){
					if(null != ((Object[])o[3])[0] && !"".equals(((Object[])o[3])[0])){
						riqi=((Object[])o[3])[0];
					}
					if(null != ((Object[])o[3])[1] && !"".equals(((Object[])o[3])[1])){
						shijian = ((String)(((Object[])o[3])[1]));
						shijian =shijian.trim().substring(0, 8);
					}
				}

				Object[] obj = new Object[] {
						o[0],//姓名
						o[1],//英文名
						sex,//性别
						riqi,//日期
						shijian,//时间
						o[4],//办签国家
						o[6],//领区
						o[5],//签证类型
						"护照",
						o[7],
						o[8],
						o[9],
						o[11],
						o[12]};
				list.add(obj);
			}
			//文件名称
			String fileName = "预约表";
			//Excel各行名称
			String[] cellTitle =  {"姓名","英文名","性别","约签日期","约签时间","约签国家","领区","签证类型","证件","护照号","出生日期","销售","送签人","AA码","制表人"};
			//文件首行标题
			String firstTitle = "";
			ExportExcel.createExcle(fileName, list, cellTitle, firstTitle, req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//文件打包下载
    public static HttpServletResponse downLoadFiles(List<File> files,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            /**这个集合就是你想要打包的所有文件，
             * 这里假设已经准备好了所要打包的文件*/
            //List<File> files = new ArrayList<File>();
        	HttpSession session = request.getSession();      
        	ServletContext  application  = session.getServletContext();    
        	String serverRealPath = application.getRealPath("/") ;
        	String path = serverRealPath+"\\userfiles\\activity\\mianqian.zip";
            /**创建一个临时压缩文件，
             * 我们会把文件流全部注入到这个文件中
             * 这里的文件你可以自定义是.rar还是.zip*/
            File file = new File(path);
            if (!file.exists()){   
                file.createNewFile();   
            }
            response.reset();
            //response.getWriter()
            //创建文件输出流
            FileOutputStream fous = new FileOutputStream(file);   
            /**打包的方法我们会用到ZipOutputStream这样一个输出流,
             * 所以这里我们把输出流转换一下*/
//            org.apache.tools.zip.ZipOutputStream zipOut 
//                = new org.apache.tools.zip.ZipOutputStream(fous);
           ZipOutputStream zipOut 
            = new ZipOutputStream(fous);
            /**这个方法接受的就是一个所要打包文件的集合，
             * 还有一个ZipOutputStream*/
           zipFile(files, zipOut);
            zipOut.close();
            fous.close();
           return downloadZip(file,response);
        }catch (Exception e) {
                e.printStackTrace();
            }
            /**直到文件的打包已经成功了，
             * 文件的打包过程被我封装在FileUtil.zipFile这个静态方法中，
             * 稍后会呈现出来，接下来的就是往客户端写数据了*/
           // OutputStream out = response.getOutputStream();
           
     
        return response ;
    }

  /**
     * 把接受的全部文件打成压缩包 
     * @param List<File>;  
     * @param org.apache.tools.zip.ZipOutputStream  
     */
    public static void zipFile
            (List<File> files,ZipOutputStream outputStream) {
        int size = files.size();
        for(int i = 0; i < size; i++) {
            File file = (File) files.get(i);
            zipFile(file, outputStream);
        }
    }

    public static HttpServletResponse downloadZip(File file,HttpServletResponse response) {
        try {
        // 以流的形式下载文件。
        InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        response.reset();

        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
        } catch (IOException ex) {
        ex.printStackTrace();
        }finally{
             try {
                    File f = new File(file.getPath());
                    f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return response;
    }

/**  
     * 根据输入的文件与输出流对文件进行打包
     * @param File
     * @param org.apache.tools.zip.ZipOutputStream
     */
    public static void zipFile(File inputFile,
            ZipOutputStream ouputStream) {
        try {
            if(inputFile.exists()) {
                /**如果是目录的话这里是不采取操作的，
                 * 至于目录的打包正在研究中*/
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    //org.apache.tools.zip.ZipEntry
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    ouputStream.putNextEntry(entry);
                    // 向压缩文件中输出数据   
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        ouputStream.write(buffer, 0, nNumber);
                    }
                    // 关闭创建的流对象   
                    bins.close();
                    IN.close();
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        if (files !=null && files.length > 0) {
                            for (int i = 0; i < files.length; i++) {
                                zipFile(files[i], ouputStream);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//	@Autowired
//	private VisaOrderService visaOrderService;
	/**
	 * 下载办签通知
	 * visaId 签证订单的主键id
	 * travalerId游客的主键id
	 * @throws TemplateException 
	 * @throws IOException 
	 * */
//	@ResponseBody
//	@RequestMapping(value={"downloadBanqian"})
//	 public Object downloadBanqian(HttpServletResponse response,HttpServletRequest request) throws IOException, TemplateException {
//		String visaId = request.getParameter("visaId");
//		visaId="12";
//		String travalerId = request.getParameter("travalerId");
//		if(null == visaId || "".equals(visaId)){
//			return null;
//		}
//		//获取模板所在文件路径
//		HttpSession session = request.getSession();
//		ServletContext  application  = session.getServletContext();  
//		String serverRealPath = application.getRealPath("/") ;
//		String path = serverRealPath+"\\download\\mianqian\\moban\\";
//		
//		//获取要填充的数据
//		Map<String, Object> formMap = visaOrderService.downloadBanqian(visaId,travalerId);
//		
//		//创建今天的文件目录
//		 Calendar now = Calendar.getInstance();  
//		 Date d = new Date();  
//	     System.out.println(d);  
//	     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
//	     String dateNowStr = sdf.format(d);  
//	     
//	     String listFilePath = createFile(serverRealPath,dateNowStr);
//	     
//	     //删除其他文件夹
//	    String fileName = "mianqian"+now.getTimeInMillis()+".doc";
//		//生成文件的路径和文件名
//		String targetFilePathAndName = listFilePath+"\\"+fileName;
//		if(null != formMap){
//			WordGenerator.generateWordFromTemplate(path, formMap.get("lingqu").toString()+".ftl", formMap, targetFilePathAndName);
//		}
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("path", targetFilePathAndName);
//		return map;
//	        
//	 }
	
//	private String createFile(String serverRealPath,String dateNowStr){
//		 String listFilePath1 = serverRealPath+"\\download\\";
//	     String listFilePath2 = serverRealPath+"\\download\\mianqian\\" ;
//	     String listFilePath3 = serverRealPath+"\\download\\mianqian\\zidingyi\\";
//	     String listFilePath4 = serverRealPath+"\\download\\mianqian\\zidingyi\\"+dateNowStr+"\\";
//			
//	     //判断文件目录已经存在的话,不再创建
//	     File tempFile1 = new File(listFilePath1);
//	     File tempFile2 = new File(listFilePath2);
//	     File tempFile3 = new File(listFilePath3);
//	     File tempFile4 = new File(listFilePath4);
//		//判断文件夹是否存在,如果不存在则创建文件夹
//	     if (!tempFile1.exists()) {
//	    	 tempFile1.mkdir();
//	     }
//	     if (!tempFile2.exists()) {
//	    	 tempFile2.mkdir();
//	     }
//	     if (!tempFile3.exists()) {
//	    	 tempFile3.mkdir();
//	     }
//	     if (!tempFile4.exists()) {
//	    	 tempFile4.mkdir();
//	     }
//	     return serverRealPath+"\\download\\mianqian\\zidingyi\\"+dateNowStr+"\\";
//	}
//	/**
//	 * 下载护照领取单
//	 * 
//	 * */
//	@ResponseBody
//	@RequestMapping(value={"mqtzdownload"})
//	 public HttpServletResponse download(  HttpServletResponse response,HttpServletRequest request) {
//	        try {
//	        	String path = request.getParameter("path");
//	            // path是指欲下载的文件的路径。
//	            File file = new File(path);
//	            // 取得文件名。
//	            String filename = file.getName();
//	            // 以流的形式下载文件。
//	            InputStream fis = new BufferedInputStream(new FileInputStream(path));
//	            byte[] buffer = new byte[fis.available()];
//	            fis.read(buffer);
//	            fis.close();
//	            // 清空response
//	            response.reset();
//	            // 设置response的Header
//	            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
//	            response.addHeader("Content-Length", "" + file.length());
//	            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
//	            response.setContentType("application/octet-stream");
//	            toClient.write(buffer);
//	            toClient.flush();
//	            toClient.close();
//	            return null;
//	        } catch (IOException ex) {
//	            ex.printStackTrace();
//	        }
//	        return response;
//	    }	
	


//	    public void downloadLocal(HttpServletResponse response) throws FileNotFoundException {
//	        // 下载本地文件
//	        String fileName = "Operator.doc".toString(); // 文件的默认保存名
//	        // 读到流中
//	        InputStream inStream = new FileInputStream("c:/Operator.doc");// 文件的存放路径
//	        // 设置输出的格式
//	        response.reset();
//	        response.setContentType("bin");
//	        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//	        // 循环取出流中的数据
//	        byte[] b = new byte[100];
//	        int len;
//	        try {
//	            while ((len = inStream.read(b)) > 0)
//	                response.getOutputStream().write(b, 0, len);
//	            inStream.close();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	    }
//
//	    public void downloadNet(HttpServletResponse response) throws MalformedURLException {
//	        // 下载网络文件
//	        int bytesum = 0;
//	        int byteread = 0;
//
//	        URL url = new URL("windine.blogdriver.com/logo.gif");
//
//	        try {
//	            URLConnection conn = url.openConnection();
//	            InputStream inStream = conn.getInputStream();
//	            FileOutputStream fs = new FileOutputStream("c:/abc.gif");
//
//	            byte[] buffer = new byte[1204];
//	            int length;
//	            while ((byteread = inStream.read(buffer)) != -1) {
//	                bytesum += byteread;
//	                System.out.println(bytesum);
//	                fs.write(buffer, 0, byteread);
//	            }
//	        } catch (FileNotFoundException e) {
//	            e.printStackTrace();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	    }
	
}
