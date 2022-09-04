package com.wsk.serviceutil.exception;

import com.wsk.serviceutil.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author:WuShangke
 * @create:2022/8/14-16:22
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    //全局异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail().message("发生异常");
    }

    //自定义异常
    @ResponseBody
    @ExceptionHandler(GgktException.class)
    public Result error(GgktException e){
        e.printStackTrace();
        return Result.fail().message("执行自定义异常");
    }
}
