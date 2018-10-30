package com.bfd.dao.mapper;

import com.bfd.bean.CompanyBean;
import com.bfd.param.vo.IpRangeVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 统计查询--用户使用查询mapper
 *
 * @author jiang.liu
 * @date 2018-09-14
 */
@Repository
public interface StatisticsUserMapper {

    /**
     * 根据平台商id统计栏目个数
     *
     * @param id 数据包id
     * @return
     */
    int getColumnTotal(long id);

    /**
     * 根据平台商id统计数据包个数
     *
     * @param id 数据包id
     * @return
     */
    int getDataPackageTotal(long id);

    /**
     * 根据平台商id统计客户单位个数
     *
     * @param id 数据包id
     * @return
     */
    int getCustomerTotal(long id);

    /**
     * 根据平台商id统计接口数量
     *
     * @param id 数据包id
     * @return
     */
    int getApiTotal(long id);

    /**
     * 根据平台商id获取客户单位列表
     *
     * @param id 平台商id
     * @return
     */
    List<CompanyBean> getCompanyList(long id);

    /**
     * 根据客户单位id获取ip区间列表
     *
     * @param id 客户单位id
     * @return
     */
    List<IpRangeVO> getIpRangList(long id);

}
