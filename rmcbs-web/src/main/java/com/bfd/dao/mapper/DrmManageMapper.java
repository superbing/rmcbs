package com.bfd.dao.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DRM管理
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年8月17日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Repository
public interface DrmManageMapper {
    
    /**
     * 根据单位id查询元数据唯一标识列表
     * 
     * @param companyId
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<String> getUniqueIdListByCompanyId(long companyId);
    
}
