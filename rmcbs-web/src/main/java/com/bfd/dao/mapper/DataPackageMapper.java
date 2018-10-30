package com.bfd.dao.mapper;

import com.bfd.bean.BusinessPackageBean;
import com.bfd.bean.DataPackageBean;
import com.bfd.param.vo.BookVO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 数据包 mapper
 *
 * @author jiang.liu
 * @date 2018-07-26
 */
@Repository
public interface DataPackageMapper {

    /**
     * 获取数据包列表
     *
     * @return
     */
    List<DataPackageBean> getPackageList();

    /**
     * 获取一级数据包集合
     * @return
     */
    List<Long> getPackageOneLevel();

    /**
     * 获取客户单位总数
     * @return
     */
    long getPackageTotal();

    /**
     * 根据id获取下一级数据包列表
     *
     * @param id 数据包id
     * @return
     */
    List<DataPackageBean> getPackageListByParentId(long id);

    /**
     * 根据id获取数据包信息
     *
     * @param id 数据包id
     * @return
     */
    DataPackageBean getPackageInfoById(long id);

    /**
     * 新增数据包信息
     *
     * @param dataPackageBean 数据包对象
     * @return
     */
    int insert(DataPackageBean dataPackageBean);

    /**
     * 更新数据包信息
     *
     * @param dataPackageBean 数据包对象
     * @return
     */
    int update(DataPackageBean dataPackageBean);

    /**
     * 删除数据包信息
     *
     * @param id 数据包id
     * @return
     */
    boolean delete(long id);

    /**
     * 批量删除数据包信息
     *
     * @param list 数据包对象列表
     * @return
     */
    boolean batchDeleteDataPackage(List<DataPackageBean> list);

    /**
     * 向数据包添加图书
     *
     * @param param 数据包和图书id关系参数
     * @return
     */
    boolean addBookMetadata(Map<String, Object> param);

    /**
     * 根据数据包id删除数据包图书关系
     *
     * @param id 数据包id
     * @return
     */
    boolean deleteDataPackageBookByDataPackageId(long id);

    /**
     * 根据数据包id批量删除数据包图书关系
     *
     * @param list 数据包对象列表
     * @return
     */
    boolean batchDeleteDataPackageBookByDataPackageId(List<DataPackageBean> list);

    /**
     * 从数据包里面批量删除图书
     *
     * @param param 数据包和图书id关系参数
     * @return
     */
    boolean batchDeleteFromDataPackage(Map<String, Object> param);

    /**
     * 根据数据包id查询图书列表
     *
     * @param dataPackageId 数据包id
     * @return
     */
    List<BookVO> getMetadataListByPackageId(long dataPackageId);


    /**
     * 按条件查询所有图书信息列表
     *
     * @param
     * @return
     */
    List<BookVO> queryMetadataList(Map<String, Object> param);

    /**
     * 根据条件查询图书列表
     *
     * @param param 查询条件
     * @return
     */
    List<BookVO> getMetadataListForPackage(Map<String, Object> param);

    /**
     * 根据数据包id查询图书数量
     *
     * @param id 数据包id
     * @return
     */
    int getBookNumber(long id);

    /**
     * 查询数据包和客户单位的关系
     *
     * @param packageList
     * @return
     */
    List<BusinessPackageBean> getPackageBusinessRelation(List<DataPackageBean> packageList);

    /**
     * 根据一级数据包id查询图书数量（有二级分类，去重之后）
     *
     * @param id 数据包id
     * @return
     */
    int getBookNumberByA(long id);

    /**
     * 根据一级数据包id查询图书数量(无二级分类，统计一级图书数量)
     *
     * @param id 数据包id
     * @return
     */
    int getBookNumberByFirst(long id);
}

