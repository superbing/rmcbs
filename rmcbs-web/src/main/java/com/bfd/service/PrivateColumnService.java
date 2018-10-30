package com.bfd.service;

import com.bfd.bean.PrivateColumnBean;
import com.bfd.bean.PrivateTemplateBean;
import com.bfd.common.vo.PageVO;
import com.bfd.param.vo.BookVO;
import com.bfd.param.vo.ResourceBookQueryVO;
import com.bfd.param.vo.ResourceQueryVO;

import java.util.List;
import java.util.Map;

/**
 * 个性化栏目service服务层
 *
 * @author jiang.liu
 * @date  2018-08-03
 */
public interface PrivateColumnService {

    /**
     * 获取某个单位个性化栏目分级树结构
     *
     * @param businessId 单位id
     * @return
     */
    List<PrivateColumnBean> getPrivateColumnList(long businessId);

    /**
     * 添加个性化栏目信息
     *
     * @param privateColumnBean  个性化栏目对象
     * @return
     */
    long insert(PrivateColumnBean privateColumnBean);

    /**
     * 更新个性化栏目信息
     *
     * @param privateColumnBean  个性化栏目对象
     * @return
     */
    long update(PrivateColumnBean privateColumnBean);

    /**
     * 删除个性化栏目信息
     *
     * @param id  个性化栏目id
     * @return
     */
    boolean delete(long id);

    /**
     * 调整个性化栏目顺序
     *
     * @param firstId  移动的栏目id
     * @param secondId 被交换位置的栏目id
     * @return
     */
    boolean movePrivateColumnSort(long firstId, long secondId);

    /**
     * 向个性化栏目添加图书
     *
     * @param privateColumnId 个性化栏目id
     * @param metadataIds     元数据id集合
     * @return
     */
    boolean addBookMetadata(long privateColumnId, List<Long> metadataIds);

    /**
     * 从个性化栏目里面批量删除图书
     * @param privateColumnId    个性化栏目id
     * @param metadataIds 元数据id集合
     * @return
     */
    boolean batchDeleteFromPrivateColumn(long privateColumnId, List<Long> metadataIds);

    /**
     * 根据个性化栏目id查询图书列表
     *
     * @param privateColumnId 个性化栏目id
     * @return
     */
    List<BookVO> getMetadataListByPrivateColumnId(long privateColumnId);

//    /**
//     * 在个性化栏目中调整图书顺序（上移、下移）
//     *
//     * @param privateColumnId     个性化栏目id
//     * @param firstBookId  移动的图书id
//     * @param secondBookId 被交换位置的图书id
//     * @return
//     */
//    boolean moveBookSortForPrivateColumn(long privateColumnId,long firstBookId,long secondBookId);
//
//    /**
//     * 个性化栏目中置顶图书
//     *
//     * @param privateColumnId    个性化栏目id
//     * @param metadataId  置顶图书id
//     * @return
//     */
//    boolean setTopForPrivateColumnBook(long privateColumnId,long metadataId);
//
//    /**
//     * 个性化栏目中置底图书
//     *
//     * @param privateColumnId    个性化栏目id
//     * @param metadataId  置底图书id
//     * @return
//     */
//    boolean setDownForPrivateColumnBook(long privateColumnId,long metadataId);

    /**
     * 为某一个性化栏目中的图书排序
     *
     * @param privateColumnId    个性化栏目id
     * @param metadataId  排序图书id
     * @param sort        排序位置
     * @return
     */
    boolean setBookSort(long privateColumnId, long metadataId, int sort);

    /**
     * 根据条件查询图书列表
     *
     * @param resourceQueryVO 参数对象
     * @return
     */
    List<BookVO> getMetadataListForPrivateColumn(ResourceQueryVO resourceQueryVO);

    /**
     * 按条件查询所有图书信息列表（包含已加入个性化栏目的数据）
     *
     * @param resourceBookQueryVO 参数
     * @return
     */
    PageVO<BookVO> queryMetadataList(ResourceBookQueryVO resourceBookQueryVO);

    /**
     * 设置个性化皮肤模板
     *
     * @param skinColor  颜色
     * @param companyId  单位id
     * @return
     */
    boolean setSkinColor(String skinColor, long companyId);

    /**
     * 获取个性化皮肤模板
     *
     * @return
     */
    PrivateTemplateBean getSkinColor(long companyId);

}
