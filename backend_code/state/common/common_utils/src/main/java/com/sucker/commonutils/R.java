package com.sucker.commonutils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

//统一返回结果类
@Data
public class R {

    //@ApiModelProperty(value = "是否成功")
    private Boolean success;

    //@ApiModelProperty(value = "返回码")
    private Integer code;

    //@ApiModelProperty(value = "返回消息")
    private String message;

    //@ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<String, Object>();

    //构造方法私有化
    private R(){}

    //成功静态方法
    public static R ok(){
        R result = new R();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("成功");
        return result;
    }

    //失败静态方法
    public static R error(){
        R result = new R();
        result.setSuccess(false);
        result.setCode(ResultCode.ERROR);
        result.setMessage("失败");
        return result;
    }

    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }
    public R message(String message){
        this.setMessage(message);
        return this;
    }
    public R code(Integer code){
        this.setCode(code);
        return this;
    }
    public R data(String key, Object value){
        this.data.put(key, value);
        return this;
    }
    public R data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

}
