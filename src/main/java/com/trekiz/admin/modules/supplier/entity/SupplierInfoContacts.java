package com.trekiz.admin.modules.supplier.entity;

public class SupplierInfoContacts {

	private SupplierInfo supplierInfo ;

	private SupplierContactsList contacts;

	public SupplierInfo getSupplierInfo() {
		return supplierInfo;
	}

	public void setSupplierInfo(SupplierInfo supplierInfo) {
		this.supplierInfo = supplierInfo;
	}

	public SupplierContactsList getContacts() {
		return contacts;
	}

	public void setContacts(SupplierContactsList contacts) {
		this.contacts = contacts;
	}
}
