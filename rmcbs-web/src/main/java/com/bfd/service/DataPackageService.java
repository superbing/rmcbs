package com.bfd.service;

import com.bfd.bean.DataPackageBean;
import com.bfd.common.vo.PageVO;
import com.bfd.param.vo.BookVO;
import com.bfd.param.vo.ResourceBookQueryVO;
import com.bfd.param.vo.ResourceQueryVO;

import java.util.List;

/**
 * 数据包service服务层
 *
 * @author jiang.liu
 * @date 2018-07-26
 */
public interface DataPackageService {

    /**
     * 获取数据包分级树结构
     *
     * @return
     */
    List<DataPackageBean> getPackageList();

    /**
     * 根据id获取下一级数据包列表
     *
     * @param id 数据包id
     * @return
     */
    List<DataPackageBean> getPackageListByParentId(long id);

    /**
     * 添加数据包信息
     *
     * @param dataPackageBean 数据包对象
     * @return
     */
    long insert(DataPackageBean dataPackageBean);

    /**
     * 更新数据包信息
     *
     * @param dataPackageBean 数据包对象
     * @return
     */
    long update(DataPackageBean dataPackageBean);

    /**
     * 删除数据包信息
     *
     * @param id 数据包id
     * @return
     */
    boolean delete(long id);

    /**
     * 向数据包添加图书
     *
     * @param dataPackageId 数据包id
     * @param metadataIds   元数据id集合
     * @return
     */
    boolean addBookMetadata(long dataPackageId, List<Long> metadataIds);

    /**
     * 从数据包里面批量删除图书
     *
     * @param dataPackageId 数据包id
     * @param metadataIds   元数据id集合
     * @return
     */
    boolean batchDeleteFromDataPackage(long dataPackageId, List<Long> metadataIds);

    /**
     * 根据数据包id查询图书列表
     *
     * @param dataPackageId 数据包id
     * @return
     */
    List<BookVO> getMetadataListByPackageId(long dataPackageId);

    /**
     * 按条件查询所有图书信息列表（包含已加入数据包的数据）
     *
     * @param resourceBookQueryVO 参数
     * @return
     */
    PageVO<BookVO> queryMetadataList(ResourceBookQueryVO resourceBookQueryVO);

    /**
     * 根据条件查询图书列表
     *
     * @param resourceQueryVO 参数对象
     * @return
     */
    List<BookVO> getMetadataListForPackage(ResourceQueryVO resourceQueryVO);
}


