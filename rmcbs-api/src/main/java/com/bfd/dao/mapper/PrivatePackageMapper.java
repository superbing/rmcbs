package com.bfd.dao.mapper;

import com.bfd.bean.PackageBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  栏目 mapper
 *
 * @author bing.shen
 * @date 2018-07-27
 */
@Repository
public interface PrivatePackageMapper {

    /**
     * 查询公共数据包
     * @param businessId
     * @return
     */
    List<PackageBean> queryPackageList(Long businessId);

    /**
     * 查询数据包下的书籍
     * @param packageId
     * @return
     */
    List<PackageBean> queryMetadataList(Long packageId);

}


