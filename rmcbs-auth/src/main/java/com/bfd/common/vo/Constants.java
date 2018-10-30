package com.bfd.common.vo;

/**
 * @Author: chong.chen
 * @Description: 常量类（防止魔法数字）
 * @Date: Created in 15:04 2018/7/27
 * @Modified by:
 */
public class Constants{


    /**
     * 判断是否是平台商，t_company中的parent_company_id字段为0，则为平台商，
     * 不为0则是客户单位
     */
    public static final int ISDEVELOPERS =  0;

    /**
     * 客户平台商状态的默认值
     */
    public static final int STATUS =  0;

    /**
     * 客户平台商值
     */
    public static final int PARENT_COMPANY_ID =  0;

    /**
     * 无固定含义的数字
     */
    public static final int ZERO = 0;


}
