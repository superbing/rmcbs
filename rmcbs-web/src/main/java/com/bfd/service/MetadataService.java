package com.bfd.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bfd.bean.MetadataBean;
import com.bfd.common.vo.PageVO;
import com.bfd.param.vo.ColumnVO;
import com.bfd.param.vo.DataPackageVO;
import com.bfd.param.vo.MetadataSetVO;
import com.bfd.param.vo.MetadataTermsVO;
import com.bfd.param.vo.MetadataVO;

/**
 * 元数据管理Service
 *
 * @author xile
 * @version [版本号, 2018年7月31日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface MetadataService {

	/**
	 * 添加图书元数据
	 *
	 * @param metadata
	 * @see [类、类#方法、类#成员]
	 */
	void addMetadata(MetadataBean metadata);

	/**
	 * 更新元数据
	 *
	 * @param metadata
	 * @see [类、类#方法、类#成员]
	 */
	void updateMetadata(MetadataBean metadata);

	/**
	 * 根据唯一标识查询元数据
	 *
	 * @param id
	 * @see [类、类#方法、类#成员]
	 */
	MetadataBean getMetaByUniqueId(String id);

	/**
	 * 按条件查询
	 *
	 * @param metadataVo
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	PageVO<MetadataBean> getMeta(MetadataVO metadataVo);

	/**
	 * 删除元数据
	 *
	 * @param id
	 * @see [类、类#方法、类#成员]
	 */
	void deleteById(String id);

	/**
	 * 判断PDF是否存在
	 * 
	 * @param uniqueId
	 * @return
	 * @throws IOException
	 */
	String onlineQueryPDF(String uniqueId) throws IOException;

	/**
	 * 判断Epub是否存在
	 * 
	 * @param uniqueId
	 * @return
	 * @throws IOException
	 */
	String onlineQueryEpub(String uniqueId) throws IOException;

	/**
	 * 在线上传pdf
	 * 
	 * @param fileName
	 * @param bookId
	 */
	void onlineUploadPDF(String fileName, String bookId);

	/**
	 * 在线上传epub
	 * 
	 * @param fileName
	 * @param bookId
	 */
	void onlineUploadEpub(String fileName, String bookId);

	/**
	 * 添加元数据与栏目的关系
	 *
	 * @param metadataId
	 * @param columnIdList
	 * @see [类、类#方法、类#成员]
	 */
	void addColumnRelation(Long metadataId, List<Long> columnIdList);

	/**
	 * 添加元数据与数据包的关系
	 *
	 * @param metadataId
	 * @param packageIdList
	 * @see [类、类#方法、类#成员]
	 */
	void addDataPakcageRelation(Long metadataId, List<Long> packageIdList);

	/**
	 * 获取唯一id
	 *
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	// Map<String, String> getUniqueId();

	/**
	 * 获取图书设置展示界面
	 *
	 * @param metadataTermsVO
	 * @return
	 */
	PageVO<MetadataSetVO> getBookOptionsList(MetadataTermsVO metadataTermsVO);

	/**
	 * PDF文档上传
	 * 
	 * @param bytes
	 * @param bookId
	 * @param suffix
	 */
	void uploadPdf(byte[] bytes, String bookId, String suffix);

	/**
	 * XML目录上传
	 *
	 * @param file
	 * @param bookId
	 * @see [类、类#方法、类#成员]
	 */
	void uploadXmlChapter(MultipartFile file, String bookId);

	/**
	 * XML内容上传
	 *
	 * @param file
	 * @param bookId
	 * @see [类、类#方法、类#成员]
	 */
	void uploadXmlContent(MultipartFile file, String bookId);

	/**
	 * 上传EPUB
	 *
	 * @param file
	 * @param bookId
	 * @see [类、类#方法、类#成员]
	 */
	void uploadEpub(MultipartFile file, String bookId);

	/**
	 * 获取数据包分级树结构
	 * 
	 * @param metadataId
	 *            元数据id
	 *
	 * @return
	 */
	List<DataPackageVO> getPackageList(long metadataId);

	/**
	 * 获取栏目分级树结构
	 * 
	 * @param metadataId
	 *            元数据id
	 *
	 * @return
	 */
	List<ColumnVO> getColumnList(long metadataId);

	/**
	 * 根据id列表查询元数据
	 * 
	 * @param bookIds
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	List<MetadataBean> getMetaDataList(List<Integer> bookIds);

	/**
	 * 上传XML元数据
	 * 
	 * @param file
	 * @see [类、类#方法、类#成员]
	 */
	void uploadXmlMeta(MultipartFile file);

	/**
	 *
	 */
	void sendPdfMq(Integer num);

}
