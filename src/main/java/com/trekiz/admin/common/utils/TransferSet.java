package com.trekiz.admin.common.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.trekiz.admin.modules.sys.entity.SysDefineDict;

public class TransferSet implements Set<java.util.Map.Entry<String, String>> {
	
	private Set<java.util.Map.Entry<String, SysDefineDict>> set;
	
	public TransferSet(Set<java.util.Map.Entry<String, SysDefineDict>> _set){
		this.set = _set;
	}

	@Override
	public int size() {
		return set.size();
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		throw new RuntimeException("不支持此操作");
	}

	@Override
	public Iterator<Entry<String, String>> iterator() {
		return new TransferIterator(set.iterator());
	}

	@Override
	public Object[] toArray() {
		throw new RuntimeException("不支持此操作");
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new RuntimeException("不支持此操作");
	}

	@Override
	public boolean add(Entry<String, String> e) {
		throw new RuntimeException("不支持此操作");
	}

	@Override
	public boolean remove(Object o) {
		throw new RuntimeException("不支持此操作");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new RuntimeException("不支持此操作");
	}

	@Override
	public boolean addAll(Collection<? extends Entry<String, String>> c) {
		throw new RuntimeException("不支持此操作");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new RuntimeException("不支持此操作");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new RuntimeException("不支持此操作");
	}

	@Override
	public void clear() {
		
	}

}
