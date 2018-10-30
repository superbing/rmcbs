package com.bfd.common.vo;

/**
 * @Author: chong.chen
 * @Description: 常量类（防止魔法数字）
 * @Date: Created in 15:04 2018/7/27
 * @Modified by:
 */
public class Constants {


    public static String DEFAULT_PASSWORD  = "123456";

    /**
     * true
     */
    public static boolean TRUE = true;

    /**
     * false
     */
    public static boolean FLASE = false;

    /**
     * 日期格式(yyyy-MM-dd)
     */
    public static String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 日期格式(yyyy-MM-dd HH:mm:ss)
     */
    public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式(yyyy年M月d日)
     */
    public static String DATE_FORMAT_CHINESE = "yyyy年M月d日";

    /**
     * 首页计算近几天常量
     */
    public static final int MINUS_ONE = -1;

    /**
     * 首页计算近7天常量
     */
    public static final int MINUS_SEVEN = -7;

    /**
     * 首页计算近几天常量
     */
    public static final int MINUS_THIRTY = -30;

    /**
     * 判断是否是平台商，t_company中的parent_company_id字段为0，则为平台商
     */
    public static final int DEVELOPER = 0;

    /**
     *平台商排序对比字段,防止sql注入
     */
    public static final String SORT_DEVELOPER = "companyName,customerNumber,createTime";

    /**
     *客户单位排序对比字段,防止sql注入
     */
    public static final String SORT_CUSTOMER = "companyCode,endTime,companyName,parentCompanyName,createTime";

    /**
     * 排序方式对比字段,防止sql注入
     */
    public static final String SORT = "desc,asc";

    /**
     * 客户单位代号为1，t_company中的parent_company_id字段为平台商ID
     */
    public static final int CUSTOMER = 1;
    
    /**
     * 客户平台商状态的默认值
     */
    public static final int STATUS = 0;
    
    /**
     * 客户平台商值
     */
    public static final int PARENT_COMPANY_ID = 0;
    
    /**
     * 无固定含义的数字
     */
    public static final int ZERO = 0;

    /**
     * 无固定含义的数字
     */
    public static final int ONE = 1;

    /**
     * 无固定含义的数字
     */
    public static final int TWO = 2;

    /**
     * 无固定含义的数字
     */
    public static final int THREE = 3;

    /**
     * 无固定含义的数字
     */
    public static final int FOUR = 4;

    /**
     * 无固定含义的数字
     */
    public static final int FIVE = 5;
    
    /**
     * 有效状态
     */
    public static final int ENABLE_STATUS = 1;
    
    public static final String START_TIME = "startTime";
    
    public static final String END_TIME = "endTime";

    /**
     * 客户端类型 PC
     */
    public static final String PC = "PC";

    /**
     * 客户端类型 APP
     */
    public static final String APP = "APP";

    /**
     * 创建时间排序
     */
    public static final String CREATE_TIME = "create_time";

    /**
     * 降序(前有空格)
     */
    public static final String DESC = " desc";

    /**
     * 降序(前有空格)
     */
    public static final String ASC = " asc";

    /**
     * 数据包、栏目新增一级的时候，默认增加的二级节点名称
     */
    public static final String WORLD = "社会";

    public static final String SCIENCE = "科学";

    public static final String MILITARY = "军事";

    public static final String LITERARYHISTORY = "文史";

    /**
     * 搜索ES中的字段名称
     */
    public static final String BUSINESSID = "businessId";

    public static final String COMPANYID = "companyId";

    public static final String TIME_STAMP = "timeStamp";

    public static final String APIID = "apiId";

    public static final String APITYPE = "apiType";

    public static final String ES_STATUS = "status";

    public static final String ADD_TIME = "addTime";

    public static final String ADD_HOUR = "addHour";

    public static final String TIME_START = " 00:00:00";

    public static final String TIME_END = " 23:59:59";

    /**
     * 防止分页
     */
    public static final int ES_ZERO = 0;


    /**
     * 生成自增ID字段名称
     */
    public static final String NEXT_VAL = "nextval";

    public static final String NAME = "name";

    /**
     * 生成自增ID类型名称
     */
    public static final String COMPANYCODE = "companyCode";


    /**
     * redis中终端数字段
     */

    public static final String TOTAL = "Total";

    /**
     * redis中终端数默认值
     */
    public static final String ZERO_STRING = "0";

    /**
     * redis中已访问数
     */
    public static final String BOOKED = "Booked";

    /**
     * redis的并发数设置
     */
    public static final String CONCURRENCY = "concurrency";

    /**
     * redis中并发数限制前缀
     */
    public static final String LIMIT_SET = "limit_set_";

    /**
     * redis的前缀用于附件字段的校验
     */
    public static final String LIMIT_EXTRA = "limit_extra_";

    /**
     * redis中ip限制前缀
     */
    public static final String LIMIT_IP = "limit_ip_";

    /**
     * redis中设备数限制前缀
     */
    public static final String LIMIT_DEVICE = "limit_device_";


    /**
     * ftp元数据文件根地址
     */
    public static final String FTP_ROOT = "/图书/";

    /**
     * ftp元数据文件存储地址,单层PDF
     */
    public static final String SINGLE_LAYER_PDF = "/单层PDF/";

    /**
     * ftp元数据文件存储地址,双层PDF
     */
    public static final String TWO_LAYER_PDF = "/双层PDF/";

    /**
     * ftp元数据文件存储地址,图像PDF
     */
    public static final String IMAGE_PDF = "/图像版PDF/";

    /**
     * ftp元数据文件存储地址,EPUB
     */
    public static final String EPUB = "/EPUB/";

    /**
     * ftp元数据文件类型,EPUB
     */
    public static final String EPUB_TYPE = ".epub";

    /**
     * ftp元数据文件类型,PDF
     */
    public static final String PDF_TYPE = ".pdf";


}
