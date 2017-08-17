package com.trekiz.admin.modules.statemachine.state;

import com.trekiz.admin.modules.statemachine.context.StateMachineContext;

import java.util.List;

public class StateOwner<T extends MutableStateBean> {
	private List<T> mutableStateBeans;
	
	private SpecificState specificState;
	
	public StateOwner(List<T> mutableStateBeans, String specificStateName){
		this.mutableStateBeans =  mutableStateBeans;
		this.specificState = StateMachineContext.getState(specificStateName);
	}
	
	public List<T> getMutableStateBeans() {
	    return mutableStateBeans;
    }
	
	public SpecificState getSpecificState() {
		return specificState;
	}
	
}
