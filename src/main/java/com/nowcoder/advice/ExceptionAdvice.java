package com.nowcoder.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice(basePackages = {"com.nowcoder.controller"})
public class ExceptionAdvice {
    @ExceptionHandler
    @ResponseBody
    public String handler(Exception e){
        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String detailStackException=stringWriter.toString();

        // TODO: 17-12-9 异常详细信息 未完成
        return "这是自定义的异常处理器，当前异常类型为："+e.getClass()+"<br>"+"异常详细信息：<br>"+e.getMessage()
                +detailStackException;
    }
}
