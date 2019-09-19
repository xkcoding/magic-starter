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

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

/**
 * 抽象 处理器
 *
 * @author L.cm
 */
public abstract class AbstractBladeProcessor extends AbstractProcessor {

	private final String DEBUG = "debug";

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	/**
	 * AutoService 注解处理器
	 *
	 * @param annotations 注解 getSupportedAnnotationTypes
	 * @param roundEnv    扫描到的 注解新
	 * @return 是否完成
	 */
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		try {
			return processImpl(annotations, roundEnv);
		} catch (Exception e) {
			fatalError(e);
			return true;
		}
	}

	/**
	 * 实现类
	 *
	 * @param annotations
	 * @param roundEnv
	 * @return
	 */
	protected abstract boolean processImpl(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

	protected void log(String msg) {
		if (processingEnv.getOptions().containsKey(DEBUG)) {
			processingEnv.getMessager().printMessage(Kind.NOTE, msg);
		}
	}

	protected void error(String msg, Element element, AnnotationMirror annotation) {
		processingEnv.getMessager().printMessage(Kind.ERROR, msg, element, annotation);
	}

	protected void fatalError(Exception e) {
		// We don't allow exceptions of any kind to propagate to the compiler
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		fatalError(writer.toString());
	}

	protected void fatalError(String msg) {
		processingEnv.getMessager().printMessage(Kind.ERROR, "FATAL ERROR: " + msg);
	}

}
