package com.bfd.service.impl;

import java.util.List;

import com.bfd.bean.PrivateColumnBean;
import com.bfd.bean.PublicColumnBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfd.bean.BookBean;
import com.bfd.common.vo.PageVO;
import com.bfd.dao.mapper.ColumnMapper;
import com.bfd.dao.mapper.PrivateColumnMapper;
import com.bfd.enums.BookTypeEnum;
import com.bfd.service.ColumnService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @author: bing.shen
 * @date: 2018/8/8 17:51
 * @Description:
 */
@Service
public class ColumnServiceImpl implements ColumnService {

    private final int maxPageSize = 200;
    
    @Autowired
    private ColumnMapper columnMapper;
    
    @Autowired
    private PrivateColumnMapper privateColumnMapper;
    
    @Override
    public List<PublicColumnBean> queryPublicColumn(Long businessId) {
        return columnMapper.queryColumnList(businessId);
    }

    @Override
    public List<PrivateColumnBean> queryPrivateColumn(Long businessId) {
        return privateColumnMapper.queryColumnList(businessId);
    }
    
    @Override
    public PageVO<BookBean> bookPcPage(int pageNum, int pageSize, Long columnId, int type, Long parentId, String order) {
        //防止传入过大条数
        if(pageSize>maxPageSize){
            pageSize = maxPageSize;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<BookBean> list;
        // 如果是一级节点
        if (parentId == 0L) {
            if (type == BookTypeEnum.COLUMN.getKey()) {
                list = columnMapper.queryPcParentMetadataList(columnId, order);
            } else {
                list = privateColumnMapper.queryPcParentMetadataList(columnId, order);
            }
        } else {
            if (type == BookTypeEnum.COLUMN.getKey()) {
                list = columnMapper.queryPcMetadataList(columnId, order);
            } else {
                list = privateColumnMapper.queryPcMetadataList(columnId, order);
            }
        }
        PageInfo<BookBean> pageInfo = new PageInfo<>(list);
        return new PageVO<>(pageNum, pageSize, pageInfo.getTotal(), pageInfo.getList());
    }
    
    @Override
    public PageVO<BookBean> bookAppPage(int pageNum, int pageSize, Long columnId, int type, Long parentId, String order) {
        //防止传入过大条数
        if(pageSize>maxPageSize){
            pageSize = maxPageSize;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<BookBean> list;
        // 如果是一级节点
        if (parentId == 0L) {
            if (type == BookTypeEnum.COLUMN.getKey()) {
                list = columnMapper.queryAppParentMetadataList(columnId, order);
            } else {
                list = privateColumnMapper.queryAppParentMetadataList(columnId, order);
            }
        } else {
            if (type == BookTypeEnum.COLUMN.getKey()) {
                list = columnMapper.queryAppMetadataList(columnId, order);
            } else {
                list = privateColumnMapper.queryAppMetadataList(columnId, order);
            }
        }
        PageInfo<BookBean> pageInfo = new PageInfo<>(list);
        return new PageVO<>(pageNum, pageSize, pageInfo.getTotal(), pageInfo.getList());
    }
    
    @Override
    public String getTemplate(Long businessId) {
        return privateColumnMapper.getTemplate(businessId);
    }
    
}
