package com.bfd.common.exception;

/**
 * @author: bing.shen
 * @date: 2018/8/23 19:07
 * @Description:
 */
public class RmcbsException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public RmcbsException(String message) {
        super(message);
    }
}
