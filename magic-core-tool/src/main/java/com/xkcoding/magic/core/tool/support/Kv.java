/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
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
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.xkcoding.magic.core.tool.support;

import cn.hutool.core.convert.Convert;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

/**
 * 链式map
 *
 * @author Chill
 */
public class Kv extends LinkedCaseInsensitiveMap<Object> {


	private Kv() {

	}

	/**
	 * 创建Kv
	 *
	 * @return Kv
	 */
	public static Kv create() {
		return new Kv();
	}

	public static HashMap newMap() {
		return new HashMap(16);
	}

	/**
	 * 设置列
	 *
	 * @param attr  属性
	 * @param value 值
	 * @return 本身
	 */
	public Kv set(String attr, Object value) {
		this.put(attr, value);
		return this;
	}

	/**
	 * 设置列，当键或值为null时忽略
	 *
	 * @param attr  属性
	 * @param value 值
	 * @return 本身
	 */
	public Kv setIgnoreNull(String attr, Object value) {
		if (null != attr && null != value) {
			set(attr, value);
		}
		return this;
	}

	public Object getObj(String key) {
		return super.get(key);
	}

	/**
	 * 获得特定类型值
	 *
	 * @param <T>          值类型
	 * @param attr         字段名
	 * @param defaultValue 默认值
	 * @return 字段值
	 */
	public <T> T get(String attr, T defaultValue) {
		final Object result = get(attr);
		return (T) (result != null ? result : defaultValue);
	}

	/**
	 * 获得特定类型值
	 *
	 * @param attr 字段名
	 * @return 字段值
	 */
	public String getStr(String attr) {
		return Convert.toStr(get(attr), null);
	}

	/**
	 * 获得特定类型值
	 *
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Integer getInt(String attr) {
		return Convert.toInt(get(attr), -1);
	}

	/**
	 * 获得特定类型值
	 *
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Long getLong(String attr) {
		return Convert.toLong(get(attr), -1L);
	}

	/**
	 * 获得特定类型值
	 *
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Float getFloat(String attr) {
		return Convert.toFloat(get(attr), null);
	}

	public Double getDouble(String attr) {
		return Convert.toDouble(get(attr), null);
	}


	/**
	 * 获得特定类型值
	 *
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Boolean getBool(String attr) {
		return Convert.toBool(get(attr), null);
	}

	/**
	 * 获得特定类型值
	 *
	 * @param attr 字段名
	 * @return 字段值
	 */
	public byte[] getBytes(String attr) {
		return get(attr, null);
	}

	/**
	 * 获得特定类型值
	 *
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Date getDate(String attr) {
		return get(attr, null);
	}

	/**
	 * 获得特定类型值
	 *
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Time getTime(String attr) {
		return get(attr, null);
	}

	/**
	 * 获得特定类型值
	 *
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Timestamp getTimestamp(String attr) {
		return get(attr, null);
	}

	/**
	 * 获得特定类型值
	 *
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Number getNumber(String attr) {
		return get(attr, null);
	}

	@Override
	public Kv clone() {
		return (Kv) super.clone();
	}

}
