package com.bfd.service;

import com.bfd.bean.CompanyBean;
import com.bfd.common.vo.PageVO;
import com.bfd.param.vo.AddDeveloperVO;
import com.bfd.param.vo.CompanyStatusVO;
import com.bfd.param.vo.CompanyVO;
import com.bfd.param.vo.edgesighvo.CompanyTreeDTO;

import java.util.List;

/**
 * @Author: chong.chen
 * @Description:  平台商、客户单位 Service
 * @Date: Created in 14:32 2018/7/27
 * @Modified by:
 */
public interface CompanyService {

    /**
     * 获取平台商列表
     * @param companyVO
     * @return
     */
    PageVO<CompanyBean> getDevelopersList(CompanyVO companyVO);

    /**
     * 获取所有平台商
     * @return
     */
    List<CompanyBean> getAllDevelopersList();


    /**
     * 获取客户单位列表
     * @param companyVO
     * @return
     */
    PageVO<CompanyBean> getCustomerList(CompanyVO companyVO);

    /**
     * 获取所有客户单位接口
     * @return
     */
    List<CompanyTreeDTO> getCompanyTree();

    /**
     * 根据ID获取平台商/客户单位信息
     * @param id
     * @return
     */
    CompanyBean getCompanyById(long id);

    /**
     * 根据ID获取平台商/客户单位信息
     * @param id
     * @return
     */
    String getExtraData(long id);

    /**
     * 添加客户单位信息
     * @param companyBean
     * @return
     */
    long addCustomer(CompanyBean companyBean);

    /**
     * 添加平台商信息
     * @param addDeveloperVO
     * @return
     */
    long addDeveloper(AddDeveloperVO addDeveloperVO);

    /**
     * 更新客户单位信息
     * @param companyBean
     * @return
     */
    int updateCustomer(CompanyBean companyBean);

    /**
     *
     * 重置AccessKey
     * @param id
     * @return
     */
    int resetAccessKey(long id);

    /**
     * 更新平台商信息
     * @param addDeveloperVO
     * @return
     */
    int updateDeveloper(AddDeveloperVO addDeveloperVO);

    /**
     * 批量更新平台商/客户单位状态
     * @param companyStatusVO
     * @return
     */
    int updateStatus(CompanyStatusVO companyStatusVO);

    /**
     * 删除平台商/客户单位信息
     * @param id  平台商/客户单位id
     * @return
     */
    boolean deleteDeveloper(long id);

    /**
     * 删除平台商/客户单位信息
     * @param id  平台商/客户单位id
     * @return
     */
    boolean deleteCustomer(long id);

    /**
     * 按条件查询
     * @param companyVO
     * @return
     */
    PageVO<CompanyBean> getCompany(CompanyVO companyVO);
}
