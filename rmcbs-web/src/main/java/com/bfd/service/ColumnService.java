package com.bfd.service;

import com.bfd.bean.ColumnBean;
import com.bfd.common.vo.PageVO;
import com.bfd.param.vo.BookVO;
import com.bfd.param.vo.ResourceBookQueryVO;
import com.bfd.param.vo.ResourceQueryVO;

import java.util.List;
import java.util.Map;

/**
 * 栏目service服务层
 *
 * @author jiang.liu
 * @date  2018-07-27
 */
public interface ColumnService {

    /**
     * 获取栏目分级树结构
     * @return
     */
    List<ColumnBean> getColumnList();

    /**
     * 根据id获取下一级栏目列表
     * @param id  栏目id
     * @return
     */
    List<ColumnBean> getPackageListByParentId(long id);

    /**
     * 添加栏目信息
     * @param columnBean  栏目对象
     * @return
     */
    long insert(ColumnBean columnBean);

    /**
     * 更新栏目信息
     * @param columnBean  栏目对象
     * @return
     */
    long update(ColumnBean columnBean);

    /**
     * 删除栏目信息
     * @param id  栏目id
     * @return
     */
    boolean delete(long id);

    /**
     * 调整栏目顺序
     *
     * @param firstId  移动的栏目id
     * @param secondId 被交换位置的栏目id
     * @return
     */
    boolean moveColumnSort(long firstId, long secondId);

    /**
     * 向栏目添加图书
     * @param columnId    栏目id
     * @param metadataIds 元数据id集合
     * @return
     */
    boolean addBookMetadata(long columnId, List<Long> metadataIds);

    /**
     * 从栏目里面批量删除图书
     * @param columnId    栏目id
     * @param metadataIds 元数据id集合
     * @return
     */
    boolean batchDeleteFromColumn(long columnId, List<Long> metadataIds);

    /**
     * 根据栏目id查询图书列表
     *
     * @param columnId 栏目id
     * @return
     */
    List<BookVO> getMetadataListByColumnId(long columnId);

//    /**
//     * 调整图书顺序（上移、下移）
//     *
//     * @param columnId     栏目id
//     * @param firstBookId  移动的图书id
//     * @param secondBookId 被交换位置的图书id
//     * @return
//     */
//    boolean moveBookSortForColumn(long columnId,long firstBookId,long secondBookId);
//
//    /**
//     * 栏目中的图书置顶
//     *
//     * @param columnId    栏目id
//     * @param metadataId  置顶图书id
//     * @return
//     */
//    boolean setTopForColumnBook(long columnId,long metadataId);
//
//    /**
//     * 栏目中的图书置底
//     *
//     * @param columnId    栏目id
//     * @param metadataId  置底的图书id
//     * @return
//     */
//    boolean setDownForColumnBook(long columnId,long metadataId);

    /**
     * 为某一栏目中的图书排序
     *
     * @param columnId    栏目id
     * @param metadataId  排序图书id
     * @param sort        排序图书id
     * @return
     */
    boolean setBookSort(long columnId, long metadataId, int sort);

    /**
     * 根据条件查询图书列表
     *
     * @param resourceQueryVO 参数对象
     * @return
     */
    List<BookVO> getMetadataListForColumn(ResourceQueryVO resourceQueryVO);

    /**
     * 按条件查询所有图书信息列表（包含已加入栏目的数据）
     *
     * @param resourceBookQueryVO 参数
     * @return
     */
    PageVO<BookVO> queryMetadataList(ResourceBookQueryVO resourceBookQueryVO);

    /**
     * 设置皮肤模板
     *
     * @param templateId  模板id
     * @param type  类型(仅包含PC或APP)
     * @return
     */
    boolean setSkinTemplate(int templateId, String type);

    /**
     * 获取皮肤模板
     *
     * @return
     */
    Map<String,Object> getSkinTemplate();
}
