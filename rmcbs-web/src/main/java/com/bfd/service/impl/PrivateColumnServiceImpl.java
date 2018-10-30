package com.bfd.service.impl;

import java.util.*;

import com.bfd.bean.ColumnBean;
import com.bfd.domain.User;
import com.bfd.utils.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfd.bean.PrivateColumnBean;
import com.bfd.bean.PrivateTemplateBean;
import com.bfd.common.vo.Constants;
import com.bfd.common.vo.PageVO;
import com.bfd.dao.mapper.PrivateColumnMapper;
import com.bfd.dao.mapper.PrivateDataPackageMapper;
import com.bfd.enums.EpubStatusEnum;
import com.bfd.enums.PdfStatusEnum;
import com.bfd.param.vo.BookVO;
import com.bfd.param.vo.ResourceBookQueryVO;
import com.bfd.param.vo.ResourceQueryVO;
import com.bfd.service.PrivateColumnService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 个性化栏目serviceImpl
 *
 * @author jiang.liu
 * @date 2018-08-03
 */
@Service
public class PrivateColumnServiceImpl implements PrivateColumnService {

    @Autowired
    PrivateColumnMapper privateColumnMapper;

    @Autowired
    PrivateDataPackageMapper privateDataPackageMapper;

    @Override
    public List<PrivateColumnBean> getPrivateColumnList(long businessId) {
        List<PrivateColumnBean> list = privateColumnMapper.getPrivateColumnList(businessId);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getParentId() == 0) {

                // 分两种情况，一级下有书（直接统计图书数量），一级下有分类（统计所有分类下图书数量）
                List<PrivateColumnBean> chirdList = privateColumnMapper.getPrivateColumnListByParentId(list.get(i).getId());
                if(chirdList.size() == 0){
                    list.get(i).setBookNumber(privateColumnMapper.getBookNumberByFirst(list.get(i).getId()));
                }else{
                    list.get(i).setBookNumber(privateColumnMapper.getBookNumberByA(list.get(i).getId()));
                }
            } else {
                list.get(i).setBookNumber(privateColumnMapper.getBookNumber(list.get(i).getId()));
            }
        }
        return list;
    }

    @Override
    public long insert(PrivateColumnBean privateColumnBean) {
        Long userId = SpringSecurityUtil.getCurrentUserId();
        privateColumnBean.setCreateUser(userId);
        privateColumnBean.setUpdateUser(userId);
        // 为新增的栏目设置排序字段为0,设置为排序第一位
        privateColumnBean.setSort(0);
        privateColumnMapper.updateSortAddByParentId(privateColumnBean.getParentId());
        privateColumnMapper.insert(privateColumnBean);
        return privateColumnBean.getId();
    }

    @Override
    public long update(PrivateColumnBean privateColumnBean) {
        Long userId = SpringSecurityUtil.getCurrentUserId();
        privateColumnBean.setUpdateUser(userId);
        privateColumnMapper.update(privateColumnBean);
        return privateColumnBean.getId();
    }

    @Override
    public boolean delete(long id) {

        // 判断需要删除的个性化栏目是一级或者二级，如果是一级,需要删除该一级、一级的所有子节、子节点和图书关系
        // 如果是二级,需要删除该二级、二级和图书关系
        PrivateColumnBean bean = privateColumnMapper.getPrivateColumnInfoById(id);
        if (bean.getParentId() == 0) {

            // 根据一级个性化栏目id查询所有子节点（二级列表）
            List<PrivateColumnBean> beanList = privateColumnMapper.getPrivateColumnListByParentId(id);
            if (!beanList.isEmpty()) {
 
                // 根据栏目id删除栏目与图书关系表映射
                privateColumnMapper.batchDeletePrivateColumnBookByColumnId(beanList);

                // 根据二级栏目对象列表批量删除所有二级栏目
                privateColumnMapper.batchDeletePrivateColumn(beanList);
            }else{

                // 如果栏目是一级，并且栏目没有子节点，则判断该一级栏目和客户单位有无关联，
                List<PrivateColumnBean> firstBeanList = new ArrayList<>();
                firstBeanList.add(bean);

                // 根据栏目id删除栏目与图书关系表映射
                privateColumnMapper.batchDeletePrivateColumnBookByColumnId(firstBeanList);
            }
        } else {

            // 根据栏目id删除栏目与图书关系表映射
            privateColumnMapper.deletePrivateColumnBookByColumnId(id);
        }
        return privateColumnMapper.delete(id);
    }

//    @Override
//    public boolean movePrivateColumnSort(long firstId, long secondId) {
//        PrivateColumnBean firstColumn = privateColumnMapper.getPrivateColumnInfoById(firstId);
//        int firstSort = firstColumn.getSort();
//        PrivateColumnBean secondColumn = privateColumnMapper.getPrivateColumnInfoById(secondId);
//        int secondSort = secondColumn.getSort();
//        firstColumn.setSort(secondSort);
//        secondColumn.setSort(firstSort);
//        privateColumnMapper.update(firstColumn);
//        privateColumnMapper.update(secondColumn);
//        return true;
//    }

    @Override
    public boolean movePrivateColumnSort(long firstId, long secondId) {

        // 目标对象2 或 9
        PrivateColumnBean firstColumn = privateColumnMapper.getPrivateColumnInfoById(firstId);
        // 目标sort=2 或 sort=9
        int firstSort = firstColumn.getSort();
        // 需要移动的对象7
        PrivateColumnBean secondColumn = privateColumnMapper.getPrivateColumnInfoById(secondId);
        // 需要移动对象的sort=7
        int secondSort = secondColumn.getSort();

        if(firstSort < secondSort){
            // 找到2--7之间的对象list
            Map<String, Object> param = new HashMap<>(16);
            param.put("id", firstColumn.getParentId());
            param.put("firstSort", firstSort);
            param.put("secondSort", secondSort);
            List<PrivateColumnBean> columnList = privateColumnMapper.getColumnListByRang(param);

            // 7的sort更新为2的sort
            secondColumn.setSort(firstSort);

            // 2--7之间的每个对象的sort+1
            for (int i = 0; i < columnList.size(); i++) {
                columnList.get(i).setSort(columnList.get(i).getSort()+1);
                privateColumnMapper.update(columnList.get(i));
            }
            privateColumnMapper.update(secondColumn);
        }else{
            // 找到9后面的对象list
            Map<String, Object> param = new HashMap<>(16);
            param.put("id", firstColumn.getParentId());
            param.put("firstSort", firstSort);
            List<PrivateColumnBean> columnList = privateColumnMapper.getColumnListByAfter(param);

            // 7的sort更新为9的sort
            secondColumn.setSort(firstSort);

            // 9之后的每个对象的sort+1
            for (int i = 0; i < columnList.size(); i++) {
                columnList.get(i).setSort(columnList.get(i).getSort()+1);
                privateColumnMapper.update(columnList.get(i));
            }
            privateColumnMapper.update(secondColumn);
        }
        return true;
    }

    @Override
    public boolean addBookMetadata(long privatecolumnId, List<Long> metadataIds) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("privatecolumnId", privatecolumnId);
//        param.put("n", metadataIds.size());
//        privateColumnMapper.updateRelationSortAddByColumnId(param);
        for (int i = 0; i < metadataIds.size(); i++) {
            param.put("metadataId", metadataIds.get(i));
            param.put("sort", -1);
            privateColumnMapper.addBookMetadata(param);
        }
        return true;
    }

    @Override
    public boolean batchDeleteFromPrivateColumn(long privateColumnId, List<Long> metadataIds) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("privateColumnId", privateColumnId);
        param.put("metadataIds", metadataIds);
        privateColumnMapper.batchDeleteFromPrivateColumn(param);
        return true;
    }

    @Override
    public List<BookVO> getMetadataListByPrivateColumnId(long privateColumnId) {
        return privateColumnMapper.getMetadataListByPrivateColumnId(privateColumnId);
    }

    @Override
    public PageVO<BookVO> queryMetadataList(ResourceBookQueryVO vo) {
        if(vo.getSortField() == null && vo.getSortField().equals("")){
            vo.setSortField("create_time");
        }
        if(vo.getSortType() == null && vo.getSortType().equals("")){
            vo.setSortType("desc");
        }
        PageHelper.startPage(vo.getCurrent(), vo.getPageSize());
        Map<String, Object> param = new HashMap<>(16);
        if (vo.getStartTime() != null && !vo.getStartTime().equals("") && vo
                .getEndTime() != null && !vo.getEndTime().equals("")) {
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
        if(vo.getSortField()!= null && !vo.getSortField().equals("")){
            param.put("sortField", vo.getSortField());
        }
        if(vo.getSortType() != null && !vo.getSortType().equals("")){
            param.put("sortType", vo.getSortType());
        }
        List<BookVO> list = privateDataPackageMapper.queryMetadataList(param);
        PageInfo<BookVO> pageInfo = new PageInfo<>(list);
        List<BookVO> listOfColumn = privateColumnMapper.getMetadataListByPrivateColumnId(vo.getCid());
        for (int i = 0; i < pageInfo.getList().size(); i++) {
            if (listOfColumn.contains(pageInfo.getList().get(i))) {
                pageInfo.getList().get(i).setChecked(true);
            }
            StringBuffer buff = new StringBuffer();
            buff.append(" ");
            if (pageInfo.getList().get(i).getBookPdf().equals(PdfStatusEnum.UPLOAD.getKey())) {
                buff.append(" PDF");
            }
            if (pageInfo.getList().get(i).getBookEpub().equals(EpubStatusEnum.ENCRYPTED.getKey())) {
                buff.append(" EPUB ");
            }
            if (pageInfo.getList().get(i).getBookXml().equals("1")) {
                buff.append("XML ");
            }
            pageInfo.getList().get(i).setBookType(buff.toString());

        }
        return new PageVO<>(vo.getCurrent(), vo.getPageSize(), pageInfo.getTotal(), pageInfo.getList());
    }

//    @Override
//    public boolean moveBookSortForPrivateColumn(long privateColumnId, long firstBookId, long secondBookId) {
//
//        Map<String, Object> param = new HashMap<>(16);
//        param.put("privateColumnId", privateColumnId);
//        param.put("metadataId", firstBookId);
//        Map<String, Object> relationBeanOne = privateColumnMapper.getPrivateColumnBookRelationBean(param);
//        int firstSort = (int) relationBeanOne.get("sort");
//        param.put("metadataId", secondBookId);
//        Map<String, Object> relationBeanTwo = privateColumnMapper.getPrivateColumnBookRelationBean(param);
//        int secondSort = (int) relationBeanTwo.get("sort");
//        param.put("metadataId", firstBookId);
//        param.put("sort", secondSort);
//        privateColumnMapper.updatePrivateColumnBookRelationSort(param);
//        param.put("metadataId", secondBookId);
//        param.put("sort", firstSort);
//        privateColumnMapper.updatePrivateColumnBookRelationSort(param);
//        return true;
//    }
//
//    @Override
//    public boolean setTopForPrivateColumnBook(long privateColumnId, long metadataId) {
//
//        Map<String, Object> param = new HashMap<>(16);
//        param.put("privateColumnId", privateColumnId);
//        param.put("metadataId", metadataId);
//        List<Long> ids = privateColumnMapper.getBeforeRelationIdsByPrivateColumnIdAndMetadataId(param);
//        if (ids.size() > 0) {
//            param.put("ids", ids);
//            privateColumnMapper.updateRelationSortAddByIds(param);
//        }
//        param.put("sort", 0);
//        privateColumnMapper.updatePrivateColumnBookRelationSort(param);
//        return true;
//    }
//
//    @Override
//    public boolean setDownForPrivateColumnBook(long privateColumnId, long metadataId) {
//
//        Map<String, Object> param = new HashMap<>(16);
//        param.put("privateColumnId", privateColumnId);
//        param.put("metadataId", metadataId);
//        List<Long> ids = privateColumnMapper.getAfterRelationIdsByPrivateColumnIdAndMetadataId(param);
//        int maxSort = privateColumnMapper.getMaxSortByPrivateColumnId(privateColumnId);
//        if (ids.size() > 0) {
//            param.put("ids", ids);
//            privateColumnMapper.updateRelationSortReduceByIds(param);
//        }
//        param.put("sort", maxSort);
//        privateColumnMapper.updatePrivateColumnBookRelationSort(param);
//        return true;
//    }

    @Override
    public boolean setBookSort(long privateColumnId, long metadataId, int sort) {

        Map<String, Object> param = new HashMap<>(16);
        param.put("privateColumnId", privateColumnId);
        param.put("metadataId", metadataId);
        param.put("sort", sort);
        privateColumnMapper.updatePrivateColumnBookRelationSort(param);
        return true;
    }

    @Override
    public List<BookVO> getMetadataListForPrivateColumn(ResourceQueryVO resourceQueryVO) {
        if(resourceQueryVO.getSortField() == null && resourceQueryVO.getSortField().equals("")){
            resourceQueryVO.setSortField("create_time");
        }
        if(resourceQueryVO.getSortType() == null && resourceQueryVO.getSortType().equals("")){
            resourceQueryVO.setSortType("desc");
        }
        PrivateColumnBean  privateColumnBean = privateColumnMapper.getPrivateColumnInfoById(resourceQueryVO.getCid());
        List<Long> idList = new ArrayList<>();
        int flag = 20; // 所有2级图书
        if(privateColumnBean != null && privateColumnBean.getParentId() == 0){
            List<PrivateColumnBean> list =  privateColumnMapper.getPrivateColumnListByParentId(resourceQueryVO.getCid());
            for (int i = 0; i < list.size(); i++) {
                idList.add(list.get(i).getId());
            }
        }else{
            idList.add(resourceQueryVO.getCid());
            flag = 2; // 单个2级图书
        }
        if(idList.size() == 0){
            idList.add(resourceQueryVO.getCid());
            flag = 1; // 1级图书
//            return new ArrayList<>();
        }
        Map<String, Object> param = new HashMap<>(16);
        param.put("privateColumnId", idList);
        if (resourceQueryVO.getStartTime() != null && !resourceQueryVO.getStartTime().equals("") && resourceQueryVO
                .getEndTime() != null && !resourceQueryVO.getEndTime().equals("")) {
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
        if(resourceQueryVO.getSortField()!= null && !resourceQueryVO.getSortField().equals("")){
            param.put("sortField", resourceQueryVO.getSortField());
        }
        if(resourceQueryVO.getSortType()!= null && !resourceQueryVO.getSortType().equals("")){
            param.put("sortType", resourceQueryVO.getSortType());
        }
        List<BookVO> list = privateColumnMapper.getMetadataListForPrivateColumn(param);
        List<Map<String, Object>> relationList = privateColumnMapper.getPrivateColumnBookRelationList(param);
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < relationList.size(); j++) {
                long metadataId = (long) relationList.get(j).get("metadata_id");
                if (list.get(i).getId() == metadataId) {
                    list.get(i).setSort((int) relationList.get(j).get("sort"));
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
    public boolean setSkinColor(String skinColor, long companyId) {
        Map<String,Object> map = new HashMap<>(16);
        map.put("skin",skinColor);
        map.put("companyId",companyId);
        PrivateTemplateBean bean = privateColumnMapper.getSkinColorInfo(map);
        if (bean == null) {

            // 新增个性化模板
            privateColumnMapper.addSkinColor(map);
        } else {

            // 修改个性化模板
            privateColumnMapper.updateSkinColor(map);
        }
        return true;
    }

    @Override
    public PrivateTemplateBean getSkinColor(long companyId) {
        Map<String,Object> param = new HashMap<>(16);
        param.put("companyId",companyId);
        PrivateTemplateBean bean = privateColumnMapper.getSkinColorInfo(param);
        return bean;
    }

}
