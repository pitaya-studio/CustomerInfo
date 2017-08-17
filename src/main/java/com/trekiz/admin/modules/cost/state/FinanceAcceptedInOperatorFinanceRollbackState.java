package com.trekiz.admin.modules.cost.state;

import java.util.List;

import com.trekiz.admin.modules.statemachine.state.MutableStateBean;
import com.trekiz.admin.modules.statemachine.state.SpecificState;
import com.trekiz.admin.modules.statemachine.state.StateOwner;

public class FinanceAcceptedInOperatorFinanceRollbackState extends SpecificState {

	public FinanceAcceptedInOperatorFinanceRollbackState(Integer status) {
	    super(status);
	    this.status = status;
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void run(StateOwner stateOwner) {
		if(stateOwner != null && stateOwner.getMutableStateBeans() != null){
			List<MutableStateBean> beans = stateOwner.getMutableStateBeans();
			for(MutableStateBean bean : beans){
				bean.setStatus(status);
			}
		}
    }
}
