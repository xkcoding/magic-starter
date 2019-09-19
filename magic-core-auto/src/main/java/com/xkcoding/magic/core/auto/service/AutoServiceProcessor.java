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
package com.xkcoding.magic.core.auto.service;

import com.xkcoding.magic.core.auto.common.AbstractBladeProcessor;
import com.xkcoding.magic.core.auto.common.MultiSetMap;
import com.xkcoding.magic.core.auto.common.Sets;
import com.xkcoding.magic.core.auto.common.TypeHelper;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * java spi 服务自动处理器 参考：google auto
 *
 * @author L.cm
 */
@SupportedOptions("debug")
public class AutoServiceProcessor extends AbstractBladeProcessor {
	/**
	 * spi 服务集合，key 接口 -> value 实现列表
	 */
	private MultiSetMap<String, String> providers = new MultiSetMap<>();
	private TypeHelper typeHelper;

	@Override
	public synchronized void init(ProcessingEnvironment env) {
		super.init(env);
		this.typeHelper = new TypeHelper(env);
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Sets.ofImmutableSet(AutoService.class.getName());
	}

	@Override
	protected boolean processImpl(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (roundEnv.processingOver()) {
			generateConfigFiles();
		} else {
			processAnnotations(annotations, roundEnv);
		}
		return true;
	}

	private void processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(AutoService.class);

		log(annotations.toString());
		log(elements.toString());

		for (Element e : elements) {
			TypeElement providerImplementer = (TypeElement) e;
			AnnotationMirror annotationMirror = getAnnotationMirror(e, AutoService.class);
			if (annotationMirror == null) {
				continue;
			}
			Set<TypeMirror> typeMirrors = getValueFieldOfClasses(annotationMirror);
			if (typeMirrors.isEmpty()) {
				error("No service interfaces provided for element!", e, annotationMirror);
				continue;
			}
			for (TypeMirror typeMirror : typeMirrors) {
				String providerInterfaceName = typeHelper.getType(typeMirror);
				Name providerImplementerName = providerImplementer.getQualifiedName();

				log("provider interface: " + providerInterfaceName);
				log("provider implementer: " + providerImplementerName);

				if (checkImplementer(providerImplementer, typeMirror)) {
					providers.put(providerInterfaceName, typeHelper.getType(providerImplementer));
				} else {
					String message = "ServiceProviders must implement their service provider interface. " + providerImplementerName + " does not implement " + providerInterfaceName;
					error(message, e, annotationMirror);
				}
			}
		}
	}

	private void generateConfigFiles() {
		Filer filer = processingEnv.getFiler();

		for (String providerInterface : providers.keySet()) {
			String resourceFile = "META-INF/services/" + providerInterface;
			log("Working on resource file: " + resourceFile);
			try {
				SortedSet<String> allServices = new TreeSet<>();
				try {
					FileObject existingFile = filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
					log("Looking for existing resource file at " + existingFile.toUri());
					Set<String> oldServices = ServicesFiles.readServiceFile(existingFile.openInputStream());
					log("Existing service entries: " + oldServices);
					allServices.addAll(oldServices);
				} catch (IOException e) {
					log("Resource file did not already exist.");
				}

				Set<String> newServices = new HashSet<>(providers.get(providerInterface));
				if (allServices.containsAll(newServices)) {
					log("No new service entries being added.");
					return;
				}

				allServices.addAll(newServices);
				log("New service file contents: " + allServices);
				FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
				OutputStream out = fileObject.openOutputStream();
				ServicesFiles.writeServiceFile(allServices, out);
				out.close();
				log("Wrote to: " + fileObject.toUri());
			} catch (IOException e) {
				fatalError("Unable to create " + resourceFile + ", " + e);
				return;
			}
		}
	}

	/**
	 * Verifies {@link java.util.spi.LocaleServiceProvider} constraints on the concrete provider class.
	 * Note that these constraints are enforced at runtime via the ServiceLoader,
	 * we're just checking them at compile time to be extra nice to our users.
	 */
	private boolean checkImplementer(TypeElement providerImplementer, TypeMirror providerType) {
		// TODO: We're currently only enforcing the subtype relationship
		// constraint. It would be nice to enforce them all.
		Types types = processingEnv.getTypeUtils();

		return types.isSubtype(providerImplementer.asType(), providerType);
	}

	/**
	 * 读取 AutoService 上的 value 值
	 *
	 * @param annotationMirror AnnotationMirror
	 * @return value 集合
	 */
	private Set<TypeMirror> getValueFieldOfClasses(AnnotationMirror annotationMirror) {
		return getAnnotationValue(annotationMirror, "value").accept(new SimpleAnnotationValueVisitor8<Set<TypeMirror>, Void>() {
			@Override
			public Set<TypeMirror> visitType(TypeMirror typeMirror, Void v) {
				Set<TypeMirror> declaredTypeSet = new HashSet<>(1);
				declaredTypeSet.add(typeMirror);
				return Collections.unmodifiableSet(declaredTypeSet);
			}

			@Override
			public Set<TypeMirror> visitArray(List<? extends AnnotationValue> values, Void v) {
				return values.stream().flatMap(value -> value.accept(this, null).stream()).collect(Collectors.toSet());
			}
		}, null);
	}

	/**
	 * Returns a {@link ExecutableElement} and its associated {@link AnnotationValue} if such
	 * an element was either declared in the usage represented by the provided
	 * {@link AnnotationMirror}, or if such an element was defined with a default.
	 *
	 * @param annotationMirror AnnotationMirror
	 * @param elementName      elementName
	 * @return AnnotationValue map
	 * @throws IllegalArgumentException if no element is defined with the given elementName.
	 */
	public AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String elementName) {
		Objects.requireNonNull(annotationMirror);
		Objects.requireNonNull(elementName);
		for (Map.Entry<ExecutableElement, AnnotationValue> entry : getAnnotationValuesWithDefaults(annotationMirror).entrySet()) {
			if (entry.getKey().getSimpleName().contentEquals(elementName)) {
				return entry.getValue();
			}
		}
		String name = typeHelper.getType(annotationMirror);
		throw new IllegalArgumentException(String.format("@%s does not define an element %s()", name, elementName));
	}

	/**
	 * Returns the {@link AnnotationMirror}'s map of {@link AnnotationValue} indexed by {@link
	 * ExecutableElement}, supplying default values from the annotation if the annotation property has
	 * not been set. This is equivalent to {@link
	 * Elements#getElementValuesWithDefaults(AnnotationMirror)} but can be called statically without
	 * an {@link Elements} instance.
	 *
	 * <p>The iteration order of elements of the returned map will be the order in which the {@link
	 * ExecutableElement}s are defined in {@code annotation}'s {@linkplain
	 * AnnotationMirror#getAnnotationType() type}.
	 *
	 * @param annotation AnnotationMirror
	 * @return AnnotationValue Map
	 */
	public Map<ExecutableElement, AnnotationValue> getAnnotationValuesWithDefaults(AnnotationMirror annotation) {
		Map<ExecutableElement, AnnotationValue> values = new HashMap<>(32);
		Map<? extends ExecutableElement, ? extends AnnotationValue> declaredValues = annotation.getElementValues();
		for (ExecutableElement method : ElementFilter.methodsIn(annotation.getAnnotationType().asElement().getEnclosedElements())) {
			// Must iterate and put in this order, to ensure consistency in generated code.
			if (declaredValues.containsKey(method)) {
				values.put(method, declaredValues.get(method));
			} else if (method.getDefaultValue() != null) {
				values.put(method, method.getDefaultValue());
			} else {
				String name = typeHelper.getType(method);
				throw new IllegalStateException("Unset annotation value without default should never happen: " + name + '.' + method.getSimpleName() + "()");
			}
		}
		return Collections.unmodifiableMap(values);
	}

	public AnnotationMirror getAnnotationMirror(Element element, Class<? extends Annotation> annotationClass) {
		String annotationClassName = annotationClass.getCanonicalName();
		for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
			String name = typeHelper.getType(annotationMirror);
			if (name.contentEquals(annotationClassName)) {
				return annotationMirror;
			}
		}
		return null;
	}

}
