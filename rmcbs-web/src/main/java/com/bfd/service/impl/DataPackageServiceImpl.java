package com.bfd.service.impl;

import com.bfd.bean.BusinessColumnBean;
import com.bfd.bean.BusinessPackageBean;
import com.bfd.bean.DataPackageBean;
import com.bfd.common.exception.RmcbsException;
import com.bfd.common.vo.Constants;
import com.bfd.common.vo.PageVO;
import com.bfd.dao.mapper.DataPackageMapper;
import com.bfd.enums.EpubStatusEnum;
import com.bfd.enums.PdfStatusEnum;
import com.bfd.param.vo.BookVO;
import com.bfd.param.vo.ResourceBookQueryVO;
import com.bfd.param.vo.ResourceQueryVO;
import com.bfd.service.DataPackageService;
import com.bfd.utils.SpringSecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据包serviceImpl
 *
 * @author jiang.liu
 * @date 2018-07-26
 */

@Service
public class DataPackageServiceImpl implements DataPackageService {

    @Autowired
    DataPackageMapper dataPackageMapper;

    @Override
    public List<DataPackageBean> getPackageList() {
        List<DataPackageBean> list = dataPackageMapper.getPackageList();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getParentId() == 0){

                // 分两种情况，一级下有书（直接统计图书数量），一级下有分类（统计所有分类下图书数量）
                List<DataPackageBean> chirdList = dataPackageMapper.getPackageListByParentId(list.get(i).getId());
                if(chirdList.size() == 0){
                    list.get(i).setBookNumber(dataPackageMapper.getBookNumberByFirst(list.get(i).getId()));
                }else{
                    list.get(i).setBookNumber(dataPackageMapper.getBookNumberByA(list.get(i).getId()));
                }
            }else{
                list.get(i).setBookNumber(dataPackageMapper.getBookNumber(list.get(i).getId()));
            }
        }
        return list;
    }

    @Override
    public List<DataPackageBean> getPackageListByParentId(long id) {
        List<DataPackageBean> list = dataPackageMapper.getPackageListByParentId(id);
        return list;
    }

    @Override
    public long insert(DataPackageBean dataPackageBean) {
        Long userId = SpringSecurityUtil.getCurrentUserId();
        dataPackageBean.setCreateUser(userId);
        dataPackageBean.setUpdateUser(userId);
        dataPackageMapper.insert(dataPackageBean);
        long id = dataPackageBean.getId();
        return id;
    }

    @Override
    public long update(DataPackageBean dataPackageBean) {
        Long userId = SpringSecurityUtil.getCurrentUserId();
        dataPackageBean.setUpdateUser(userId);
        dataPackageMapper.update(dataPackageBean);
        return dataPackageBean.getId();
    }

    @Override
    public boolean delete(long id) {

        // 判断需要删除的数据包是一级或者二级，如果是一级，需要删除该一级、一级的所有子节点、子节点和图书关系
        // 如果是二级，需要删除该二级、二级和图书关系
        DataPackageBean bean = dataPackageMapper.getPackageInfoById(id);
        if (bean.getParentId() == 0) {
            // 根据一级数据包id查询所有子节点（二级列表）

            List<DataPackageBean> beanList = getPackageListByParentId(id);
            if(!beanList.isEmpty()){

                // 如果栏目和客户单位有关联，则不能进行删除
                List<BusinessPackageBean> relationList = dataPackageMapper.getPackageBusinessRelation(beanList);
                if (!CollectionUtils.isEmpty(relationList)) {
                    throw new RmcbsException("此数据包的所有子数据包关联了" + relationList.size() + "个客户单位，请先解除关联！");
                }

                // 根据数据包id删除数据包与图书关系表映射
                dataPackageMapper.batchDeleteDataPackageBookByDataPackageId(beanList);

                // 根据二级数据包对象列表批量删除所有二级数据包
                dataPackageMapper.batchDeleteDataPackage(beanList);
            }else{

                // 如果数据包是一级，并且数据包没有子节点，则判断该一级数据包和客户单位有无关联，
                List<DataPackageBean> firstBeanList = new ArrayList<>();
                firstBeanList.add(bean);
                List<BusinessPackageBean> relationList = dataPackageMapper.getPackageBusinessRelation(firstBeanList);
                if (!CollectionUtils.isEmpty(relationList)) {
                    throw new RmcbsException("此数据包关联了" + relationList.size() + "个客户单位，请先解除关联！");
                }

                // 根据数据包id删除数据包与图书关系表映射
                dataPackageMapper.batchDeleteDataPackageBookByDataPackageId(firstBeanList);
            }
        } else {
            List<DataPackageBean> beanList = new ArrayList<DataPackageBean>();
            beanList.add(bean);
            List<BusinessPackageBean> relationList = dataPackageMapper.getPackageBusinessRelation(beanList);
            if (!CollectionUtils.isEmpty(relationList)) {
                throw new RmcbsException("此数据包关联了" + relationList.size() + "个客户单位，请解除关联！");
            }

            // 根据数据包id删除数据包与图书关系表映射
            dataPackageMapper.deleteDataPackageBookByDataPackageId(id);
        }

        // 根据数据包id删除该数据包
        return dataPackageMapper.delete(id);
    }

    @Override
    public boolean addBookMetadata(long dataPackageId, List<Long> metadataIds) {
        if(metadataIds != null && metadataIds.size() > Constants.ZERO){
            Map<String, Object> param = new HashMap<>();
            param.put("dataPackageId", dataPackageId);
            param.put("metadataIds", metadataIds);
            dataPackageMapper.addBookMetadata(param);
        }
        return true;
    }

    @Override
    public boolean batchDeleteFromDataPackage(long dataPackageId, List<Long> metadataIds) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("dataPackageId", dataPackageId);
        param.put("metadataIds", metadataIds);
        return dataPackageMapper.batchDeleteFromDataPackage(param);
    }

    @Override
    public List<BookVO> getMetadataListByPackageId(long dataPackageId) {
        return dataPackageMapper.getMetadataListByPackageId(dataPackageId);
    }

    @Override
    public PageVO<BookVO> queryMetadataList(ResourceBookQueryVO vo) {
        if(vo.getSortField() == null || vo.getSortField().equals("")){
            vo.setSortField("create_time");
        }
        if(vo.getSortType() == null || vo.getSortType().equals("")){
            vo.setSortType("desc");
        }
        PageHelper.startPage(vo.getCurrent(), vo.getPageSize());
        Map<String, Object> param = new HashMap<>(16);
        if(vo.getStartTime() != null &&!vo.getStartTime().equals("") && vo
                .getEndTime() != null &&!vo.getEndTime().equals("")){
            param.put("startTime", vo.getStartTime());
            param.put("endTime", vo.getEndTime());
        }
        if(vo.getBookStatus() != 0){
            switch(vo.getBookStatus()){
                case 1:
                    param.put("bookPdf", PdfStatusEnum.UPLOAD.getKey());
                    break;
                case 2:
                    param.put("bookEpub", EpubStatusEnum.ENCRYPTED.getKey());
                    break;
                case 3:
                    param.put("bookXml", 1);
                    break;
                case 4:
                    param.put("bookStatus", 1);
            }
        }
        if(vo.getBookName() != null && !vo.getBookName().equals("")){
            param.put("bookName", vo.getBookName());
        }
        if(vo.getUniqueId() != null && !vo.getUniqueId().equals("")){
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

        List<BookVO> list = dataPackageMapper.queryMetadataList(param);
        PageInfo<BookVO> pageInfo = new PageInfo<>(list);
        System.out.println("pageInfo====="+pageInfo.getList().toString());
        List<BookVO> listOfPackage = dataPackageMapper.getMetadataListByPackageId(vo.getCid());
        System.out.println("listOfPackage====="+listOfPackage.toString());
        for (int i = 0; i < pageInfo.getList().size(); i++) {
            if(listOfPackage.contains(pageInfo.getList().get(i))){
                pageInfo.getList().get(i).setChecked(true);
            }
            StringBuffer buff = new StringBuffer();
            buff.append(" ");
            if(pageInfo.getList().get(i).getBookPdf().equals(PdfStatusEnum.UPLOAD.getKey())){
                buff.append(" PDF ");
            }
            if(pageInfo.getList().get(i).getBookEpub().equals(EpubStatusEnum.ENCRYPTED.getKey())){
                buff.append(" EPUB ");
            }
            if(pageInfo.getList().get(i).getBookXml().equals("1")){
                buff.append(" XML ");
            }
            pageInfo.getList().get(i).setBookType(buff.toString());

        }
        return new PageVO<>(vo.getCurrent(), vo.getPageSize(), pageInfo.getTotal(), pageInfo.getList());

    }

    @Override
    public List<BookVO> getMetadataListForPackage(ResourceQueryVO resourceQueryVO) {

        if(resourceQueryVO.getSortField() == null || resourceQueryVO.getSortField().equals("")){
            resourceQueryVO.setSortField("create_time");
        }
        if(resourceQueryVO.getSortType() == null || resourceQueryVO.getSortType().equals("")){
            resourceQueryVO.setSortType("desc");
        }
        DataPackageBean dataPackageBean = dataPackageMapper.getPackageInfoById(resourceQueryVO.getCid());
        List<Long> idList = new ArrayList<>();
        if(dataPackageBean.getParentId() == 0){
            List<DataPackageBean> list = dataPackageMapper.getPackageListByParentId(resourceQueryVO.getCid());
            for (int i = 0; i < list.size(); i++) {
                idList.add(list.get(i).getId());
            }
        }else{
            idList.add(resourceQueryVO.getCid());
        }
        if(idList.size() == 0){

            // 查询一级节点下的图书列表
            idList.add(resourceQueryVO.getCid());
//            return new ArrayList<>();
        }
        Map<String, Object> param = new HashMap<>(16);
        param.put("dataPackageId", idList);
        if(resourceQueryVO.getStartTime() != null &&!resourceQueryVO.getStartTime().equals("") && resourceQueryVO
                .getEndTime() != null &&!resourceQueryVO.getEndTime().equals("")){
            param.put("startTime", resourceQueryVO.getStartTime());
            param.put("endTime", resourceQueryVO.getEndTime());
        }
        if(resourceQueryVO.getAuthor() != null && !resourceQueryVO.getAuthor().equals("")){
            param.put("author", resourceQueryVO.getAuthor());
        }
        if(resourceQueryVO.getBookName() != null && !resourceQueryVO.getBookName().equals("")){
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
        return dataPackageMapper.getMetadataListForPackage(param);
    }

}
