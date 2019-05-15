package com.martin.processor.lazyinit;

import com.martin.processor.util.ProcessorUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/4 0004
 */
public class LazyInitModel {

    /**
     * 方法List
     */
    private List<LazyInitMethodModel> mMethodList;

    /**
     * 元素相关的辅助类
     */
    private Elements mElementUtils;

    /**
     * 日志相关的类
     */
    private Messager mMessager;

    /**
     * 类工具
     */
    private TypeElement mTypeElement;

    public LazyInitModel(Elements mElementUtils, Messager mMessager, TypeElement mTypeElement) {
        this.mElementUtils = mElementUtils;
        this.mMessager = mMessager;
        this.mTypeElement = mTypeElement;
        mMethodList = new ArrayList<>();
    }

    /**
     * 生成JavaFile
     *
     * @return JavaFile
     */
    JavaFile generate() {
        TypeName fragmentName = TypeName.get(mTypeElement.asType());
        FieldSpec mFragment = FieldSpec.builder(fragmentName, "mFragment", Modifier.PRIVATE)
                .build();
        FieldSpec mCallCount = FieldSpec.builder(TypeName.INT, "mCallCount", Modifier.PRIVATE)
                .build();
        MethodSpec initMethod = MethodSpec.methodBuilder("init")
                .addJavadoc("初始化\n")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(fragmentName, "fragment")
                .addStatement("mFragment = fragment")
                .addStatement("reset()")
                .build();
        MethodSpec.Builder callBuilder = MethodSpec.methodBuilder("call")
                .addJavadoc("调用方法\n")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addCode("if(mFragment.getUserVisibleHint()){\n");

        Collections.sort(mMethodList);
        for (LazyInitMethodModel model : mMethodList) {
            if (model.getmLazy().isCycle()) {
                callBuilder.addStatement("mFragment.$N()", model.getmName());
            } else {
                callBuilder.addStatement("if(mCallCount==0) mFragment.$N()", model.getmName());
            }
        }

        MethodSpec callMethod = callBuilder
                .addStatement("mCallCount++")
                .addCode("}\n")
                .build();

        MethodSpec resetMethod = MethodSpec.methodBuilder("reset")
                .addJavadoc("重置\n")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("mCallCount=0")
                .build();

        String packageName = ProcessorUtils.getPackageName(mElementUtils, mTypeElement);
        String className = ProcessorUtils.getClassName(mElementUtils, mTypeElement);
        String name = ClassName.get(packageName, className).simpleName() + "$LazyInit";
        ClassName iLazyFragment = ClassName.get("com.martin.core.interfaces", "ILazyFragment");
        TypeSpec typeSpec = TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(iLazyFragment, fragmentName))
                .addField(mFragment)
                .addField(mCallCount)
                .addMethod(initMethod)
                .addMethod(callMethod)
                .addMethod(resetMethod)
                .build();
        return JavaFile.builder(packageName, typeSpec).build();
    }

    /**
     * 添加方法Model
     *
     * @param method 方法Model
     */
    void addMethod(LazyInitMethodModel method) {
        mMethodList.add(method);
        Collections.sort(mMethodList, new Comparator<LazyInitMethodModel>() {
            @Override
            public int compare(LazyInitMethodModel lazyInitMethodModel, LazyInitMethodModel t1) {
                return lazyInitMethodModel.getmLazy().priority() - t1.getmLazy().priority();
            }
        });
    }
}
