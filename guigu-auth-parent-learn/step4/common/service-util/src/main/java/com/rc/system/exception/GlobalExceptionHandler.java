package com.rc.system.exception;

import com.rc.common.result.Result;
import com.rc.common.result.ResultCodeEnum;
// import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// 表示通过 AOP 方式 加上异常处理
@ControllerAdvice
public class GlobalExceptionHandler {

    //1 全局异常处理
    @ExceptionHandler(Exception.class)
    // 此处因为没有 controller 中的 @RestController ，故不会返回json数据。故此处加上 @ResponseBody 以便返回json数据
    @ResponseBody
    public Result error(Exception e) {
        System.out.println("全局....");
        e.printStackTrace();    // 打印异常信息
        return Result.fail().message("执行了全局异常处理");
    }

    //2 特定异常处理      除以0会导致 ArithmeticException 异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e) {
        System.out.println("特定......");
        e.printStackTrace();
        return Result.fail().message("执行了特定异常处理");
    }

    //3 自定义异常处理     需要手动抛出异常
    @ExceptionHandler(GuiguException.class)
    @ResponseBody
    public Result error(GuiguException e) {
        e.printStackTrace();
        return Result.fail().code(e.getCode()).message(e.getMsg());
    }

    /**
     * spring security异常
     * @param e
     * @return
     */
    // @ExceptionHandler(AccessDeniedException.class)
    // @ResponseBody
    // public Result error(AccessDeniedException e) throws AccessDeniedException {
    //     return Result.fail().code(ResultCodeEnum.PERMISSION.getCode()).message("没有当前功能操作权限");
    // }
}
