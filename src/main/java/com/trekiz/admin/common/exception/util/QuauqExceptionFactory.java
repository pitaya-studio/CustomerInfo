package com.trekiz.admin.common.exception.util;

import com.trekiz.admin.common.exception.bean.BaseException4Quauq;
import com.trekiz.admin.common.exception.bean.airticket.Airticket4BookingException;
import com.trekiz.admin.common.exception.bean.airticket.Airticket4OrderException;
import com.trekiz.admin.common.exception.bean.airticket.Airticket4ProductException;
import com.trekiz.admin.common.exception.bean.airticket.AirticketException;
import com.trekiz.admin.common.exception.bean.finance.FinanceException;
import com.trekiz.admin.common.exception.bean.group.Group4StockException;
import com.trekiz.admin.common.exception.bean.group.GroupException;
import com.trekiz.admin.common.exception.bean.visa.VisaException;
import com.trekiz.admin.common.exception.common.ExceptionConstants;

public class QuauqExceptionFactory {
	
	
	public static BaseException4Quauq newQuauqException(Integer productType,Integer module,String message ,Throwable e){
		
		if(productType==ExceptionConstants.PRODUCT_TYPE_JIPIAO){
			if(module==null)
				return new AirticketException(message,e);
			
			if(module==ExceptionConstants.MODULE_CHANPIN){
				return new Airticket4ProductException(message,e) ;
			}else if(module==ExceptionConstants.MODULE_BAOMING){
				return new Airticket4BookingException(message,e) ;
			}else if(module==ExceptionConstants.MODULE_DINGDAN){
				return new Airticket4OrderException(message,e) ;
			}else{
				return new AirticketException(message,e);
			}
			
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_DANTUAN){
			
			return new GroupException(message,e);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_SANPIN){
			
			return new GroupException(message,e);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_YOUXUE){
			
			return new GroupException(message,e);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_DAKEHU){
			
			return new GroupException(message,e);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_ZIYOUXING){
			
			return new GroupException(message,e);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_YOULUN){
			
			if(module==null)
				return new GroupException(message,e);
			
			if(module==ExceptionConstants.MODULE_YOULUNKUCUN){
				return new Group4StockException(message,e) ;
			}else{
				return new GroupException(message,e);
			}
			
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_QIANZHENG){
			
			return new VisaException(message,e);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_FINANCE){
			
			return new FinanceException(message,e);
		}else{
			return new BaseException4Quauq(message,e);
		}
		
	}

	
	public static BaseException4Quauq newQuauqException(Integer productType,Integer module,String message ){
		
		if(productType==ExceptionConstants.PRODUCT_TYPE_JIPIAO){
			if(module==null)
				return new AirticketException(message);
			
			if(module==ExceptionConstants.MODULE_CHANPIN){
				return new Airticket4ProductException(message) ;
			}else if(module==ExceptionConstants.MODULE_BAOMING){
				return new Airticket4BookingException(message) ;
			}else if(module==ExceptionConstants.MODULE_DINGDAN){
				return new Airticket4OrderException(message) ;
			}else{
				return new AirticketException(message);
			}
			
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_DANTUAN){
			
			return new GroupException(message);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_SANPIN){
			
			return new GroupException(message);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_YOUXUE){
			
			return new GroupException(message);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_DAKEHU){
			
			return new GroupException(message);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_ZIYOUXING){
			
			return new GroupException(message);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_YOULUN){
			
			if(module==null)
				return new GroupException(message);
			
			if(module==ExceptionConstants.MODULE_YOULUNKUCUN){
				return new Group4StockException(message) ;
			}else{
				return new GroupException(message);
			}
			
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_QIANZHENG){
			
			return new VisaException(message);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_FINANCE){
			
			return new FinanceException(message);
		}else{
			return new BaseException4Quauq(message);
		}
		
		
	}
	
	public static BaseException4Quauq newQuauqException(Integer productType,Integer module,Throwable e){
		
		if(productType==ExceptionConstants.PRODUCT_TYPE_JIPIAO){
			if(module==null)
				return new AirticketException(e);
			
			if(module==ExceptionConstants.MODULE_CHANPIN){
				return new Airticket4ProductException(e) ;
			}else if(module==ExceptionConstants.MODULE_BAOMING){
				return new Airticket4BookingException(e) ;
			}else if(module==ExceptionConstants.MODULE_DINGDAN){
				return new Airticket4OrderException(e) ;
			}else{
				return new AirticketException(e);
			}
			
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_DANTUAN){
			
			return new GroupException(e);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_SANPIN){
			
			return new GroupException(e);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_YOUXUE){
			
			return new GroupException(e);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_DAKEHU){
			
			return new GroupException(e);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_ZIYOUXING){
			
			return new GroupException(e);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_YOULUN){
			if(module==null)
				return new GroupException(e);
			
			if(module==ExceptionConstants.MODULE_YOULUNKUCUN){
				return new Group4StockException(e) ;
			}else{
				return new GroupException(e);
			}
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_QIANZHENG){
			
			return new VisaException(e);
		}else if(productType==ExceptionConstants.PRODUCT_TYPE_FINANCE){
			
			return new FinanceException(e);
		}else{
			return new BaseException4Quauq(e);
		}
		
		
	}
	
}
