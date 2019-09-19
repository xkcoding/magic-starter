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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Type utilities.
 *
 * @author Stephane Nicoll
 * @since 5.0
 */
public class TypeHelper {

	private final ProcessingEnvironment env;

	private final Types types;


	public TypeHelper(ProcessingEnvironment env) {
		this.env = env;
		this.types = env.getTypeUtils();
	}


	public String getType(Element element) {
		return getType(element != null ? element.asType() : null);
	}

	public String getType(AnnotationMirror annotation) {
		return getType(annotation != null ? annotation.getAnnotationType() : null);
	}

	public String getType(TypeMirror type) {
		if (type == null) {
			return null;
		}
		if (type instanceof DeclaredType) {
			DeclaredType declaredType = (DeclaredType) type;
			Element enclosingElement = declaredType.asElement().getEnclosingElement();
			if (enclosingElement != null && enclosingElement instanceof TypeElement) {
				return getQualifiedName(enclosingElement) + "$" + declaredType.asElement().getSimpleName().toString();
			} else {
				return getQualifiedName(declaredType.asElement());
			}
		}
		return type.toString();
	}

	private String getQualifiedName(Element element) {
		if (element instanceof QualifiedNameable) {
			return ((QualifiedNameable) element).getQualifiedName().toString();
		}
		return element.toString();
	}

	/**
	 * Return the super class of the specified {@link Element} or null if this
	 * {@code element} represents {@link Object}.
	 *
	 * @param element Element
	 * @return Element
	 */
	public Element getSuperClass(Element element) {
		List<? extends TypeMirror> superTypes = this.types.directSupertypes(element.asType());
		if (superTypes.isEmpty()) {
			// reached java.lang.Object
			return null;
		}
		return this.types.asElement(superTypes.get(0));
	}

	/**
	 * Return the interfaces that are <strong>directly</strong> implemented by the
	 * specified {@link Element} or an empty list if this {@code element} does not
	 * implement any interface.
	 *
	 * @param element Element
	 * @return Element list
	 */
	public List<Element> getDirectInterfaces(Element element) {
		List<? extends TypeMirror> superTypes = this.types.directSupertypes(element.asType());
		List<Element> directInterfaces = new ArrayList<>();
		// index 0 is the super class
		if (superTypes.size() > 1) {
			for (int i = 1; i < superTypes.size(); i++) {
				Element e = this.types.asElement(superTypes.get(i));
				if (e != null) {
					directInterfaces.add(e);
				}
			}
		}
		return directInterfaces;
	}

	public List<? extends AnnotationMirror> getAllAnnotationMirrors(Element e) {
		return this.env.getElementUtils().getAllAnnotationMirrors(e);
	}

}
