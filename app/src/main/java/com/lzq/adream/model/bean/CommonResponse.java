package com.lzq.adream.model.bean;

import java.io.Serializable;

/**
 * 返回通用格式数据
 */
public class CommonResponse<T> implements Serializable {

    private T value;
    public T getValue() {
        return value;
    }

    private T HeWeather5;

    public T getHeWeather5() {
        return HeWeather5;
    }

    public void setHeWeather5(T heWeather5) {
        HeWeather5 = heWeather5;
    }

    public void setValue(T value) {
        this.value = value;
    }


    private String methodName;

    /**
            * 数据库操作动作update/delete/insert
     **/
    private String mark;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    private boolean returnStatus;
    public boolean isReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(boolean returnStatus) {
        this.returnStatus = returnStatus;
    }

 private static final long serialVersionUID = 3713874654852996944L;
    //返回状态码
    private String code;
    //数据


    //返回状态码描述
    private String message;
    //返回状态 true正确 false出错


    /**
     * 调用的方法名
     */


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }




    @Override
    public String toString() {
        return "CommonResponse{" +
                "code='" + code + '\'' +
                ", value=" + value +
                ", message='" + message + '\'' +
                ", returnStatus=" + returnStatus +
                ", methodName='" + methodName + '\'' +
                ", mark='" + mark + '\'' +
                '}';
    }
}
