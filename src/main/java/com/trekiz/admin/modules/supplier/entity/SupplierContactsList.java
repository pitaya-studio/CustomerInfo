package com.trekiz.admin.modules.supplier.entity;

import java.util.ArrayList;
import java.util.List;

public class SupplierContactsList {
	
	private List<SupplierContacts> supplierContactses = new ArrayList<SupplierContacts>();

	public List<SupplierContacts> getSupplierContactses() {
		return supplierContactses;
	}

	public void setSupplierContactses(List<SupplierContacts> supplierContactses) {
		this.supplierContactses = supplierContactses;
	}
}
