package com.bfd.enums;

import com.bfd.bean.DictBean;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @Author: chong.chen
 * @Description:   图书状态
 * @Date: Created in 10:19 2018/8/6
 * @Modified by:
 */
public enum BookStatusEnum {

    /**
     * PDF
     */
    PDF("1","PDF"),
    /**
     * EPUB
     */
    EPUB("2","EPUB"),
    /**
     * XML
     */
    XML("3","XML");

    private String key;
    private String desc;

    BookStatusEnum(String key, String desc) {
        this.key=key;
        this.desc=desc;
    }

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    public static Map<String,String> BookStatusMap() {
        Map<String,String> BookStatusMap = Maps.newHashMap();
        for (BookStatusEnum s : BookStatusEnum.values()){
            BookStatusMap.put(s.getKey(),s.getDesc());
        }
        return BookStatusMap;
    }

    public static List<DictBean> getList(){
        List<DictBean> list = Lists.newArrayList();
        for (BookStatusEnum s : BookStatusEnum.values()){
            DictBean dictBean = new DictBean();
            dictBean.setCode(Integer.parseInt(s.getKey()));
            dictBean.setName(s.getDesc());
            dictBean.setType("book");
            list.add(dictBean);
        }
        return list;
    }
}
