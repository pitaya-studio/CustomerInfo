package com.trekiz.admin.common.utils;

import java.util.Iterator;
import java.util.Map;

import com.trekiz.admin.modules.sys.entity.SysDefineDict;

public class TransferIterator implements Iterator<java.util.Map.Entry<String, String>> {
	
    static class InnerEntry implements Map.Entry<String, String> {
    	
    	private java.util.Map.Entry<String, SysDefineDict> entry;
    	
    	public InnerEntry(java.util.Map.Entry<String, SysDefineDict> _entry){
    		this.entry = _entry;
    	}

		@Override
		public String getKey() {
			return entry.getKey();
		}

		@Override
		public String getValue() {
			return entry.getValue().getLabel();
		}

		@Override
		public String setValue(String value) {
			throw new RuntimeException("不支持此操作");
		}

    }
	
	private Iterator<java.util.Map.Entry<String, SysDefineDict>> it;
	
	public TransferIterator(Iterator<java.util.Map.Entry<String, SysDefineDict>> _it){
		this.it = _it;
	}

	@Override
	public boolean hasNext() {
		return it.hasNext();
	}

	@Override
	public java.util.Map.Entry<String, String> next() {
		return new InnerEntry(it.next());
	}

	@Override
	public void remove() {
		throw new RuntimeException("不支持此操作");
		
	}

}
