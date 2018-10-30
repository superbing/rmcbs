package com.bfd.utils;

import java.util.regex.Pattern;

public class MatchUtil {
    
    /**
     * 方法二：推荐，速度最快 判断是否为整数
     * 
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    
}
