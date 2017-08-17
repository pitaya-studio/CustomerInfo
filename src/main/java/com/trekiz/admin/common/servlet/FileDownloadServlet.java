package com.trekiz.admin.common.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.service.DocInfoService;

/**
 * Servlet implementation class FileDownloadServlet
 */
public class FileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileDownloadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	File downFile;
    	OutputStream os = null;
    	String docId = request.getParameter("id");
    	if(StringUtils.isNotEmpty(docId)){
    		Long documentId = Long.valueOf(docId);
    		ServletContext servletContext = this.getServletContext();
    		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    		DocInfoService docInfoService = (DocInfoService) ctx.getBean("docInfoService");
        	try {
        		DocInfo docInfo = docInfoService.getDocInfo(documentId);
        		if(docInfo!=null){
        			downFile = new File(Global.getBasePath()+File.separator+docInfo.getDocPath());
        			if(downFile.exists()){
//        				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//                        headers.setContentDispositionFormData("attachment", new String(docInfo.getDocName().getBytes("utf-8"), "ISO-8859-1")); 
//                        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(downFile),headers, HttpStatus.CREATED);
        				response.reset();
        				response.setHeader("Content-Disposition", "attachment; filename="+new String(docInfo.getDocName().getBytes("gb2312"), "ISO-8859-1"));
        				response.setContentType("application/octet-stream; charset=utf-8");
        		    	os = response.getOutputStream();
        				os.write(FileUtils.readFileToByteArray(downFile));
        	            os.flush();
        			}       		
        		}
        		
    		} catch (Exception e) {
    			e.printStackTrace();
    		}finally{
    			if(os!=null)
    				try {
    					os.close();
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
    		}
    	}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
