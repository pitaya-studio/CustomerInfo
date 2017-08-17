package com.trekiz.admin.common.query.entity;

import java.util.List;

/**
 * Created by 6CR433WKMN on 2016/4/8.
 */
public class SelectJson {
    private String error;
    private List<SelectOption> data;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


   

	public List<SelectOption> getData() {
		return data;
	}


	public void setData(List<SelectOption> data) {
		this.data = data;
	}



}

