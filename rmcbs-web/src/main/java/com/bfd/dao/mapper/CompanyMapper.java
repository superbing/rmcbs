package com.bfd.dao.mapper;

import com.bfd.bean.CompanyBean;
import com.bfd.param.vo.CompanyVO;
import com.bfd.param.vo.edgesighvo.CompanyTreeDTO;
import com.bfd.param.vo.statisticsuser.CustomerQueryVO;
import com.bfd.param.vo.statisticsuser.DeveloperQueryVO;
import com.bfd.param.vo.statisticsuser.UserQueryQO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author: chong.chen
 * @Description: 平台商/客户单位 Mapper
 * @Date: Created in 14:36 2018/7/27
 * @Modified by:
 */
@Repository
public interface CompanyMapper {

    /**
     * 获取平台旗下策略统计
     * @param userQueryQO
     * @return
     */
    List<DeveloperQueryVO> getDeveloperQuery(UserQueryQO userQueryQO);

    /**
     * 获取客户单位的策略统计
     * @param userQueryQO
     * @return
     */
    List<CustomerQueryVO> getCustomerQuery(UserQueryQO userQueryQO);

    /**
     * 获取全部平台商
     * 
     * @param companyVO
     * @return
     */
    List<CompanyBean> getDevelopersList(CompanyVO companyVO);

    /**
     * 获取平台商分页展示
     * @param companyVO
     * @return
     */
    List<CompanyBean> getDevelopersPage(CompanyVO companyVO);

    /**
     * 获取客户单位列表
     *
     * @param companyVO
     * @return
     */
    List<CompanyBean> getCustomerList(CompanyVO companyVO);

    /**
     * 获取客户单位总数
     * @return
     */
    long getCustomerTotal();

    /**
     * 获取客户单位列表
     * @return
     */
    List<CompanyTreeDTO> getCompanyTree();

    /**
     * 根据平台商ID集合获取其下属客户单位
     * @param ids
     * @return
     */
    List<CompanyTreeDTO> getCompanyIdTree(@Param(value = "ids") List<Long> ids);

    /**
     *
     * @return
     */
    Map<Long,Long> getCustomerNumber();

    /**
     * 根据ID获取平台商/客户单位信息
     * 
     * @param id
     * @return
     */
    CompanyBean getCompanyById(@Param(value = "id") long id);


    /**
     * 根据客户单位id查询其父亲的状态
     * @param id
     * @return
     */
    int getParentStatus(@Param(value = "id") long id);

    /**
     * 根据客户单位的ID集合
     * @param CompanyCodes
     * @return
     */
    List<CompanyBean> getCompanyByCompanyCodes(@Param(value = "CompanyCodes") List<String> CompanyCodes);

    /**
     * 根据平台商ID获取客户单位
     * 
     * @param id
     * @return
     */
    List<CompanyBean> getCustomerById(@Param(value = "id") long id);

    /**
     * 根据平台商ID获取旗下客户单位名称
     * @param id
     * @return
     */
    List<CompanyBean> getCustomersName(@Param(value = "id") long id);

    /**
     * 添加平台商/客户单位信息
     * 
     * @param companyBean 数据包对象
     * @return
     */
    long insert(CompanyBean companyBean);
    
    /**
     * 更新平台商/客户单位信息
     * 
     * @param companyBean
     * @return
     */
    int update(CompanyBean companyBean);
    
    /**
     * 批量更新平台商/客户单位状态
     * 
     * @param status
     * @param ids
     * @param updateUser
     * @return
     */
    int updateStatus(@Param(value = "status") int status,@Param(value = "updateUser") Long updateUser, @Param(value = "ids") List<Long> ids);
    
    /**
     * 删除平台商/客户单位信息
     * 
     * @param id 平台商/客户单位id
     * @return
     */
    boolean delete(long id);
    
    /**
     * 删除批量客户单位
     * 
     * @param companyBeans 客户单位集合
     * @return
     */
    int deleteCustomers(@Param(value = "List") List<CompanyBean> companyBeans);
    
    /**
     * 按条件查询
     * 
     * @param companyVO
     * @return
     */
    List<CompanyBean> getCompany(CompanyVO companyVO);
    
    /**
     * 修改客户单位的下载或者离线加密包状态
     * 
     * @param company
     * @see [类、类#方法、类#成员]
     */
    void updateCompanyByAccessKey(CompanyBean company);
    
    /**
     * 根据companyCode查询客户单位
     * 
     * @param companyCode
     * @return
     * @see [类、类#方法、类#成员]
     */
    CompanyBean getCompanyByCode(String companyCode);
    
    /**
     * 根据accessKey查询
     * 
     * @param accessKey
     * @return
     * @see [类、类#方法、类#成员]
     */
    CompanyBean getByAccessKey(String accessKey);

    /**
     * 根据名称查询客户单位
     * @param name
     * @return
     */
    List<CompanyBean> getCustomerByName(String name);

    /**
     * 根据名称查询平台商
     * @param name
     * @return
     */
    List<CompanyBean> getDeveloperByName(String name);

}
