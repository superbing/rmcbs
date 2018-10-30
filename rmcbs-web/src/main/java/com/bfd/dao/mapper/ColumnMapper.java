package com.bfd.dao.mapper;

import com.bfd.bean.BusinessColumnBean;
import com.bfd.bean.ColumnBean;
import com.bfd.bean.PublicTemplateBean;
import com.bfd.param.vo.BookVO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 栏目 mapper
 *
 * @author jiang.liu
 * @date 2018-07-27
 */
@Repository
public interface ColumnMapper {
    
    /**
     * 获取栏目列表
     * 
     * @return
     */
    List<ColumnBean> getColumnList();

    /**
     * 获取所有的一级栏目ID集合
     * @return
     */
    List<Long> getColumnOneLevel();

    /**
     * 获取栏目总数
     * 
     * @return
     */
    Long getColumnTotal();
    
    /**
     * 根据id获取下一级栏目列表
     * 
     * @param id 栏目id
     * @return
     */
    List<ColumnBean> getColumnListByParentId(long id);
    
    /**
     * 根据id获取栏目信息
     * 
     * @param id 栏目id
     * @return
     */
    ColumnBean getColumnInfoById(long id);
    
    /**
     * 新增栏目信息
     * 
     * @param columnBean 栏目对象
     * @return
     */
    int insert(ColumnBean columnBean);
    
    /**
     * 更新栏目信息
     * 
     * @param columnBean 栏目对象
     * @return
     */
    int update(ColumnBean columnBean);
    
    /**
     * 删除栏目信息
     * 
     * @param id 栏目id
     * @return
     */
    boolean delete(long id);
    
    /**
     * 批量删除栏目信息
     * 
     * @param list 栏目对象列表
     * @return
     */
    boolean batchDeleteColumn(List<ColumnBean> list);
    
    /**
     * 向栏目添加图书
     * 
     * @param param 栏目和图书id关系参数
     * @return
     */
    boolean addBookMetadata(Map<String, Object> param);
    
    /**
     * 根据栏目id删除栏目图书关系
     * 
     * @param id 栏目id
     * @return
     */
    boolean deleteColumnBookByColumnId(long id);
    
    /**
     * 根据栏目id批量删除栏目图书关系
     * 
     * @param list 栏目对象列表
     * @return
     */
    boolean batchDeleteColumnBookByColumnId(List<ColumnBean> list);
    
    /**
     * 根据parentId将已存在的栏目sort排序字段向后移动1个位置
     * 
     * @param parentId 分类父id
     * @return
     */
    int updateSortAddByParentId(long parentId);
    
    /**
     * 根据columnId和metadataId将已存在的图书关系sort排序字段向后移动n位
     * 
     * @param param 参数(long columnId,int n)
     * @return
     */
    int updateRelationSortAddByColumnId(Map<String, Object> param);
    
    /**
     * 从栏目里面批量删除图书
     * 
     * @param param 栏目和图书id关系参数
     * @return
     */
    boolean batchDeleteFromColumn(Map<String, Object> param);
    
    /**
     * 根据栏目id查询图书列表
     *
     * @param columnId 栏目id
     * @return
     */
    List<BookVO> getMetadataListByColumnId(long columnId);

    /**
     * 获取栏目图书关系对象集合
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> getColumnBookRelationList(Map<String, Object> param);

    /**
     * 更新栏目数据关系表的书籍排序sort
     *
     * @param param
     * @return
     */
    int updateColumnBookRelationSort(Map<String, Object> param);
    
    /**
     * 获取栏目图书关系对象
     *
     * @param param
     * @return
     */
    Map<String, Object> getColumnBookRelationBean(Map<String, Object> param);
    
    /**
     * 根据栏目id和图书id查询在该图书排序之前的所有关系id（同一个栏目下）
     *
     * @param param
     * @return
     */
    List<Long> getBeforeRelationIdsByColumnIdAndMetadataId(Map<String, Object> param);
    
    /**
     * 根据栏目id和图书id查询在该图书排序之后的所有关系id（同一个栏目下）
     *
     * @param param
     * @return
     */
    List<Long> getAfterRelationIdsByColumnIdAndMetadataId(Map<String, Object> param);
    
    /**
     * 通过置顶图书的id将之前的所有sort全部向后移动1个位置
     * 
     * @param param
     * @return
     */
    int updateRelationSortAddByIds(Map<String, Object> param);
    
    /**
     * 通过置顶图书的id将之前的所有sort全部向前移动1个位置
     * 
     * @param param
     * @return
     */
    int updateRelationSortReduceByIds(Map<String, Object> param);
    
    /**
     * 获取栏目图书关系表某一栏目下最大的排序字段sort值
     * 
     * @param columnId 栏目id
     * @return
     */
    int getMaxSortByColumnId(long columnId);
    
    /**
     * 根据条件查询图书列表
     *
     * @param param 查询条件
     * @return
     */
    List<BookVO> getMetadataListForColumn(Map<String, Object> param);
    
    /**
     * 查询皮肤模板
     *
     * @param type 客户端类型
     * @return
     */
    PublicTemplateBean getSkinTemplateByType(String type);
    
    /**
     * 新增皮肤模板
     *
     * @param param 查询条件
     * @return
     */
    int addSkinTemplate(Map<String, Object> param);
    
    /**
     * 修改皮肤模板
     *
     * @param param 查询条件
     * @return
     */
    int updateSkinTemplate(Map<String, Object> param);
    
    /**
     * 根据栏目id查询图书数量
     *
     * @param id 栏目id
     * @return
     */
    int getBookNumber(long id);

    /**
     * 查询栏目和客户单位的关系
     *
     * @param columnList
     * @return
     */
    List<BusinessColumnBean> getColumnBusinessRelation(List<ColumnBean> columnList);

    /**
     * 根据一级栏目id查询图书数量（去重之后）
     *
     * @param id 栏目id
     * @return
     */
    int getBookNumberByA(long id);

    /**
     * 根据一级栏目id查询图书数量(无二级分类，统计一级图书数量)
     *
     * @param id 栏目id
     * @return
     */
    int getBookNumberByFirst(long id);

    /**
     * 根据sort1，sort2 获取两个栏目对象之间的栏目list
     *
     * @param param 查询条件
     * @return
     */
    List<ColumnBean> getColumnListByRang(Map<String, Object> param);

    /**
     * 根据sort获取该栏目对象之后的所有栏目list
     *
     * @param param 查询条件
     * @return
     */
    List<ColumnBean> getColumnListByAfter(Map<String, Object> param);
}
