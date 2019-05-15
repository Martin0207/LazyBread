package com.martin.processor.lazyinit;

import com.google.auto.service.AutoService;
import com.martin.annotation.LazyInit;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/4 0004
 */
@AutoService(Processor.class)
public class LazyInitProcessor extends AbstractProcessor {

    /**
     * 文件相关的辅助类
     */
    private Filer mFiler;

    /**
     * 元素相关的辅助类
     */
    private Elements mElementUtils;

    /**
     * 日志相关的类
     */
    private Messager mMessager;

    /**
     * 注解类Map
     */
    private Map<String, LazyInitModel> mMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mElementUtils = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
        mMap = new HashMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (Element e : roundEnvironment.getElementsAnnotatedWith(LazyInit.class)) {
            TypeElement typeElement = (TypeElement) e.getEnclosingElement();
            String qualifiedName = typeElement.getQualifiedName().toString();
            if (!mMap.containsKey(qualifiedName)) {
                mMap.put(qualifiedName, new LazyInitModel(mElementUtils, mMessager, typeElement));
            }
            mMap.get(qualifiedName).addMethod(new LazyInitMethodModel(e));
        }
        for (LazyInitModel model : mMap.values()) {
            try {
                model.generate().writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        LinkedHashSet<String> types = new LinkedHashSet<>();
        types.add(LazyInit.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
