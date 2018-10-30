package com.bfd.dao.mapper;

import com.bfd.bean.PrivateColumnBean;
import com.bfd.bean.PrivateTemplateBean;
import com.bfd.param.vo.BookVO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 个性化栏目Dao层
 *
 * @author jiang.liu
 * @date 2018-08-03
 */
@Repository
public interface PrivateColumnMapper {

    /**
     * 获取个性化栏目列表
     *
     * @param businessId 单位id
     * @return
     */
    List<PrivateColumnBean> getPrivateColumnList(long businessId);

    /**
     * 根据id获取下一级个性化栏目列表
     *
     * @param id  个性化栏目id
     * @return
     */
    List<PrivateColumnBean> getPrivateColumnListByParentId(long id);

    /**
     * 根据id获取个性化栏目信息
     * @param id  个性化栏目id
     * @return
     */
    PrivateColumnBean getPrivateColumnInfoById(long id);

    /**
     * 新增个性化栏目信息
     * @param privateColumnBean  个性化栏目对象
     * @return
     */
    int insert(PrivateColumnBean privateColumnBean);

    /**
     * 更新个性化栏目信息
     * @param privateColumnBean  个性化栏目对象
     * @return
     */
    int update(PrivateColumnBean privateColumnBean);

    /**
     * 删除个性化栏目信息
     * @param id  个性化栏目id
     * @return
     */
    boolean delete(long id);

    /**
     * 批量删除个性化栏目信息
     * @param list 个性化栏目对象列表
     * @return
     */
    boolean batchDeletePrivateColumn(List<PrivateColumnBean> list);

    /**
     * 向个性化栏目添加图书
     * @param param  个性化栏目和图书id关系参数
     * @return
     */
    boolean addBookMetadata(Map<String,Object> param);

    /**
     * 根据个性化栏目id删除栏目图书关系
     * @param id  个性化栏目id
     * @return
     */
    boolean deletePrivateColumnBookByColumnId(long id);

    /**
     * 根据个性化栏目id批量删除栏目图书关系
     * @param list 个性化栏目对象列表
     * @return
     */
    boolean batchDeletePrivateColumnBookByColumnId(List<PrivateColumnBean> list);

    /**
     * 根据parentId将已存在的个性化栏目sort排序字段向后移动1个位置
     * @param parentId 分类父id
     * @return
     */
    int updateSortAddByParentId(long parentId);

    /**
     * 根据columnId和metadataId将已存在的图书关系sort排序字段向后移动n位
     * @param param  参数(long columnId,int n)
     * @return
     */
    int updateRelationSortAddByColumnId(Map<String,Object> param);

    /**
     * 从个性化栏目里面批量删除图书
     * @param param  个性化栏目和图书id关系参数
     * @return
     */
    boolean batchDeleteFromPrivateColumn(Map<String,Object> param);

    /**
     * 根据个性化栏目id查询图书列表
     *
     * @param privateColumnId 个性化栏目id
     * @return
     */
    List<BookVO> getMetadataListByPrivateColumnId(long privateColumnId);

    /**
     * 更新个性化栏目数据关系表的书籍排序sort
     *
     * @param param
     * @return
     */
    int updatePrivateColumnBookRelationSort(Map<String, Object> param);

    /**
     * 获取个性化栏目图书关系对象
     *
     * @param param
     * @return
     */
    Map<String,Object> getPrivateColumnBookRelationBean(Map<String, Object> param);

    /**
     * 获取个性化栏目图书关系对象
     *
     * @param param
     * @return
     */
    List<Map<String,Object>> getPrivateColumnBookRelationList(Map<String, Object> param);

    /**
     * 根据栏目id和图书id查询在该图书排序之前的所有关系id（同一个栏目下）
     *
     * @param param
     * @return
     */
    List<Long> getBeforeRelationIdsByPrivateColumnIdAndMetadataId(Map<String, Object> param);

    /**
     * 根据栏目id和图书id查询在该图书排序之后的所有关系id（同一个栏目下）
     *
     * @param param
     * @return
     */
    List<Long> getAfterRelationIdsByPrivateColumnIdAndMetadataId(Map<String, Object> param);

    /**
     * 通过置顶图书的id将之前的所有sort全部向后移动1个位置
     * @param param
     * @return
     */
    int updateRelationSortAddByIds(Map<String,Object> param);

    /**
     * 通过置顶图书的id将之前的所有sort全部向前移动1个位置
     * @param param
     * @return
     */
    int updateRelationSortReduceByIds(Map<String,Object> param);

    /**
     * 获取个性化栏目图书关系表某一栏目下最大的排序字段sort值
     * @param privatecolumnId 栏目id
     * @return
     */
    int getMaxSortByPrivateColumnId(long privatecolumnId);

    /**
     * 根据条件查询图书列表
     *
     * @param param 查询条件
     * @return
     */
    List<BookVO> getMetadataListForPrivateColumn(Map<String, Object> param);

    /**
     * 查询单位皮肤颜色
     *
     * @param param 查询条件
     * @return
     */
    PrivateTemplateBean getSkinColorInfo(Map<String, Object> param);

    /**
     * 新增单位皮肤颜色
     *
     * @param param 查询条件
     * @return
     */
    int addSkinColor(Map<String, Object> param);

    /**
     * 修改单位皮肤颜色
     *
     * @param param 查询条件
     * @return
     */
    int updateSkinColor(Map<String, Object> param);

    /**
     * 根据个性化栏目id查询图书数量
     *
     * @param id 个性化栏目id
     * @return
     */
    int getBookNumber(long id);

    /**
     * 根据一级个性化栏目id查询图书数量（去重之后）
     *
     * @param id 栏目id
     * @return
     */
    int getBookNumberByA(long id);

    /**
     * 根据一级个性化栏目id查询图书数量(无二级分类，统计一级图书数量)
     *
     * @param id 个性化栏目id
     * @return
     */
    int getBookNumberByFirst(long id);

    /**
     * 根据sort1，sort2 获取两个栏目对象之间的栏目list
     *
     * @param param 查询条件
     * @return
     */
    List<PrivateColumnBean> getColumnListByRang(Map<String, Object> param);

    /**
     * 根据sort获取该栏目对象之后的所有栏目list
     *
     * @param param 查询条件
     * @return
     */
    List<PrivateColumnBean> getColumnListByAfter(Map<String, Object> param);
}
