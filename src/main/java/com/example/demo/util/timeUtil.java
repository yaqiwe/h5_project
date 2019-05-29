package com.example.demo.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
*@author yaqiwe
*@data 2019-05-16 14:57
* 将时间转化为Data对象
**/
public class timeUtil {
    public static Date stringToData(String stringTime){
        Date date;
        DateFormat simpleDateFormat;
        if (stringTime!=null){
            try {
                simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                date=simpleDateFormat.parse(stringTime);
                return date;
            } catch (ParseException e) { }
            try {
                simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date=simpleDateFormat.parse(stringTime);
                return date;
            }catch (ParseException e){ }
            return null;
        }else
            return null;
    }
}
