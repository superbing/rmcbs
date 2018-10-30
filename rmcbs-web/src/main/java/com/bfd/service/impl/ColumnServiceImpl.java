package com.bfd.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.bfd.domain.User;
import com.bfd.utils.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.bfd.bean.BusinessColumnBean;
import com.bfd.bean.ColumnBean;
import com.bfd.bean.PublicTemplateBean;
import com.bfd.common.exception.RmcbsException;
import com.bfd.common.vo.Constants;
import com.bfd.common.vo.PageVO;
import com.bfd.dao.mapper.ColumnMapper;
import com.bfd.dao.mapper.DataPackageMapper;
import com.bfd.enums.EpubStatusEnum;
import com.bfd.enums.PdfStatusEnum;
import com.bfd.param.vo.BookVO;
import com.bfd.param.vo.ResourceBookQueryVO;
import com.bfd.param.vo.ResourceQueryVO;
import com.bfd.service.ColumnService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 栏目serviceImpl
 *
 * @author jiang.liu
 * @date 2018-07-27
 */

@Service
public class ColumnServiceImpl implements ColumnService {

    @Autowired
    ColumnMapper columnMapper;

    @Autowired
    DataPackageMapper dataPackageMapper;

    @Override
    public List<ColumnBean> getColumnList() {
        List<ColumnBean> list = columnMapper.getColumnList();
        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getParentId() == 0) {

                // 分两种情况，一级下有书（直接统计图书数量），一级下有分类（统计所有分类下图书数量）
                List<ColumnBean> chirdList = columnMapper.getColumnListByParentId(list.get(i).getId());
                if(chirdList.size() == 0){
                    list.get(i).setBookNumber(columnMapper.getBookNumberByFirst(list.get(i).getId()));
                }else{
                    list.get(i).setBookNumber(columnMapper.getBookNumberByA(list.get(i).getId()));
                }

            } else {
                list.get(i).setBookNumber(columnMapper.getBookNumber(list.get(i).getId()));
            }
        }
        return list;
    }

    @Override
    public List<ColumnBean> getPackageListByParentId(long id) {
        List<ColumnBean> list = columnMapper.getColumnListByParentId(id);
        return list;
    }

    @Override
    public long insert(ColumnBean columnBean) {
        Long userId = SpringSecurityUtil.getCurrentUserId();
        columnBean.setCreateUser(userId);
        columnBean.setUpdateUser(userId);
        // 为新增的栏目设置排序字段为0,设置为排序第一位
        columnBean.setSort(0);
        columnMapper.updateSortAddByParentId(columnBean.getParentId());
        columnMapper.insert(columnBean);
        long id = columnBean.getId();
        return id;
    }

    @Override
    public long update(ColumnBean columnBean) {
        Long userId = SpringSecurityUtil.getCurrentUserId();
        columnBean.setUpdateUser(userId);
        columnMapper.update(columnBean);
        return columnBean.getId();
    }

    @Override
    public boolean delete(long id) {

        // 判断需要删除的栏目是一级或者二级，如果是一级,需要删除该一级、一级的所有子节、子节点和图书关系
        // 如果是二级,需要删除该二级、二级和图书关系
        ColumnBean columnBean = columnMapper.getColumnInfoById(id);
        if (columnBean.getParentId() == 0) {

            // 根据一级栏目id查询所有子节点（二级列表）
            List<ColumnBean> beanList = columnMapper.getColumnListByParentId(id);
            if (!beanList.isEmpty()) {

                // 如果栏目和客户单位有关联，则不能进行删除
                List<BusinessColumnBean> relationList = columnMapper.getColumnBusinessRelation(beanList);
                if (!CollectionUtils.isEmpty(relationList)) {
                    throw new RmcbsException("此栏目的所有子栏目关联了" + relationList.size() + "个客户单位，请先解除关联！");
                }

                // 根据栏目id删除栏目与图书关系表映射
                columnMapper.batchDeleteColumnBookByColumnId(beanList);

                // 根据二级栏目对象列表批量删除所有二级栏目
                columnMapper.batchDeleteColumn(beanList);
            }else{

                // 如果栏目是一级，并且栏目没有子节点，则判断该一级栏目和客户单位有无关联，
                List<ColumnBean> firstBeanList = new ArrayList<>();
                firstBeanList.add(columnBean);
                List<BusinessColumnBean> relationList = columnMapper.getColumnBusinessRelation(firstBeanList);
                if (!CollectionUtils.isEmpty(relationList)) {
                    throw new RmcbsException("此栏目关联了" + relationList.size() + "个客户单位，请先解除关联！");
                }

                // 根据栏目id删除栏目与图书关系表映射
                columnMapper.batchDeleteColumnBookByColumnId(firstBeanList);
            }
        } else {
            List<ColumnBean> beanList = new ArrayList<ColumnBean>();
            beanList.add(columnBean);
            List<BusinessColumnBean> relationList = columnMapper.getColumnBusinessRelation(beanList);
            if (!CollectionUtils.isEmpty(relationList)) {
                throw new RmcbsException("此栏目关联了" + relationList.size() + "个客户单位，请解除关联！");
            }

            // 根据栏目id删除栏目与图书关系表映射
            columnMapper.deleteColumnBookByColumnId(id);
        }
        return columnMapper.delete(id);
    }

    @Override
    public boolean addBookMetadata(long columnId, List<Long> metadataIds) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("columnId", columnId);
//        param.put("n", metadataIds.size());
//        columnMapper.updateRelationSortAddByColumnId(param);
        for (int i = 0; i < metadataIds.size(); i++) {
            param.put("metadataId", metadataIds.get(i));
            param.put("sort", -1);
            columnMapper.addBookMetadata(param);
        }
        return true;
    }

//    @Override
//    public boolean moveColumnSort(long firstId, long secondId) {
//        ColumnBean firstColumn = columnMapper.getColumnInfoById(firstId);
//        int firstSort = firstColumn.getSort();
//        ColumnBean secondColumn = columnMapper.getColumnInfoById(secondId);
//        int secondSort = secondColumn.getSort();
//        firstColumn.setSort(secondSort);
//        secondColumn.setSort(firstSort);
//        columnMapper.update(firstColumn);
//        columnMapper.update(secondColumn);
//        return true;
//    }

    @Override
    public boolean moveColumnSort(long firstId, long secondId) {

        // 目标对象2
        ColumnBean firstColumn = columnMapper.getColumnInfoById(firstId);
        // 目标sort=2
        int firstSort = firstColumn.getSort();
        // 需要移动的对象7
        ColumnBean secondColumn = columnMapper.getColumnInfoById(secondId);
        // 需要移动对象的sort=7
        int secondSort = secondColumn.getSort();
        if(firstSort < secondSort){
            // 找到2--7之间的对象list
            Map<String, Object> param = new HashMap<>(16);
            param.put("id", firstColumn.getParentId());
            param.put("firstSort", firstSort);
            param.put("secondSort", secondSort);
            List<ColumnBean> columnList = columnMapper.getColumnListByRang(param);

            // 7的sort更新为2的sort
            secondColumn.setSort(firstSort);

            // 2--7之间的每个对象的sort+1
            for (int i = 0; i < columnList.size(); i++) {
                columnList.get(i).setSort(columnList.get(i).getSort()+1);
                columnMapper.update(columnList.get(i));
            }
            columnMapper.update(secondColumn);
        }else{
            // 找到7后面的对象list
            Map<String, Object> param = new HashMap<>(16);
            param.put("id", firstColumn.getParentId());
            param.put("firstSort", firstSort);
            List<ColumnBean> columnList = columnMapper.getColumnListByAfter(param);

            // 7的sort更新为9的sort
            secondColumn.setSort(firstSort);

            // 7之后的每个对象的sort+1
            for (int i = 0; i < columnList.size(); i++) {
                columnList.get(i).setSort(columnList.get(i).getSort()+1);
                columnMapper.update(columnList.get(i));
            }
            columnMapper.update(secondColumn);
        }
        return true;
    }

    @Override
    public boolean batchDeleteFromColumn(long columnId, List<Long> metadataIds) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("columnId", columnId);
        param.put("metadataIds", metadataIds);
        columnMapper.batchDeleteFromColumn(param);
        return true;
    }

    @Override
    public List<BookVO> getMetadataListByColumnId(long columnId) {
        return columnMapper.getMetadataListByColumnId(columnId);
    }

    @Override
    public PageVO<BookVO> queryMetadataList(ResourceBookQueryVO vo) {
        if (vo.getSortField() == null || vo.getSortField().equals("")) {
            vo.setSortField("create_time");
        }
        if (vo.getSortType() == null || vo.getSortType().equals("")) {
            vo.setSortType("desc");
        }
        PageHelper.startPage(vo.getCurrent(), vo.getPageSize());
        Map<String, Object> param = new HashMap<>(16);
        if (vo.getStartTime() != null && !vo.getStartTime().equals("") && vo.getEndTime() != null && !vo.getEndTime().equals("")) {
            param.put("startTime", vo.getStartTime());
            param.put("endTime", vo.getEndTime());
        }
        if (vo.getBookStatus() != 0) {
            switch (vo.getBookStatus()) {
                case 1:
                    param.put("bookPdf", PdfStatusEnum.UPLOAD.getKey());
                    break;
                case 2:
                    param.put("bookEpub", EpubStatusEnum.ENCRYPTED.getKey());
                    break;
                case 3:
                    param.put("bookXml", 1);
                    break;
                default:
                    param.put("bookStatus", 1);
            }
        }
        if (vo.getBookName() != null && !vo.getBookName().equals("")) {
            param.put("bookName", vo.getBookName());
        }
        if (vo.getUniqueId() != null && !vo.getUniqueId().equals("")) {
            param.put("uniqueId", vo.getUniqueId());
        }
        if(vo.getBookIsbn() != null && !vo.getBookIsbn().equals("")){
            param.put("bookIsbn", vo.getBookIsbn());
        }
        if (vo.getSortField() != null && !vo.getSortField().equals("")) {
            param.put("sortField", vo.getSortField());
        }
        if (vo.getSortType() != null && !vo.getSortType().equals("")) {
            param.put("sortType", vo.getSortType());
        }
        List<BookVO> list = dataPackageMapper.queryMetadataList(param);
        PageInfo<BookVO> pageInfo = new PageInfo<>(list);
        List<BookVO> listOfColumn = columnMapper.getMetadataListByColumnId(vo.getCid());
        for (int i = 0; i < pageInfo.getList().size(); i++) {
            if (listOfColumn.contains(pageInfo.getList().get(i))) {
                pageInfo.getList().get(i).setChecked(true);
            }
            StringBuffer buff = new StringBuffer();
            buff.append(" ");
            if (pageInfo.getList().get(i).getBookPdf().equals(PdfStatusEnum.UPLOAD.getKey())) {
                buff.append("PDF ");
            }
            if (pageInfo.getList().get(i).getBookEpub().equals(EpubStatusEnum.ENCRYPTED.getKey())) {
                buff.append("EPUB ");
            }
            if (pageInfo.getList().get(i).getBookXml().equals("1")) {
                buff.append("XML ");
            }
            pageInfo.getList().get(i).setBookType(buff.toString());
        }
        return new PageVO<>(vo.getCurrent(), vo.getPageSize(), pageInfo.getTotal(), pageInfo.getList());
    }

//    @Override
//    public boolean moveBookSortForColumn(long columnId, long firstBookId, long secondBookId) {
//
//        Map<String, Object> param = new HashMap<>(16);
//        param.put("columnId", columnId);
//        param.put("metadataId", firstBookId);
//        Map<String, Object> relationBeanOne = columnMapper.getColumnBookRelationBean(param);
//        int firstSort = (int)relationBeanOne.get("sort");
//        param.put("metadataId", secondBookId);
//        Map<String, Object> relationBeanTwo = columnMapper.getColumnBookRelationBean(param);
//        int secondSort = (int)relationBeanTwo.get("sort");
//        param.put("metadataId", firstBookId);
//        param.put("sort", secondSort);
//        columnMapper.updateColumnBookRelationSort(param);
//        param.put("metadataId", secondBookId);
//        param.put("sort", firstSort);
//        columnMapper.updateColumnBookRelationSort(param);
//        return true;
//    }
//
//    @Override
//    public boolean setTopForColumnBook(long columnId, long metadataId) {
//
//        Map<String, Object> param = new HashMap<>(16);
//        param.put("columnId", columnId);
//        param.put("metadataId", metadataId);
//        List<Long> ids = columnMapper.getBeforeRelationIdsByColumnIdAndMetadataId(param);
//        if (ids.size() > 0) {
//            param.put("ids", ids);
//            columnMapper.updateRelationSortAddByIds(param);
//        }
//        param.put("sort", 0);
//        columnMapper.updateColumnBookRelationSort(param);
//        return true;
//    }
//
//    @Override
//    public boolean setDownForColumnBook(long columnId, long metadataId) {
//
//        Map<String, Object> param = new HashMap<>(16);
//        param.put("columnId", columnId);
//        param.put("metadataId", metadataId);
//        List<Long> ids = columnMapper.getAfterRelationIdsByColumnIdAndMetadataId(param);
//        int maxSort = columnMapper.getMaxSortByColumnId(columnId);
//        if (ids.size() > 0) {
//            param.put("ids", ids);
//            columnMapper.updateRelationSortReduceByIds(param);
//        }
//        param.put("sort", maxSort);
//        columnMapper.updateColumnBookRelationSort(param);
//        return true;
//    }

    @Override
    public boolean setBookSort(long columnId, long metadataId, int sort) {

        Map<String, Object> param = new HashMap<>(16);
        param.put("columnId", columnId);
        param.put("metadataId", metadataId);
        param.put("sort", sort);
        columnMapper.updateColumnBookRelationSort(param);
        return true;
    }

    @Override
    public List<BookVO> getMetadataListForColumn(ResourceQueryVO resourceQueryVO) {
        if (resourceQueryVO.getSortField() == null || resourceQueryVO.getSortField().equals("")) {
            resourceQueryVO.setSortField("create_time");
        }
        if (resourceQueryVO.getSortType() == null || resourceQueryVO.getSortType().equals("")) {
            resourceQueryVO.setSortType("desc");
        }
        ColumnBean columnBean = columnMapper.getColumnInfoById(resourceQueryVO.getCid());
        List<Long> idList = new ArrayList<>();
        int flag = 20; // 所有2级图书
        if (columnBean.getParentId() == 0) {
            List<ColumnBean> list = columnMapper.getColumnListByParentId(resourceQueryVO.getCid());
            for (int i = 0; i < list.size(); i++) {
                idList.add(list.get(i).getId());
            }
        } else {
            idList.add(resourceQueryVO.getCid());
            flag = 2; // 单个2级图书
        }
        if (idList.size() == 0) {
            idList.add(resourceQueryVO.getCid());
            flag = 1; // 1级图书
//            return new ArrayList<>();
        }
        Map<String, Object> param = new HashMap<>(16);
        param.put("columnId", idList);
        if (resourceQueryVO.getStartTime() != null && !resourceQueryVO.getStartTime().equals("") && resourceQueryVO.getEndTime() != null && !resourceQueryVO.getEndTime().equals("")) {
            param.put("startTime", resourceQueryVO.getStartTime());
            param.put("endTime", resourceQueryVO.getEndTime());
        }
        if (resourceQueryVO.getAuthor() != null && !resourceQueryVO.getAuthor().equals("")) {
            param.put("author", resourceQueryVO.getAuthor());
        }
        if (resourceQueryVO.getBookName() != null && !resourceQueryVO.equals("")) {
            param.put("bookName", resourceQueryVO.getBookName());
        }
        if(resourceQueryVO.getBookIsbn() != null && !resourceQueryVO.getBookIsbn().equals("")){
            param.put("bookIsbn", resourceQueryVO.getBookIsbn());
        }
        if(resourceQueryVO.getUniqueId() != null && !resourceQueryVO.getUniqueId().equals("")){
            param.put("uniqueId", resourceQueryVO.getUniqueId());
        }
        if (resourceQueryVO.getSortField() != null && !resourceQueryVO.getSortField().equals("")) {
            param.put("sortField", resourceQueryVO.getSortField());
        }
        if (resourceQueryVO.getSortType() != null && !resourceQueryVO.getSortType().equals("")) {
            param.put("sortType", resourceQueryVO.getSortType());
        }
        List<BookVO> list = columnMapper.getMetadataListForColumn(param);
        List<Map<String, Object>> relationList = columnMapper.getColumnBookRelationList(param);
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < relationList.size(); j++) {
                long metadataId = (long)relationList.get(j).get("metadata_id");
                if (list.get(i).getId() == metadataId) {
                    list.get(i).setSort((int)relationList.get(j).get("sort"));
                }
            }
        }
        List<BookVO> result = new ArrayList<>();
        if(flag == 20){
            result =   getPublishDate(list);
        }else{
            List<BookVO> sortList = new ArrayList<>();
            List<BookVO> publishDateList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getSort() == -1){
                    publishDateList.add(list.get(i));
                }else{
                    sortList.add(list.get(i));
                }
            }
            sortList = getSort(sortList);
            publishDateList = getPublishDate(publishDateList);
            result.addAll(sortList);
            result.addAll(publishDateList);
        }
        return result;
    }

    public List<BookVO> getSort(List<BookVO> list){
        Collections.sort(list, new Comparator<BookVO>() {
            @Override
            public int compare(BookVO o1, BookVO o2) {
                int i = o1.getSort()-o2.getSort();
                if(i == 0){
                    return o2.getPublishDate().compareTo(o1.getPublishDate());
                }
                return i;
            }
        });
        return list;
    }

    public List<BookVO> getPublishDate(List<BookVO> list){
        Collections.sort(list, new Comparator<BookVO>() {
            @Override
            public int compare(BookVO o1, BookVO o2) {
                return o2.getPublishDate().compareTo(o1.getPublishDate());
            }
        });
        return list;
    }

    @Override
    public boolean setSkinTemplate(int templateId, String type) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("templateId", templateId);
        map.put("type", type);
        PublicTemplateBean bean = columnMapper.getSkinTemplateByType(type);
        if (bean == null) {

            // 新增模板
            columnMapper.addSkinTemplate(map);
        } else {

            // 修改模板
            columnMapper.updateSkinTemplate(map);
        }
        return true;
    }

    @Override
    public Map<String, Object> getSkinTemplate() {
        Map<String, Object> map = new HashMap<>(16);
        PublicTemplateBean pcbean = columnMapper.getSkinTemplateByType(Constants.PC);
        PublicTemplateBean appbean = columnMapper.getSkinTemplateByType(Constants.APP);
        map.put(Constants.PC, pcbean.getTemplateId());
        map.put(Constants.APP, appbean.getTemplateId());
        return map;
    }
}
