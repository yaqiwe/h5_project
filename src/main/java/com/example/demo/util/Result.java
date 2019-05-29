package com.example.demo.util;

import lombok.Data;

/*
*@author yaqiwe
*@data 2019-05-15 15:58
**/
@Data
public class Result <T> {
    private Integer code;
    private String msg;
    private T date;
}
