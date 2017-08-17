package com.trekiz.admin.common.utils.word;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerUtil {

	public static File generateFile(String templateName,String fileName, Map<?, ?> root) throws IOException, TemplateException{
         /**
          * 创建Configuration对象
          */
         Configuration config = new Configuration();
         /**
          * 指定模板路径
          */
         File file = new File(FreeMarkerUtil.class.getResource("/").getPath()+"templates");
         /**
          * 设置要解析的模板所在的目录，并加载模板文件
          */
         config.setDirectoryForTemplateLoading(file);
         /**
          * 设置包装器，并将对象包装为数据模型
          */
         config.setObjectWrapper(new DefaultObjectWrapper());
 
         /**
          * 获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致
          */
         Template template = config.getTemplate(templateName,"utf-8");
         /**
          * 合并数据模型与模板
          */
         File genFile = new File(FreeMarkerUtil.class.getResource("/").getPath()+"word/"+fileName);
         
         Writer out = new OutputStreamWriter(new FileOutputStream(genFile),"utf-8");
         template.process(root, out);
         out.flush();
         out.close();
         return genFile;

	}
	
	/**
	 * 字符串过滤问题
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static String StringFilter(String str) throws PatternSyntaxException{      
		String regEx="[&<>\"']";   
		Pattern   p   =   Pattern.compile(regEx);      
		Matcher   m   =   p.matcher(str);      
		return   m.replaceAll("-").trim();      
	}
}
