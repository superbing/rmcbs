package com.bfd.service;

import java.util.List;

import com.bfd.bean.PackageBean;
import com.bfd.common.vo.PageVO;

/**
 * @author: bing.shen
 * @date: 2018/8/8 17:48
 * @Description:
 */
public interface PackageService {
    
    /**
     * 查询公共栏目
     * 
     * @return
     */
    List<PackageBean> queryAllPackage(Long businessId);
    
    /**
     * 分页查询数据包下的书籍
     * 
     * @return
     */
    PageVO<PackageBean> bookPage(int pageNum, int pageSize, Long packageId, int type);
}
