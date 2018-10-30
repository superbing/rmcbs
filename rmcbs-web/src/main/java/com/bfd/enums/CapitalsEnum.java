package com.bfd.enums;

import com.bfd.bean.DictBean;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @Author: chong.chen
 * @Description: 省会城市
 * @Date: Created in 13:59 2018/7/31
 * @Modified by:
 */
public enum CapitalsEnum {

    /**
     * 北京市
     */
    JING(0,"北京市"),
    /**
     *天津市
     */
    JIN(1,"天津市"),
    /**
     *上海市
     */
    HU(2,"上海市"),
    /**
     *重庆市
     */
    YU(3,"重庆市"),

    /**
     *内蒙古自治区
     */
    NEI_MENG_GU(4,"内蒙古自治区"),
    /**
     *维吾尔自治区
     */
    XIN(5,"维吾尔自治区"),
    /**
     *西藏自治区
     */
    ZANG(6,"西藏自治区"),
    /**
     *宁夏回族自治区
     */
    NING(7,"宁夏回族自治区"),
    /**
     *广西壮族自治区
     */
    GUI(8,"广西壮族自治区"),


    /**
     *黑龙江省
     */
    HEI(9,"黑龙江省"),
    /**
     *吉林省
     */
    JI(10,"吉林省"),
    /**
     *辽宁省
     */
    LIAO(11,"辽宁省"),
    /**
     *河北省
     */
    HEI_BEI(12,"河北省"),
    /**
     *山西省
     */
    SHAN_XI(13,"山西省"),
    /**
     *青海省
     */
    QING(14,"青海省"),
    /**
     *山东省
     */
    LU(15,"山东省"),
    /**
     * 河南省
     */
    HE_NAN(15,"河南省"),
    /**
     *江苏省
     */
    SU(16,"江苏省"),
    /**
     *安徽省
     */
    WAN(17,"安徽省"),
    /**
     *浙江省
     */
    ZHE(18,"浙江省"),
    /**
     *福建省
     */
    MIN(19,"福建省"),
    /**
     *江西省
     */
    GAN(20,"江西省"),
    /**
     *湖南省
     */
    XIANG(21,"湖南省"),
    /**
     *湖北省
     */
    E(22,"湖北省"),
    /**
     *广东省
     */
    YUE(23,"广东省"),
    /**
     *台湾省
     */
    TAI(24,"台湾省"),
    /**
     *海南省
     */
    QIONG(25,"海南省"),
    /**
     *甘肃省
     */
    GAN_SU(26,"甘肃省"),
    /**
     *陕西省
     */
    SHAN(27,"陕西省"),
    /**
     *四川省
     */
    CHUAN(28,"四川省"),
    /**
     *贵州省
     */
    GUI_ZHOU(29,"贵州省"),
    /**
     *云南省
     */
    YU_NAN(30,"云南省");


    private Integer key;
    private String desc;

    CapitalsEnum(Integer key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public Integer getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    public static Map<Integer,String> capitalsMap() {
        Map<Integer,String> capitalsMap = Maps.newHashMap();
        for (CapitalsEnum s : CapitalsEnum.values()){
            capitalsMap.put(s.getKey(),s.getDesc());
        }
        return capitalsMap;
    }

    public static List<DictBean> getList(){
        List<DictBean> list = Lists.newArrayList();
        for (CapitalsEnum s : CapitalsEnum.values()){
            DictBean dictBean = new DictBean();
            dictBean.setCode(s.getKey());
            dictBean.setName(s.getDesc());
            dictBean.setType("city");
            list.add(dictBean);
        }
        return list;
    }

}
