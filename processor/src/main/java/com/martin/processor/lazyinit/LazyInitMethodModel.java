package com.martin.processor.lazyinit;

import com.martin.annotation.LazyInit;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;

/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/4 0004
 */
public class LazyInitMethodModel implements Comparable<LazyInitMethodModel> {

    /**
     * 注解实体
     */
    private LazyInit mLazy;

    /**
     * 方法名称
     */
    private Name mName;

    public LazyInitMethodModel(Element element) {
        /*
            判断注解不是方法,则抛出异常
         */
        if (element.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException(
                    String.format("Only method can be annotated with @%s",
                            LazyInit.class.getSimpleName()));
        }
        this.mLazy = element.getAnnotation(LazyInit.class);
        this.mName = element.getSimpleName();
    }

    public LazyInit getmLazy() {
        return mLazy;
    }

    public void setmLazy(LazyInit mLazy) {
        this.mLazy = mLazy;
    }

    public Name getmName() {
        return mName;
    }

    public void setmName(Name mName) {
        this.mName = mName;
    }

    @Override
    public int compareTo(LazyInitMethodModel o) {
        return o.getmLazy().priority()-this.getmLazy().priority();
    }
}
