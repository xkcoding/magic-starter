/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */
package com.xkcoding.magic.core.auto.common;

import java.util.*;

/**
 * MultiSetMap
 *
 * @author L.cm
 */
public class MultiSetMap<K, V> {
	private transient final Map<K, Set<V>> map;

	public MultiSetMap() {
		map = new HashMap<>();
	}

	private Set<V> createSet() {
		return new HashSet<>();
	}

	/**
	 * put to MultiSetMap
	 *
	 * @param key   键
	 * @param value 值
	 * @return boolean
	 */
	public boolean put(K key, V value) {
		Set<V> set = map.get(key);
		if (set == null) {
			set = createSet();
			if (set.add(value)) {
				map.put(key, set);
				return true;
			} else {
				throw new AssertionError("New set violated the set spec");
			}
		} else if (set.add(value)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否包含某个key
	 *
	 * @param key key
	 * @return 结果
	 */
	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	/**
	 * 是否包含 value 中的某个值
	 *
	 * @param value value
	 * @return 是否包含
	 */
	public boolean containsVal(V value) {
		Collection<Set<V>> values = map.values();
		return values.stream().anyMatch(vs -> vs.contains(value));
	}

	/**
	 * key 集合
	 *
	 * @return keys
	 */
	public Set<K> keySet() {
		return map.keySet();
	}

	/**
	 * put list to MultiSetMap
	 *
	 * @param key 键
	 * @param set 值列表
	 * @return boolean
	 */
	public boolean putAll(K key, Set<V> set) {
		if (set == null) {
			return false;
		} else {
			map.put(key, set);
			return true;
		}
	}

	/**
	 * get List by key
	 *
	 * @param key 键
	 * @return List
	 */
	public Set<V> get(K key) {
		return map.get(key);
	}

	/**
	 * clear MultiSetMap
	 */
	public void clear() {
		map.clear();
	}

	/**
	 * isEmpty
	 *
	 * @return isEmpty
	 */
	public boolean isEmpty() {
		return map.isEmpty();
	}
}
