package com.bfd.common.vo;

import java.util.List;

import lombok.Data;

/**
 * PageVO
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年7月31日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
public class PageVO<T> {
    
    private int current;
    
    private int pageSize;
    
    private long total;
    
    private List<T> list;
    
    public PageVO(int current, int pageSize, long total, List<T> list) {
        this.current = current;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
    }
    
}
