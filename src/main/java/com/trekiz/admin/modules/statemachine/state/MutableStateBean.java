package com.trekiz.admin.modules.statemachine.state;


public interface MutableStateBean {
	void setStatus(Integer status);
	
	Integer getStatus();
}
