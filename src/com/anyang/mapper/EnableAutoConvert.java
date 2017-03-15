package com.anyang.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于类型不对的时候自动转换  比如 转string  或者list转set
 * Created by Anyang on 2017/3/2.
 */
@Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(ElementType.TYPE)
public @interface EnableAutoConvert {
}
