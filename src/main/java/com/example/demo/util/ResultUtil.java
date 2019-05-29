package com.example.demo.util;
/*
*@author yaqiwe
*@data 2019-05-15 15:58
**/
public class ResultUtil {
    /**
     * 成功返回
     * @param date 返回的数据
     * @return
     */
    public static Result success(Object date){
        Result result=new Result();
        result.setCode(200);
        result.setMsg("成功");
        result.setDate(date);
        return result;
    }

    /**
     * 返回成功状态
     * @return
     */
    public static Result success(){
        return success(null);
    }

    /**
     * 失败返回
     * @param code 失败状态码
     * @param msg 失败描述
     * @return
     */
    public static Result error(Integer code, String msg){
        Result result=new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
