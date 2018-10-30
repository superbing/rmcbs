package com.bfd.dao.mapper;

import java.util.List;
import java.util.Map;

import com.bfd.bean.ColumnBean;
import com.bfd.bean.DataPackageBean;
import com.bfd.bean.DataPackgeBookBean;
import com.bfd.bean.MetadataBean;
import com.bfd.param.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * MetadataMapper
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年7月31日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Repository
public interface MetadataMapper {
    
    public void addMetadata(MetadataBean metadata);
    
    public void updateMetadata(MetadataBean metadata);
    
    public MetadataBean getMetaByUniqueId(String uniqueId);
    
    public List<MetadataBean> getMeta(MetadataVO metadataVo);
    
    public void deleteMetadataById(String id);
    
    public void deleteColumnRelationByMetaId(String id);
    
    public void deleteDataPackageRelationByMetaId(String id);
    
    public void deletePrivateColumnRelationByMetaId(String id);
    
    public void deletePrivateDataPackageRelationByMetaId(String id);
    
    /**
     * 根据元数据id获取与之关联的栏目id
     * 
     * @param metadataId
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<Long> getColumnIdListByMetadataId(Long metadataId);
    
    /**
     * 根据元数据id和栏目id删除两者关系
     * 
     * @param delParam(metadataId和columnId)
     * @see [类、类#方法、类#成员]
     */
    public void deleteColumnMetaRelation(Map<String, Object> delParam);
    
    /**
     * 更新栏目id下所有的sort(sort = sort +1)
     * 
     * @param columnId
     * @see [类、类#方法、类#成员]
     */
    public void updateColumnRelationByColumnId(Long columnId);
    
    /**
     * 新增栏目和元数据的关系
     * 
     * @param param
     * @see [类、类#方法、类#成员]
     */
    public void addColumnRelation(Map<String, Object> param);
    
    /**
     * 获取元数据总数
     * 
     * @return
     */
    long getMetadataTotal();
    
    /**
     * 根据元数据id获取与之关联的数据包id
     * 
     * @param metadataId
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<Long> getPackageIdListByMetadataId(Long metadataId);
    
    /**
     * 根据元数据id和数据包id删除两者关系
     * 
     * @param bean
     * @see [类、类#方法、类#成员]
     */
    public void deletePackageMetaRelation(DataPackgeBookBean bean);
    
    /**
     * 新增数据包和元数据的关系
     * 
     * @param param
     * @see [类、类#方法、类#成员]
     */
    public void addDataPakcageRelation(Map<String, Object> param);
    
    /**
     * 获取图书设置展示界面
     * 
     * @param metadataTermsVO
     * @return
     */
    List<MetadataSetVO> getBookOptionsList(MetadataTermsVO metadataTermsVO);
    
    /**
     * 获取数据包列表
     *
     * @return
     */
    List<DataPackageVO> getPackageList();
    
    /**
     * 获取栏目列表
     *
     * @return
     */
    List<ColumnVO> getColumnList();
    
    /**
     * 根图书id查询数据包列表
     *
     * @param metadataId 数据包id
     * @return
     */
    List<DataPackageVO> getPackageListByMetadataId(long metadataId);
    
    /**
     * 根图书id查询数据包列表
     *
     * @param metadataId 数据包id
     * @return
     */
    List<ColumnVO> getColumnListByMetadataId(long metadataId);
    
    /**
     * 根据id列表查询元数据
     * 
     * @param bookIds
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<MetadataBean> getMetaDataList(List<Integer> bookIds);
    
    /**
     * 根据元数据ID的集合，获取其所属栏目名称
     * 
     * @param metadataSetVOS
     * @return
     */
    List<ColumnBean> getColumnNames(@Param(value = "list") List<MetadataSetVO> metadataSetVOS);
    
    /**
     * 根据元数据ID的集合，获取其所属数据包名称
     * 
     * @param metadataSetVOS
     * @return
     */
    List<DataPackageBean> getPackageNames(@Param(value = "list") List<MetadataSetVO> metadataSetVOS);
    
    /**
     * 根据唯一标识列表查询元数据
     * 
     * @param uniqueIdList
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<MetadataBean> getMetadataListByUniqueIdList(List<String> uniqueIdList);
    
    /**
     * 根据metadataId删除与数据包的关系
     * 
     * @param metadataId
     * @see [类、类#方法、类#成员]
     */
    public void deletePackageRelationByMetadataId(Long metadataId);
    
    /**
     * 根据metadataId删除与数据包的关系
     * 
     * @param metadataId
     * @see [类、类#方法、类#成员]
     */
    public void deleteColumnRelationByMetadataId(Long metadataId);


    List<MetadataBean> getMetaLimit(MetadataVO metadataVo);
}
