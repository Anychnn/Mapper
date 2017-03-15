package com.anyang.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Anyang on 2017/3/2.
 */
public class DateToStringConverter implements SingleConverter<Date,String> {
    @Override
    public String convert(Date date) {
        if(date==null) return "";
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
