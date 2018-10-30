package com.bfd.service;

import com.bfd.bean.PrivateDataPackageBean;
import com.bfd.common.vo.PageVO;
import com.bfd.param.vo.BookVO;
import com.bfd.param.vo.ResourceBookQueryVO;
import com.bfd.param.vo.ResourceQueryVO;

import java.util.List;

/**
 * 个性化数据包service服务层
 *
 * @author jiang.liu
 * @date 2018-08-03
 */
public interface PrivateDataPackageService {

    /**
     * 获取某个单位的个性化数据包分级树结构
     * @param businessId  单位id
     * @return
     */
    List<PrivateDataPackageBean> getPrivatePackageList(long businessId);

    /**
     * 添加个性化数据包信息
     *
     * @param privatedataPackageBean 个性化数据包对象
     * @return
     */
    long insert(PrivateDataPackageBean privatedataPackageBean);

    /**
     * 更新个性化数据包信息
     *
     * @param privatedataPackageBean 个性化数据包对象
     * @return
     */
    long update(PrivateDataPackageBean privatedataPackageBean);

    /**
     * 删除个性化数据包信息
     *
     * @param id 个性化数据包id
     * @return
     */
    boolean delete(long id);

    /**
     * 向个性化数据包添加图书
     *
     * @param privateDataPackageId 个性化数据包id
     * @param metadataIds   元数据id集合
     * @return
     */
    boolean addBookForPrivateDataPackage(long privateDataPackageId, List<Long> metadataIds);

    /**
     * 从个性化数据包里面批量删除图书
     *
     * @param privateDataPackageId 个性化数据包id
     * @param metadataIds   元数据id集合
     * @return
     */
    boolean batchDeleteFromPrivateDataPackage(long privateDataPackageId, List<Long> metadataIds);

    /**
     * 根据个性化数据包数据包id查询图书列表
     *
     * @param privateDataPackageId 数据包id
     * @return
     */
    List<BookVO> getMetadataListByPrivateDataPackageId(long privateDataPackageId);

    /**
     * 根据条件查询图书列表
     *
     * @param resourceQueryVO 参数对象
     * @return
     */
    List<BookVO> getMetadataListForPrivatePackage(ResourceQueryVO resourceQueryVO);

    /**
     * 按条件查询所有图书信息列表（包含已加入个性化数据包的数据）
     *
     * @param resourceBookQueryVO 参数
     * @return
     */
    PageVO<BookVO> queryMetadataList(ResourceBookQueryVO resourceBookQueryVO);

}
