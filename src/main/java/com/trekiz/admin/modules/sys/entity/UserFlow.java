package com.trekiz.admin.modules.sys.entity;

import java.io.Serializable;

public class UserFlow implements Serializable{

    
	 /**
	 *    
     *DEVELOP_DEMO
	 */
	private static final long serialVersionUID = -5657965356375838989L;
	private String userName;
	 private String userAge;
	 // 用户 籍贯
	 private String userJG;
	 // 用户  政治
	 private String userZH;
	
	 public UserFlow(){
	     
	 }
	 
	public UserFlow(String userName, String userAge, String userJG, String userZH) {
		
		this.userName = userName;
		this.userAge = userAge;
		this.userJG = userJG;
		this.userZH = userZH;
	}
	 
    public void setFirstInfo(String userName, String userAge ) {
    	this.userName=userName;
    	this.userAge=userAge;
    }
    
    public void setSecoddInfo(String userJG, String userZH) {
    	this.userJG=userJG;
    	this.userZH=userZH;
    }
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserAge() {
		return userAge;
	}
	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}
	public String getUserJG() {
		return userJG;
	}
	public void setUserJG(String userJG) {
		this.userJG = userJG;
	}
	public String getUserZH() {
		return userZH;
	}
	public void setUserZH(String userZH) {
		this.userZH = userZH;
	}
	 
	 
}
