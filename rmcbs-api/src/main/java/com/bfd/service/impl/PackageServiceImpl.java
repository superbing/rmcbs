package com.bfd.service.impl;

import com.bfd.bean.PackageBean;
import com.bfd.common.vo.PageVO;
import com.bfd.dao.mapper.PackageMapper;
import com.bfd.dao.mapper.PrivatePackageMapper;
import com.bfd.enums.BookTypeEnum;
import com.bfd.service.PackageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: bing.shen
 * @date: 2018/8/8 17:51
 * @Description:
 */
@Service
public class PackageServiceImpl implements PackageService {

    @Autowired
    private PackageMapper packageMapper;

    @Autowired
    private PrivatePackageMapper privatePackageMapper;


    @Override
    public List<PackageBean> queryAllPackage(Long businessId) {
        List<PackageBean> list = packageMapper.queryPackageList(businessId);
        List<PackageBean> privateList = privatePackageMapper.queryPackageList(businessId);
        if(CollectionUtils.isEmpty(privateList)){
            return list;
        }else{
            privateList.addAll(list);
            return privateList;
        }
    }

    @Override
    public PageVO<PackageBean> bookPage(int pageNum, int pageSize, Long packageId, int type) {
        PageHelper.startPage(pageNum, pageSize);
        List<PackageBean> list;
        if(type==BookTypeEnum.PACKAGE.getKey()){
            list = packageMapper.queryMetadataList(packageId);
        }else{
            list = privatePackageMapper.queryMetadataList(packageId);
        }
        PageInfo<PackageBean> pageInfo = new PageInfo<>(list);
        return new PageVO<>(pageNum, pageSize, pageInfo.getTotal(), pageInfo.getList());
    }
}
