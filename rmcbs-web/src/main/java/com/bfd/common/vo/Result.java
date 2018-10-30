package com.bfd.common.vo;

/**
 * 结果返回值对象
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年7月25日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Result<T> {

    private int code;

    private String message;

    private T data;
    
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
}
