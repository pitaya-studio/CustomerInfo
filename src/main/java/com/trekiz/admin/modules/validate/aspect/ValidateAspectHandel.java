package com.trekiz.admin.modules.validate.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.validate.groupField.ValidateField;
import com.trekiz.admin.modules.validate.groupField.ValidateGroup;

/**
 * 验证注解处理类
 * @author chao.zhang
 */
@Component  
@Aspect
public class ValidateAspectHandel {
	/**
	 * 使用AOP对使用了ValidateGroup的方法进行代理校验 
	 * @param joinPoint
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Around("@annotation(com.trekiz.admin.modules.validate.groupField.ValidateGroup)")
	public Object validateAround(ProceedingJoinPoint joinPoint){
		boolean flag = false;
		ValidateGroup an = null;  
        Object[] args =  null ;  
        Method method = null;  
        Object target = null ;  
        String methodName = null;
		try {
			//获得当前正在执行的方法
			methodName = joinPoint.getSignature().getName();
			//返回目标对象  
			target = joinPoint.getTarget();
			//根据类和方法名得到方法
			method = getMethodByClassAndName(target.getClass(),methodName);
			//返回被通知方法参数列表
			args = joinPoint.getArgs();
			//根据目标方法和注解类型  得到该目标方法的指定注解
			an = (ValidateGroup) getAnnotationByMethod(method,ValidateGroup.class);
			//验证参数是否合法
			flag = validateFiled(an.fields(),args);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}finally{
			if(flag){  
                System.out.println("验证通过");  
                try {
					return joinPoint.proceed();
				} catch (Throwable e) {
					e.printStackTrace();
				}  
            }else{  //我们使用了Spring MVC ，所有返回值应该为String或ModelAndView ，如果是用Struts2，直接返回一个String的resutl就行了  
                System.out.println("验证未通过");  
                Class returnType = method.getReturnType();  //得到方法返回值类型
                HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();   
                if(returnType == String.class){ //如果返回值为String
                	request.setAttribute(Context.ERROR_MESSAGE_KEY, "您提交的数据有错误");
                    return Context.ERROR_PAGE;        //返回错误页面  
                }else if(returnType == ModelAndView.class){ 
                	request.setAttribute(Context.ERROR_MESSAGE_KEY, "您提交的数据有错误");
                    return Context.ERROR_PAGE;//返回错误页面  
                }else{  //当使用Ajax的时候 可能会出现这种情况  
                    return null ;  
                }  
            }  
		}
		return flag;
	}
	
	/**
	 * 验证参数是否合法
	 * @param valiedatefiles
	 * @param args
	 * @return
	 */
	public boolean validateFiled(ValidateField[] valiedatefiles , Object[] args){
		for (ValidateField validateFiled : valiedatefiles) {  
            Object arg = null; 
            //获得参数
            if("".equals(validateFiled.fieldName()) ){  
                arg = args[validateFiled.index()];  
            }else{  
                arg = getFieldByObjectAndFileName(args[validateFiled.index()] ,  
                        validateFiled.fieldName() );  
            }  
  
            if(validateFiled.notNull()){ //判断参数是否为空  
                if(arg == null ){
                    return false;  
                }
            }else{      //如果该参数能够为空，并且当参数为空时，就不用判断后面的了 ，直接返回true  
                if(arg == null ){
                	 return true;  
                }
            }  
            
            //判断字符串最大长度  
            if(validateFiled.maxLen() > 0){      
                if(((String)arg).length() > validateFiled.maxLen()){
                	return false;
                }
            }  
            
            //判断字符串最小长度
            if(validateFiled.minLen() > 0){        
                if(((String)arg).length() < validateFiled.minLen()){
                	 return false;
                }
            }  
            
            //判断数值最大值
            if(validateFiled.maxVal() != -1){     
                if( (Integer)arg > validateFiled.maxVal()){
                	 return false;  
                }
            }  
            
            //判断数值最小值
            if(validateFiled.minVal() != -1){     
                if((Integer)arg < validateFiled.minVal()){
                	return false; 
                }
            }  
            
            //判断正则
            if(!"".equals(validateFiled.regStr())){   
                if(arg instanceof String){  
                    if(!((String)arg).matches(validateFiled.regStr())) {
                    	return false;
                    }
                }else{  
                    return false;  
                }  
            }  
        }  
        return true;  
	}
	
	/**
	 * 根据对象和属性名得到 属性 
	 * @param targetObj
	 * @param fileName
	 * @return
	 */
    public Object getFieldByObjectAndFileName(Object targetObj , String fileName){  
        String tmp[] = fileName.split("\\.");  
        Object arg = targetObj ;  
        try {
			for (int i = 0; i < tmp.length; i++) {  
			    Method methdo = arg.getClass().getMethod(getGetterNameByFiledName(tmp[i]));  
			    arg = methdo.invoke(arg);  
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("error");
		}  
        return arg ;  
    }  
	
    /**
     * 根据目标方法和注解类型  得到该目标方法的指定注解
     * @param fieldName
     * @return
     */
    public String getGetterNameByFiledName(String fieldName){  
        return "get" + fieldName.substring(0 ,1).toUpperCase() + fieldName.substring(1) ;  
    }  
    
    /**
	 * 根据目标方法和注解类型  得到该目标方法的指定注解
	 * @param method
	 * @param annoClass
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Annotation getAnnotationByMethod(Method method , Class annoClass){
		//获取该方法的Annotations所有的注解实例
		Annotation all[] = method.getAnnotations();  
        for (Annotation annotation : all) {  
        	//注解类型是否相同，相同返回注解实例
            if (annotation.annotationType() == annoClass) {  
                return annotation;  
            }  
        }  
        return null;  
	}
	
	/**
	 * 根据类和方法名得到方法
	 * @param c
	 * @param methodName
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public Method getMethodByClassAndName(Class c,String methodName){
		try {
			//获得所有方法
			Method[] methods = c.getDeclaredMethods();  
	        for (Method method : methods) {  
	        	//判断方法名称和methodName是否相等，相等返回该方法
	            if(method.getName().equals(methodName)){  
	                return method ;  
	            }  
	        }  
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("error");
		}
		return null;
	}
}
