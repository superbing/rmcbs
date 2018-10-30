package com.bfd.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bfd.common.vo.Result;

/**
 * 处理所有Controller抛出的异常
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年7月25日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    
    /**
     * 捕获所有Controller的异常
     * 
     * @param e
     * @return
     */
    @ExceptionHandler({RmcbsException.class})
    @ResponseBody
    public Object handleRmcbsException(RmcbsException e) {
        log.error(e.getMessage(), e);
        Result<Object> result = new Result<>(HttpStatus.EXPECTATION_FAILED.value(), HttpStatus.EXPECTATION_FAILED.getReasonPhrase(), e.getMessage());
        return result;
    }

    /**
     * 捕获所有Controller的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public Object handleException(Exception e) {
        log.error(e.getMessage(), e);
        Result<Object> result = new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
        return result;
    }
}
