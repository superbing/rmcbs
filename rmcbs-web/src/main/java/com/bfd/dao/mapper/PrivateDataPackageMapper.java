package com.bfd.dao.mapper;

import com.bfd.bean.PrivateDataPackageBean;
import com.bfd.param.vo.BookVO;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * 个性化数据包Dao层
 *
 * @author jiang.liu
 * @date 2018-08-03
 */
@Repository
public interface PrivateDataPackageMapper {

    /**
     * 获取个性化数据包列表
     * @param  businessId 单位id
     * @return
     */
    List<PrivateDataPackageBean> getPrivatePackageList(long businessId);

    /**
     * 根据id获取下一级个性化数据包列表
     *
     * @param id 个性化数据包id
     * @return
     */
    List<PrivateDataPackageBean> getPrivateDataPackageListByParentId(long id);

    /**
     * 根据id获取个性化数据包信息
     *
     * @param id 个性化数据包id
     * @return
     */
    PrivateDataPackageBean getPrivatePackageInfoById(long id);

    /**
     * 新增个性化数据包信息
     *
     * @param privateDataPackageBean 个性化数据包对象
     * @return
     */
    int insert(PrivateDataPackageBean privateDataPackageBean);

    /**
     * 更新个性化数据包信息
     *
     * @param privateDataPackageBean 个性化数据包对象
     * @return
     */
    int update(PrivateDataPackageBean privateDataPackageBean);

    /**
     * 删除个性化数据包信息
     *
     * @param id 个性化数据包id
     * @return
     */
    boolean delete(long id);

    /**
     * 批量删除个性化数据包信息
     *
     * @param list 个性化数据包对象列表
     * @return
     */
    boolean batchDeletePrivateDataPackage(List<PrivateDataPackageBean> list);

    /**
     * 向个性化数据包添加图书
     *
     * @param param 个性化数据包和图书id关系参数
     * @return
     */
    boolean addBookForPrivateDataPackage(Map<String, Object> param);

    /**
     * 根据个性化数据包id删除个性化数据包图书关系
     *
     * @param id 个性化数据包id
     * @return
     */
    boolean deleteRelationByDataPackageId(long id);

    /**
     * 根据个性化数据包id批量删除个性化数据包图书关系
     *
     * @param list 个性化数据包对象列表
     * @return
     */
    boolean batchDeleteRelationByPrivateDataPackageId(List<PrivateDataPackageBean> list);

    /**
     * 从个性化数据包里面批量删除图书
     *
     * @param param 个性化数据包和图书id关系参数
     * @return
     */
    boolean batchDeleteFromPrivateDataPackage(Map<String, Object> param);

    /**
     * 根据个性化数据包id查询图书列表
     *
     * @param privateDataPackageId 个性化数据包id
     * @return
     */
    List<BookVO> getMetadataListByPrivatePackageId(long privateDataPackageId);

    /**
     * 根据条件查询图书列表
     *
     * @param param 查询条件
     * @return
     */
    List<BookVO> getMetadataListForPrivatePackage(Map<String, Object> param);

    /**
     * 按条件查询所有图书信息列表
     *
     * @param
     * @return
     */
    List<BookVO> queryMetadataList(Map<String, Object> param);

    /**
     * 根据个性化数据包id查询图书数量
     *
     * @param id 个性化数据包id
     * @return
     */
    int getBookNumber(long id);

    /**
     * 根据一级个性化数据包id查询图书数量（去重之后）
     *
     * @param id 数据包id
     * @return
     */
    int getBookNumberByA(long id);

    /**
     * 根据一级个性化数据包id查询图书数量(无二级分类，统计一级图书数量)
     *
     * @param id 个性化数据包id
     * @return
     */
    int getBookNumberByFirst(long id);

}
