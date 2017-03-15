package com.anyang.mapper;

/**
 * Created by Anyang on 2017/3/3.
 */
public class MapperFactory {
    private static Mapper mapper=null;

    /**
     * 单例
     * @return
     */
    public synchronized static Mapper createMapper(){
        if(mapper==null){
           mapper=new Mapper();
        }
        return mapper;
    }
}
