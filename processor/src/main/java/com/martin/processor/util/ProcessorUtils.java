package com.martin.processor.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/5 0005
 */
public class ProcessorUtils {


    /**
     * 获取类型名
     *
     * @param packageName 包名
     * @param simpleName  类名
     * @return TypeName
     */
    public static TypeName getTypeName(String packageName, String simpleName) {
        return ClassName.get(packageName, simpleName, new String[0]);
    }

    /**
     * 获取包名
     */
    public static String getPackageName(Elements elements, TypeElement typeElement) {
        return elements.getPackageOf(typeElement).getQualifiedName().toString();
    }

    /**
     * 获取类名
     */
    public static String getClassName(Elements elements, TypeElement typeElement) {
        int packageLength = getPackageName(elements, typeElement).length();
        return typeElement.getQualifiedName().toString().substring(packageLength + 1).replace(".", "$");
    }

}
