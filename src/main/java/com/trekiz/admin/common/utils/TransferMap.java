package com.trekiz.admin.common.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.trekiz.admin.modules.sys.entity.SysDefineDict;

public class TransferMap implements Map<String, String> {
	
	private Map<String,SysDefineDict> map;
	
	public TransferMap(Map<String,SysDefineDict> _map){
		this.map = _map;
	}

	@Override
	public int size() {
		if(this.map==null)return 0;
		return this.map.size();
	}

	@Override
	public boolean isEmpty() {
		if(this.map==null)return true;
		return this.map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		if(this.map==null)return true;
		return this.map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		throw new RuntimeException("不支持此操作");
	}

	@Override
	public String get(Object key) {
		return this.map.get(key).getLabel();
	}

	@Override
	public String put(String key, String value) {
		throw new RuntimeException("不支持此操作");
	}

	@Override
	public String remove(Object key) {
		throw new RuntimeException("不支持此操作");
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		throw new RuntimeException("不支持此操作");
	}

	@Override
	public void clear() {
		throw new RuntimeException("不支持此操作");
	}

	@Override
	public Set<String> keySet() {
		return this.map.keySet();
	}

	@Override
	public Collection<String> values() {
		throw new RuntimeException("不支持此操作");
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return new TransferSet(this.map.entrySet());
	}

}
