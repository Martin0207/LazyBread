package com.martin.processor.lazycycle;

import com.martin.annotation.model.CycleModel;
import com.martin.processor.util.ProcessorUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/15 0015
 */
public class LazyCycleModel {

    /**
     * 被注解的方法集
     * 以循环间隔来区分保存
     */
    private Map<Integer, List<LazyCycleMethodModel>> mMap;

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

    LazyCycleModel(Elements mElementUtils, Messager mMessager, TypeElement mTypeElement) {
        this.mElementUtils = mElementUtils;
        this.mMessager = mMessager;
        this.mTypeElement = mTypeElement;
        mMap = new HashMap<>();
    }

    /**
     * 添加方法
     */
    void addMethod(LazyCycleMethodModel method) {
        int interval = method.cycle.interval();
        List<LazyCycleMethodModel> list;
        if (!mMap.containsKey(interval)) {
            list = new ArrayList<>();
            mMap.put(interval, list);
        } else {
            list = mMap.get(interval);
        }
        list.add(method);
    }

    /**
     * 生成文件
     */
    JavaFile generate() {
        TypeName typeName = TypeName.get(mTypeElement.asType());
        /*
            构建类
         */
        String packageName = ProcessorUtils.getPackageName(mElementUtils, mTypeElement);
        String className = ProcessorUtils.getClassName(mElementUtils, mTypeElement);
        String name = ClassName.get(packageName, className).simpleName() + "$LazyCycle";
        ClassName iLazyCycle = ClassName.get("com.martin.core.interfaces", "ILazyCycle");
        TypeName cycleCallBackListener = ProcessorUtils.getTypeName("com.martin.core.interfaces", "OnCycleCallBackListener");

        TypeSpec.Builder builder = TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(iLazyCycle, typeName))
                .addSuperinterface(cycleCallBackListener);
        /*
            构建成员变量
         */
        buildField(typeName, builder);
        /*
            构建方法
         */
        buildMethod(typeName, builder);

        return JavaFile.builder(packageName, builder.build()).build();
    }

    /**
     * 构建成员变量
     */
    private void buildField(TypeName typeName, TypeSpec.Builder builder) {
        FieldSpec mObjField = FieldSpec.builder(typeName, "mObj", Modifier.PRIVATE).build();
        FieldSpec mModelField = FieldSpec.builder(
                ProcessorUtils.getTypeName("com.martin.core.model",
                        "HandlerModel"),
                "mModel",
                Modifier.PRIVATE)
                .build();
        FieldSpec mMapField = FieldSpec.builder(
                ProcessorUtils.getTypeName("java.util", "HashMap"),
                "mMap")
                .build();
        builder.addField(mObjField)
                .addField(mModelField)
                .addField(mMapField);
    }

    /**
     * 构建方法
     */
    private void buildMethod(TypeName typeName, TypeSpec.Builder builder) {
        MethodSpec initMethod = buildInitMethod(typeName);
        MethodSpec resumeMethod = MethodSpec.methodBuilder("resume")
                .addJavadoc("开始循环\n")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("mModel.resume()")
                .build();
        MethodSpec pauseMethod = MethodSpec.methodBuilder("pause")
                .addJavadoc("停止循环\n")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("mModel.pause()")
                .build();
        MethodSpec onCallBackMethod = buildOnCallBackMethod();
        builder.addMethod(initMethod)
                .addMethod(resumeMethod)
                .addMethod(pauseMethod)
                .addMethod(onCallBackMethod);
    }

    /**
     * 构建初始化方法
     */
    private MethodSpec buildInitMethod(TypeName typeName) {
        Map<String, Class<?>> map = new HashMap<>();
        map.put("list", ArrayList.class);
        map.put("bean", CycleModel.class);
        MethodSpec.Builder initBuilder = MethodSpec.methodBuilder("init")
                .addJavadoc("初始化\n")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(typeName, "obj")
                .addStatement("mObj = obj")
                .addNamedCode("mMap = new HashMap<String,$bean:T>();\n", map)
                .addNamedCode("$list:T list = new ArrayList<Integer>();\n", map);
        for (Integer i : mMap.keySet()) {
            initBuilder.addStatement("list.add(" + i + ")");
        }
        for (List<LazyCycleMethodModel> value : mMap.values()) {
            for (LazyCycleMethodModel model : value) {
                initBuilder.addStatement(
                        String.format("mMap.put(\"%s\",new CycleModel(%d,%d))",
                                model.name.toString(), model.cycle.interval(), model.cycle.times()));
            }
        }
        return initBuilder
                .addStatement("mModel = new HandlerModel(list,this)")
                .build();
    }

    /**
     * 构建循环回调方法
     */
    private MethodSpec buildOnCallBackMethod() {
        MethodSpec.Builder onCallBackBuilder = MethodSpec.methodBuilder("onCallBack")
                .addJavadoc("循环回调\n")
                .addParameter(TypeName.INT, "key")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.BOOLEAN)
                .addComment("==0时,没有判定;>0时继续;<0时停止循环")
                .addStatement("int autoContinue = 0")
                .addCode("switch (key){\n");

        for (Integer key : mMap.keySet()) {
            List<LazyCycleMethodModel> models = mMap.get(key);
            onCallBackBuilder.addCode("\tcase " + key + ":\n");
            /*
                判断是否有无限循环的方法
             */
            boolean unlimited = false;
            for (LazyCycleMethodModel model : models) {
                unlimited = model.cycle.times() <= 0;
                if (unlimited) {
                    break;
                }
            }
            if (unlimited) {
                onCallBackBuilder.addStatement("\t\t\tautoContinue = 1");
            }
            for (LazyCycleMethodModel model : models) {
                if (model.cycle.times() > 0) {
                    String name = model.name.toString();
                    onCallBackBuilder
                            .addStatement(String.format("\t\t\tCycleModel %s = (CycleModel)mMap.get(\"%s\")", name, name))
                            .addStatement(String.format("\t\t\t%s.completionTimes++", name))
                            .addCode(String.format("\t\t\tif (%s.times >= %s.completionTimes){\n", name, name))
                            .addStatement("\t\t\t\tmObj.$N()", model.name);
                    if (unlimited) {
                        onCallBackBuilder.addCode("\t\t\t}\n");
                    } else {
                        onCallBackBuilder
                                .addStatement("\t\t\t\tautoContinue = 1")
                                .addCode("\t\t\t}else{\n")
                                .addStatement("\t\t\t\tif(autoContinue == 0) autoContinue = -1")
                                .addCode("\t\t\t}\n");
                    }
                } else {
                    onCallBackBuilder.addStatement("\t\t\tmObj.$N()", model.name);
                }
            }
            onCallBackBuilder.addCode("\t\tbreak;\n");
        }
        return onCallBackBuilder
                .addCode("}")
                .addStatement("return autoContinue>0")
                .build();
    }

}
