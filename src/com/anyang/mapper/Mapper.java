package com.anyang.mapper;


import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 一个javaBean转换工具, 最开始是想做成ORM映射工具，但是后面发现 只能单向 因为 a+b=c  能够转换 但是不能逆向转换 c到a+b
 * 如果以后有更好的映射方案，再进行解决
 * Created by Anyang on 2017/3/2.
 */
public class Mapper {
    protected Mapper(){}

    /**
     * 集合映射
     * @param sourceList
     * @param destClass
     * @return
     * @throws MappingException
     */
    public List mapList(Collection sourceList, Class destClass) throws MappingException {
        if(sourceList ==null) return null;
        List res=new ArrayList<>();
        for(Object obj: sourceList){
            res.add(map(obj,destClass));
        }
        return res;
    }

    public List mapList(Collection sourceList) throws MappingException{
        return mapList(sourceList,null);
    }

    /**
     * 一对一映射  指定目标class
     * @param source
     * @param destClass
     * @return
     * @throws MappingException
     */
    public Object map(Object source,Class destClass) throws MappingException {
        Object dest=null;
        if(destClass==null){
            return map(source);
        }
        try {
            dest = destClass.newInstance();
            Field[] destFields=dest.getClass().getDeclaredFields();
            if(destFields==null) return null;
            for(Field destField:destFields){
                processField(dest,source,destField,destClass);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return dest;
    }

    /**
     * 注解自动映射 一对一
     * @param source
     * @return
     * @throws MappingException
     */
    public Object map(Object source) throws MappingException{
        Class sourceClass=source.getClass();
        if(!sourceClass.isAnnotationPresent(Source.class)){
            throw new MappingException("没有指定Target");
        }
        Source sourceAnnotation = (Source) sourceClass.getAnnotation(Source.class);
        return map(source,sourceAnnotation.source());
    }


    /**
     * 处理每一个要转换目标类的字段
     * 如果需要从A 转换 到B  但是没有在A中找到对应的get方法 或者 没有在 B中找到对应的set方法  直接不处理 catch该异常
     * @param dest
     * @param source
     * @param destField
     * @param destClass
     * @throws MappingException
     */
    private void processField(Object dest,Object source,Field destField,Class destClass) throws MappingException {
        try {
            //通过get方法获取 从source 对象中获取 参数 以及 方法返回类型
            //这里参数类型和方法返回类型不一定对应  因为可能是父子关系
            Object[] sourceParamAndType = getSourceParam(source,destField);

            // 处理集合 Set List 之间的互相转换
            //这里判断destParam是否为空 不为空 并且是集合 则直接作为容器
            //比如在student对象中有 List<Course> courses=new ArrayList(); 或者是 new LinkedList();作为容器
            Method destGetMethod = getMethodByName(getMethodName("get",destField.getName()),destClass);
            Object destParam = destGetMethod.invoke(dest);

            if(destParam != null && destParam instanceof Collection && sourceParamAndType[0] != null && sourceParamAndType[0] instanceof Collection){
                Collection destCollection= (Collection) destParam;
                Collection sourceCollection= (Collection) sourceParamAndType[0];
                if(sourceCollection == null || sourceCollection.size() == 0 ) return ;

                    Type type=destField.getGenericType();
                    //获取泛型值  然后寻找对应的Class
                    if(type instanceof ParameterizedType){
                        Type generic=((ParameterizedType)type).getActualTypeArguments()[0];
                        if(generic ==null) return;

                        Class genericClass= (Class) generic;

                        Iterator sourceIt=sourceCollection.iterator();
                        while (sourceIt.hasNext()){
                            destCollection.add(this.map(sourceIt.next(),genericClass));
                        }
                    }
            }else{
                //直接set进去 不用集合转换
                Method destSetMethod = getMethodByName(getMethodName("set",destField.getName()),destClass, (Class) sourceParamAndType[1]);
                destSetMethod.invoke(dest,sourceParamAndType[0]);
            }
        }  catch (MappingMethodNotFoundException e) {
            //方法未找到  不处理 该字段
            return;
        } catch (IllegalAccessException e) {
            return;
        } catch (InvocationTargetException e) {
            return;
        }

    }


    /**
     * @param source
     * @param destField
     * @return 返回对象数组  数组第一个表示返回的对象 第二个表示返回的类型
     * @throws MappingException
     * @throws MappingMethodNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private Object[] getSourceParam(Object source,Field destField) throws MappingException, MappingMethodNotFoundException, IllegalAccessException, InvocationTargetException {
        Mapping mapping=destField.getAnnotation(Mapping.class);

        if(mapping==null){
            //mapping注解是空的
            Method getMethod=getMethodByName(getMethodName("get",destField.getName()),source.getClass());
            return new Object[]{getMethod.invoke(source),getMethod.getReturnType()};
        }
        //根据mapping寻找
        String getFieldName=null;
        if(mapping.source()==null||mapping.source().length()==0){
            getFieldName=destField.getName();
        }else{
            getFieldName=mapping.source();
        }
        Converter converter=getConverter(mapping.converter());
        if(converter==null){
            Method getMethod=getMethodByName(getMethodName("get",getFieldName),source.getClass());
            return new Object[]{getMethod.invoke(source),getMethod.getReturnType()};
        }
        if(converter instanceof SingleConverter){
            Method method=getMethodByName(getMethodName("get",getFieldName),source.getClass());
            Object convertParam=converter.convert(method.invoke(source));
            return new Object[]{convertParam,
                    getMethodByName(converter.getClass(),Converter.convertMethodName).getReturnType()};
        }else if(converter instanceof CompoundConverter){
            /**
             * 这里source不能有值
             * 如果有 直接报错
             */
            if(mapping.source()!=null&&mapping.source().length()>0){
                throw new MappingException("不能同时写source并且有converter继承CompoundConverter接口:"+destField.getName());
            }
            Object convertParam=converter.convert(source);
            return new Object[]{convertParam,
                    getMethodByName(converter.getClass(),Converter.convertMethodName).getReturnType()};
        }else{
            throw new MappingException("未继承Converter接口:"+converter.getClass().getSimpleName());
        }


    }

    private Method getMethodByName(Class clazz,String methodName){
        Method[] methods=clazz.getDeclaredMethods();
        if(methods!=null){
            for(Method method:methods){
                if(methodName.equals(method.getName())){
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 根据方法名通过反射拿方法
     * 如果找不到 拿父类
     * @param name
     * @param clazz
     * @return
     */
    private Method getMethodByName(String name,Class clazz,Class... params) throws MappingMethodNotFoundException {
        Method method=null;
        if(name==null) return method;
        try {
            method=clazz.getDeclaredMethod(name,params);
        } catch (NoSuchMethodException e) {
            Class superClass=clazz.getSuperclass();
            if(superClass!=null){
                method=getMethodByName(name,clazz.getSuperclass(),params);
            }
        }
        if(method==null){
            throw new MappingMethodNotFoundException("找不到对应映射Bean的get方法:"+name);
        }
        return method;
    }

    public Converter getConverter(Class<? extends Converter> converterClass) {
        Converter converter=null;
        try {
            converter=converterClass.newInstance();
        } catch (InstantiationException e) {

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return converter;
    }

    private String getMethodName(String type,String propertyName){
        if(propertyName==null||propertyName.length()==0) return null;
        return type+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1);
    }
}
