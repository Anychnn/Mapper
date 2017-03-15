package com.anyang.mapper;

/**
 * Created by Anyang on 2017/3/2.
 */
public class DefaultConverter implements Converter<Object,Object> {

    @Override
    public Object convert(Object o) {
        return o;
    }
}
