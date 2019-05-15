package com.martin.processor.lazycycle;

import com.martin.annotation.LazyCycle;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;

/*
 * @author martin
 * @Emily martin0207mfh@163.com
 * @date 2019/5/15 0015
 */
public class LazyCycleMethodModel {

    public Name name;
    public LazyCycle cycle;

    public LazyCycleMethodModel(Element element) {
        this.name = element.getSimpleName();
        this.cycle = element.getAnnotation(LazyCycle.class);
    }

}
