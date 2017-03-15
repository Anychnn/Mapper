package com.anyang.mapper;

/**
 * Created by Anyang on 2017/3/2.
 */
public interface Converter<K,T> {
    public static String convertMethodName="convert";
    public T convert(K k) throws MappingException;
}
