package com.bfd.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bfd.domain.User;
import com.bfd.utils.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfd.bean.PrivateDataPackageBean;
import com.bfd.common.vo.PageVO;
import com.bfd.dao.mapper.PrivateDataPackageMapper;
import com.bfd.enums.EpubStatusEnum;
import com.bfd.enums.PdfStatusEnum;
import com.bfd.param.vo.BookVO;
import com.bfd.param.vo.ResourceBookQueryVO;
import com.bfd.param.vo.ResourceQueryVO;
import com.bfd.service.PrivateDataPackageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 个性化数据包serviceImpl
 *
 * @author jiang.liu
 * @date 2018-07-26
 */
@Service
public class PrivateDataPackageServiceImpl implements PrivateDataPackageService {

    @Autowired
    PrivateDataPackageMapper privateDataPackageMapper;

    @Override
    public List<PrivateDataPackageBean> getPrivatePackageList(long businessId) {
        List<PrivateDataPackageBean> list = privateDataPackageMapper.getPrivatePackageList(businessId);
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getParentId() == 0){

                // 分两种情况，一级下有书（直接统计图书数量），一级下有分类（统计所有分类下图书数量）
                List<PrivateDataPackageBean> chirdList = privateDataPackageMapper.getPrivateDataPackageListByParentId(list.get(i).getId());
                if(chirdList.size() == 0){
                    list.get(i).setBookNumber(privateDataPackageMapper.getBookNumberByFirst(list.get(i).getId()));
                }else{
                    list.get(i).setBookNumber(privateDataPackageMapper.getBookNumberByA(list.get(i).getId()));
                }
            }else{
                list.get(i).setBookNumber(privateDataPackageMapper.getBookNumber(list.get(i).getId()));
            }
        }
        return list;
    }

    @Override
    public long insert(PrivateDataPackageBean privatedataPackageBean) {
        Long userId = SpringSecurityUtil.getCurrentUserId();
        privatedataPackageBean.setCreateUser(userId);
        privatedataPackageBean.setUpdateUser(userId);
        privateDataPackageMapper.insert(privatedataPackageBean);
        return privatedataPackageBean.getId();
    }

    @Override
    public long update(PrivateDataPackageBean privatedataPackageBean) {
        Long userId = SpringSecurityUtil.getCurrentUserId();
        privatedataPackageBean.setUpdateUser(userId);
        privateDataPackageMapper.update(privatedataPackageBean);
        return privatedataPackageBean.getId();
    }

    @Override
    public boolean delete(long id) {

        // 判断需要删除的个性化数据包是一级或者二级，如果是一级，需要删除该一级、一级的所有子节点、子节点和图书关系
        // 如果是二级，需要删除该二级、二级和图书关系
        PrivateDataPackageBean bean = privateDataPackageMapper.getPrivatePackageInfoById(id);
        if (bean.getParentId() == 0) {

            // 根据一级个性化数据包id查询所有子节点（二级列表）
            List<PrivateDataPackageBean> beanList = privateDataPackageMapper.getPrivateDataPackageListByParentId(id);
            if(!beanList.isEmpty()){

                // 根据个性化数据包id删除数据包与图书关系表映射
                privateDataPackageMapper.batchDeleteRelationByPrivateDataPackageId(beanList);

                // 根据二级个性化数据包对象列表批量删除所有二级数据包
                privateDataPackageMapper.batchDeletePrivateDataPackage(beanList);
            }else{

                // 如果栏目是一级，并且栏目没有子节点，则判断该一级栏目和客户单位有无关联，
                List<PrivateDataPackageBean> firstBeanList = new ArrayList<>();
                firstBeanList.add(bean);

                // 根据栏目id删除栏目与图书关系表映射
                privateDataPackageMapper.batchDeleteRelationByPrivateDataPackageId(firstBeanList);
            }
        } else {

            // 根据个性化数据包id删除数据包与图书关系表映射
            privateDataPackageMapper.deleteRelationByDataPackageId(id);
        }

        // 根据个性化数据包id删除该数据包
        return privateDataPackageMapper.delete(id);
    }

    @Override
    public boolean addBookForPrivateDataPackage(long privateDataPackageId, List<Long> metadataIds) {
        Map<String, Object> param = new HashMap<>();
        param.put("privateDataPackageId", privateDataPackageId);
        param.put("metadataIds", metadataIds);
        privateDataPackageMapper.addBookForPrivateDataPackage(param);
        return true;
    }

    @Override
    public boolean batchDeleteFromPrivateDataPackage(long privateDataPackageId, List<Long> metadataIds) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("privateDataPackageId", privateDataPackageId);
        param.put("metadataIds", metadataIds);
        return privateDataPackageMapper.batchDeleteFromPrivateDataPackage(param);
    }

    @Override
    public List<BookVO> getMetadataListByPrivateDataPackageId(long privateDataPackageId) {
        return privateDataPackageMapper.getMetadataListByPrivatePackageId(privateDataPackageId);
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
        List<BookVO> list = privateDataPackageMapper.queryMetadataList(param);
        PageInfo<BookVO> pageInfo = new PageInfo<>(list);
        List<BookVO> listOfPackage = privateDataPackageMapper.getMetadataListByPrivatePackageId(vo.getCid());
        for (int i = 0; i < pageInfo.getList().size(); i++) {
            if(listOfPackage.contains(pageInfo.getList().get(i))){
                pageInfo.getList().get(i).setChecked(true);
            }
            StringBuffer buff = new StringBuffer();
            buff.append(" ");
            if(pageInfo.getList().get(i).getBookPdf().equals(PdfStatusEnum.UPLOAD.getKey())){
                buff.append("PDF");
            }
            if(pageInfo.getList().get(i).getBookEpub().equals(EpubStatusEnum.ENCRYPTED.getKey())){
                buff.append(" EPUB");
            }
            if(pageInfo.getList().get(i).getBookXml().equals("1")){
                buff.append(" XML");
            }
            pageInfo.getList().get(i).setBookType(buff.toString());
        }
        return new PageVO<>(vo.getCurrent(), vo.getPageSize(), pageInfo.getTotal(), pageInfo.getList());
    }


    @Override
    public List<BookVO> getMetadataListForPrivatePackage(ResourceQueryVO resourceQueryVO) {
        if(resourceQueryVO.getSortField() == null || resourceQueryVO.getSortField().equals("")){
            resourceQueryVO.setSortField("create_time");
        }
        if(resourceQueryVO.getSortType() == null || resourceQueryVO.getSortType().equals("")){
            resourceQueryVO.setSortType("desc");
        }
        PrivateDataPackageBean privateDataPackageBean = privateDataPackageMapper.getPrivatePackageInfoById(resourceQueryVO.getCid());
        List<Long> idList = new ArrayList<>();
        if(privateDataPackageBean.getParentId() == 0){
            List<PrivateDataPackageBean> list = privateDataPackageMapper.getPrivateDataPackageListByParentId(resourceQueryVO.getCid());
            for (int i = 0; i < list.size(); i++) {
                idList.add(list.get(i).getId());
            }
        }else{
            idList.add(resourceQueryVO.getCid());
        }
        if(idList.size() == 0){
            idList.add(resourceQueryVO.getCid());
//            return new ArrayList<>();
        }
        Map<String, Object> param = new HashMap<>(16);
        param.put("privateDataPackageId", idList);
        if(resourceQueryVO.getStartTime() != null &&!resourceQueryVO.getStartTime().equals("") && resourceQueryVO
                .getEndTime() != null &&!resourceQueryVO.getEndTime().equals("")){
            param.put("startTime", resourceQueryVO.getStartTime());
            param.put("endTime", resourceQueryVO.getEndTime());
        }
        if(resourceQueryVO.getAuthor() != null && !resourceQueryVO.getAuthor().equals("")){
            param.put("author", resourceQueryVO.getAuthor());
        }
        if(resourceQueryVO.getBookName() != null && !resourceQueryVO.equals("")){
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
        return privateDataPackageMapper.getMetadataListForPrivatePackage(param);
    }

}
