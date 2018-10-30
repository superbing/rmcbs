package com.bfd.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bfd.bean.ColumnBean;
import com.bfd.bean.DataPackageBean;
import com.bfd.bean.DataPackgeBookBean;
import com.bfd.bean.MessageBean;
import com.bfd.bean.MetadataBean;
import com.bfd.common.exception.RmcbsException;
import com.bfd.common.vo.Constants;
import com.bfd.common.vo.PageVO;
import com.bfd.dao.mapper.ColumnMapper;
import com.bfd.dao.mapper.DataPackageMapper;
import com.bfd.dao.mapper.MetadataMapper;
import com.bfd.dao.mapper.SequenceMapper;
import com.bfd.enums.EpubStatusEnum;
import com.bfd.enums.FileTypeEnum;
import com.bfd.enums.PdfStatusEnum;
import com.bfd.param.vo.ColumnVO;
import com.bfd.param.vo.DataPackageVO;
import com.bfd.param.vo.MetadataSetVO;
import com.bfd.param.vo.MetadataTermsVO;
import com.bfd.param.vo.MetadataVO;
import com.bfd.service.MetadataService;
import com.bfd.utils.FtpUtils;
import com.bfd.utils.IsbnUtils;
import com.bfd.utils.SpringSecurityUtil;
import com.bfd.xml.ChapterHandler;
import com.bfd.xml.ContentHandler;
import com.bfd.xml.MetaHandler;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * 元数据管理Service实现
 * 
 * @author xile
 * @version [版本号, 2018年7月31日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service
@Slf4j
public class MetadataServiceImpl implements MetadataService {

	private static final String THIRD_DIGIT = "%3d";
	private static final String SIXTH_DIGIT = "%6d";

	public static final String EPUB_FILE_NAME_LIMIT = "limit";

	private static final String PREFIX_CP = "CP";

	public static final Object object = new Object();

	public static final String METADATA_ID = "metadataId";

	public static final String COLUMN_ID = "columnId";

	public static final String SORT = "sort";

	public static final String PACKAGE_ID_LIST = "packageIdList";

	public static final String NEXT_VAL = "nextval";

	public static final String ZERO = "0";

	public static final String NAME = "name";

	public static final String UNIQUE_ID = "uniqueId";

	@Autowired
	MetadataMapper metadataMapper;

	@Autowired
	SequenceMapper sequenceMapper;

	@Autowired
	DataPackageMapper dataPackageMapper;

	@Autowired
	ColumnMapper columnMapper;

	@Autowired
	@Qualifier("transportClient")
	TransportClient transportClient;

	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Value("${rabbitmq.pdfQueueName}")
	private String pdfQueueName;

	@Value("${rabbitmq.epubQueueName}")
	private String epubQueueName;

	@Value("${pdf.dir}")
	private String pdfDir;

	@Value("${epub.dir}")
	private String epubDir;

	@Value("${xml.dir}")
	private String xmlDir;

	@Value("${xml.chapter.indexName}")
	private String chapterIndexName;

	@Value("${xml.chapter.indexType}")
	private String chapterIndexType;

	@Value("${xml.content.indexName}")
	private String contentIndexName;

	@Value("${xml.content.indexType}")
	private String contentIndexType;

	@Value("${online.epub.url}")
	private String onlineEpubUrl;

	// xml目录文件的nodeName
	private static final String CONTENT_NODE = "Content";

	// xml内容文件的nodeName
	private static final String PAGE_NODE = "page";

	@Override
	public void addMetadata(MetadataBean metadata) {
		String uniqueId = "";

		// 先判断ISBN编码是否正确; 如果正确,则根据ISBN生成唯一编码; 如果不正确,则根据出版年份生成唯一编码
		if (IsbnUtils.checkISBN(metadata.getBookIsbn())) {
			log.info("标准的ISBN, 按照ISBN生成唯一标识.");
			String isbnSequenceName = this.getIsbnSequenceName(metadata.getBookIsbn());

			// 按照ISBN号生成uniqueId, 后缀不足3位补零(B_isbnSequenceName_001)
			uniqueId = this.getUniqueId(isbnSequenceName, THIRD_DIGIT);
		} else {
			log.info("不标准的ISBN, 按照出版年份生成唯一标识.");
			String[] datas = metadata.getPublishDate().split("-");
			String yearSequenceName = datas[Constants.ZERO];

			// 按照出版年份生成uniqueId, 后缀不足6位补零(B_yearSequenceName_000001)
			uniqueId = this.getUniqueId(yearSequenceName, SIXTH_DIGIT);
		}

		log.info("generate uniqueId------------->" + uniqueId);
		metadata.setUniqueId(uniqueId);
		metadata.setCreateUser(String.valueOf(SpringSecurityUtil.getCurrentUserId()));
		metadata.setUpdateUser(String.valueOf(SpringSecurityUtil.getCurrentUserId()));
		metadataMapper.addMetadata(metadata);
	}

	/**
	 * 截取ISBN，用于生成唯一标识时使用
	 * 
	 * @param originalIsbn（978-7-01-011904-5或7-01-003640-3）
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	private String getIsbnSequenceName(String originalIsbn) {
		String isbnSequenceName = "";

		// 用"-"拆分出的数组
		String[] isbns = originalIsbn.split("-");

		// 判断数组的大小,看是13位编码的还是10位编码的 5是13位的,4是10位的
		if (isbns.length == Constants.FIVE) {

			// ISBN数组长度为5的取下标[2]、[3]部分（978-7-01-011904-5）
			isbnSequenceName = isbns[Constants.TWO] + isbns[Constants.THREE];
		} else {

			// ISBN数组长度为4,取下标[1]、[2]部分（7-01-003640-3）
			isbnSequenceName = isbns[Constants.ONE] + isbns[Constants.TWO];
		}
		return isbnSequenceName;
	}

	/**
	 * 获取唯一标识
	 * 
	 * @param sequenceName：出版年份或者截出的ISBN号
	 * @param digit:%3d或%6d
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	private String getUniqueId(String sequenceName, String digit) {
		synchronized (object) {
			String uniqueId = "";
			Map<String, Object> param = new HashMap<String, Object>();
			param.put(NAME, sequenceName);

			// 数据库中有元数据是XML里面导进去的, 而t_squence表里面没记录, 不循环可能重复
			do {
				this.sequenceMapper.getNextVal(param);
				BigInteger sequenceId = (BigInteger) param.get(NEXT_VAL);
				if (sequenceId == null) {
					log.info("添加新的SequenceName：" + sequenceName);
					sequenceMapper.addSequenceName(sequenceName);
					this.sequenceMapper.getNextVal(param);
					sequenceId = (BigInteger) param.get(NEXT_VAL);
				}

				// 不足3位或者6位左补0（按照digit）
				String leftPaddingZero = String.format(digit, sequenceId).replace(" ", ZERO);
				StringBuffer uniqueIdbuf = new StringBuffer("B");
				uniqueIdbuf.append("_").append(sequenceName).append("_").append(leftPaddingZero);
				uniqueId = uniqueIdbuf.toString();
			} while (metadataMapper.getMetaByUniqueId(uniqueId) != null);

			return uniqueId;
		}
	}

	@Override
	public void updateMetadata(MetadataBean metadata) {
		metadata.setUpdateUser(String.valueOf(SpringSecurityUtil.getCurrentUserId()));
		metadataMapper.updateMetadata(metadata);
	}

	@Override
	public MetadataBean getMetaByUniqueId(String id) {
		return metadataMapper.getMetaByUniqueId(id);
	}

	@Override
	public PageVO<MetadataBean> getMeta(MetadataVO metadataVo) {
		PageHelper.startPage(metadataVo.getCurrent(), metadataVo.getPageSize());
		if (metadataVo.getSortField() == null || metadataVo.getSortField().equals("")) {
			metadataVo.setSortField("create_time");
		}
		if (metadataVo.getSortType() == null || metadataVo.getSortType().equals("")) {
			metadataVo.setSortType("desc");
		}
		List<MetadataBean> meta = metadataMapper.getMeta(metadataVo);
		PageInfo<MetadataBean> pageInfo = new PageInfo<MetadataBean>(meta);
		return new PageVO<MetadataBean>(metadataVo.getCurrent(), metadataVo.getPageSize(), pageInfo.getTotal(),
				pageInfo.getList());
	}

	/**
	 * 删除元数据 <BR>
	 * 1.删除元数据本身 <BR>
	 * 2.删除元数据与栏目的关系 <BR>
	 * 3.删除元数据与数据包的关系 <BR>
	 * 3.删除元数据与个性化栏目的关系 <BR>
	 * 4.删除元数据与个性化数据包的关系 <BR>
	 * 
	 * @param id
	 */
	// TODO 还未做删除书籍
	@Override
	@Transactional
	public void deleteById(String id) {
		metadataMapper.deleteMetadataById(id);
		metadataMapper.deleteColumnRelationByMetaId(id);
		metadataMapper.deleteDataPackageRelationByMetaId(id);
		metadataMapper.deletePrivateColumnRelationByMetaId(id);
		metadataMapper.deletePrivateDataPackageRelationByMetaId(id);
	}

	@Override
	public String onlineQueryPDF(String uniqueId) throws IOException {
		// 先查单层pdf 拼接路径
		String path = Constants.FTP_ROOT.concat(uniqueId).concat(Constants.SINGLE_LAYER_PDF);
		String result = FtpUtils.existFile(path, uniqueId);
		if (StringUtils.isNotBlank(result)) {
			return result;
		}
		// 先查双层pdf 拼接路径
		path = Constants.FTP_ROOT.concat(uniqueId).concat(Constants.TWO_LAYER_PDF);
		result = FtpUtils.existFile(path, uniqueId);
		if (StringUtils.isNotBlank(result)) {
			return result;
		}
		// 最后查图形pdf 拼接路径
		path = Constants.FTP_ROOT.concat(uniqueId).concat(Constants.IMAGE_PDF);
		result = FtpUtils.existFile(path, uniqueId);
		if (StringUtils.isNotBlank(result)) {
			return result;
		}
		throw new RmcbsException("文件不存在");
	}

	@Override
	public String onlineQueryEpub(String uniqueId) throws IOException {
		return FtpUtils.existFile(Constants.FTP_ROOT.concat(uniqueId).concat(Constants.EPUB), uniqueId);
	}

	@Override
	@Async
	public void onlineUploadPDF(String ftpFileName, String bookId) {
		MetadataBean metadataBean = new MetadataBean();
		metadataBean.setUniqueId(bookId);
		metadataBean.setBookPdf(PdfStatusEnum.UPLOADING.getKey());
		metadataMapper.updateMetadata(metadataBean);
		ftpUpload(pdfDir.concat(bookId), bookId.concat(".pdf"), ftpFileName);
		amqpTemplate.convertAndSend(pdfQueueName, new MessageBean(pdfDir.concat(bookId), bookId.concat(".pdf"), bookId));
	}

	@Override
	@Async
	public void onlineUploadEpub(String fileName, String bookId) {
		MetadataBean metadata = new MetadataBean();
		metadata.setUniqueId(bookId);
		metadata.setBookEpub(EpubStatusEnum.UPLOADED.getKey());
		metadataMapper.updateMetadata(metadata);
		ftpUpload(epubDir.concat("CP").concat(bookId), "limit.epub", fileName);
		amqpTemplate.convertAndSend(epubQueueName, new MessageBean(epubDir.concat(bookId), "limit.epub", bookId));
	}

	private void ftpUpload(String localPath, String localFileName, String ftpPath) {
		// 对传过来的路径拆分出文件路径,与文件名称
		File file = new File(localPath, localFileName);
		// 判断文件父目录是否存在
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdir();
		} else {
			// 重新上传删除之前文件
			for (File childFile : file.getParentFile().listFiles()) {
				childFile.delete();
			}
		}
		// 将Ftp上的文件下载到本地
		FtpUtils.downloadFile(file, ftpPath);
	}

	/**
	 * 添加元数据与栏目的关系
	 * 
	 * @param metadataId
	 * @param columnIdList
	 */
	@Override
	@Transactional
	public void addColumnRelation(Long metadataId, List<Long> columnIdList) {

		// 如果packageIdList为空，则删除该书与数据包的关系
		if (CollectionUtils.isEmpty(columnIdList)) {
			metadataMapper.deleteColumnRelationByMetadataId(metadataId);
		} else {

			// 先根据metadataId查出所有相关的columnId
			List<Long> currentColumnList = metadataMapper.getColumnIdListByMetadataId(metadataId);

			// 遍历之前的columnId, 将不包含在传入的columnIdList的columnId删除
			if (!CollectionUtils.isEmpty(currentColumnList)) {
				Map<String, Object> delParam = new HashMap<String, Object>(16);
				for (Long columnId : currentColumnList) {
					if (!columnIdList.contains(columnId)) {
						delParam.put(METADATA_ID, metadataId);
						delParam.put(COLUMN_ID, columnId);

						// 删除
						metadataMapper.deleteColumnMetaRelation(delParam);
					}
				}

				// 查出新增关系不在当前关系中的columnId, 进行添加
				for (Long columnId : columnIdList) {
					if (!currentColumnList.contains(columnId)) {
						this.addColumnBookRelation(columnId, metadataId);
					}
				}
			}
			// 该书没有任何栏目的情况下, 直接执行先更新分类下书籍的排序, 然后添加书籍
			else {
				for (Long columnId : columnIdList) {
					this.addColumnBookRelation(columnId, metadataId);
				}
			}
		}
	}

	/**
	 * 添加元数据与栏目的关系
	 * 
	 * @param columnId
	 * @param metadataId
	 * @see [类、类#方法、类#成员]
	 */
	private void addColumnBookRelation(Long columnId, Long metadataId) {
		Map<String, Object> addParam = new HashMap<String, Object>(16);

		// 先更新该栏目下书籍的sort = sort + 1
//		metadataMapper.updateColumnRelationByColumnId(columnId);

		// 然后添加，将新书添加到该分类下
		addParam.put(METADATA_ID, metadataId);
		addParam.put(COLUMN_ID, columnId);
		addParam.put(SORT, -1);
		metadataMapper.addColumnRelation(addParam);
	}

	@Override
	@Transactional
	public void addDataPakcageRelation(Long metadataId, List<Long> packageIdList) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(METADATA_ID, metadataId);

		// 如果packageIdList为空，则删除该书与数据包的关系
		if (CollectionUtils.isEmpty(packageIdList)) {
			metadataMapper.deletePackageRelationByMetadataId(metadataId);
		} else {

			// 先根据metadataId查出所有相关的packageId
			List<Long> currentPackageIdList = metadataMapper.getPackageIdListByMetadataId(metadataId);

			// 遍历之前的packageId, 将不包含在传入的packageIdList的packageId删除
			if (!CollectionUtils.isEmpty(currentPackageIdList)) {
				for (Long packageId : currentPackageIdList) {
					if (!packageIdList.contains(packageId)) {
						DataPackgeBookBean bean = new DataPackgeBookBean(packageId, metadataId);

						// 删除
						metadataMapper.deletePackageMetaRelation(bean);
					}
				}

				// 查出新增关系不在当前关系中的packageId, 进行添加
				List<Long> addList = new ArrayList<Long>(10);
				for (Long packageId : packageIdList) {
					if (!currentPackageIdList.contains(packageId)) {
						addList.add(packageId);
					}
				}

				if (!CollectionUtils.isEmpty(addList)) {
					param.put(PACKAGE_ID_LIST, addList);
					metadataMapper.addDataPakcageRelation(param);
				}

			} else {
				param.put(PACKAGE_ID_LIST, packageIdList);
				metadataMapper.addDataPakcageRelation(param);
			}
		}
	}

	// @Override
	// public Map<String, String> getUniqueId() {
	// synchronized (object) {
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
	// String year = sdf.format(new Date());
	// Map<String, Object> param = new HashMap<String, Object>();
	// param.put(NAME, year);
	//
	// this.sequenceMapper.getNextVal(param);
	// BigInteger uniqueId = (BigInteger) param.get(NEXT_VAL);
	// if (uniqueId == null) {
	// log.info("添加新的一年的sequence");
	// sequenceMapper.addSequenceName(year);
	// this.sequenceMapper.getNextVal(param);
	// uniqueId = (BigInteger) param.get(NEXT_VAL);
	// }
	//
	// // 不足6位左补0
	// String leftPaddingZero = String.format("%6d", uniqueId).replace(" ", ZERO);
	// StringBuffer uniqueIdbuf = new StringBuffer("B");
	// uniqueIdbuf.append("_").append(year).append("_").append(leftPaddingZero);
	//
	// Map<String, String> result = new HashMap<String, String>();
	// result.put(UNIQUE_ID, uniqueIdbuf.toString());
	// return result;
	// }
	// }

	@Override
	public PageVO<MetadataSetVO> getBookOptionsList(MetadataTermsVO metadataTermsVO) {
		PageHelper.startPage(metadataTermsVO.getCurrent(), metadataTermsVO.getPageSize());

		if (metadataTermsVO.getSortField() == null && metadataTermsVO.getSortField().equals("")) {
			metadataTermsVO.setSortField("create_time");
		}
		if (metadataTermsVO.getSortType() == null && metadataTermsVO.getSortType().equals("")) {
			metadataTermsVO.setSortType("desc");
		}
		// 获取图书的基本信息
		List<MetadataSetVO> meta = metadataMapper.getBookOptionsList(metadataTermsVO);
		// 判断集合是否为空,不为空才往里面填数据
		if (meta != null && meta.size() != Constants.ZERO) {
			// 根据图书ID获取其栏目
			List<ColumnBean> columnBeans = metadataMapper.getColumnNames(meta);
			// 根据图书ID获取其数据包
			List<DataPackageBean> packageBeans = metadataMapper.getPackageNames(meta);
			// 遍历图书集合，将数据包，栏目塞进去
			for (MetadataSetVO metadataSetVO : meta) {
				// 遍历栏目集合，对比ID
				for (ColumnBean columnBean : columnBeans) {
					if (columnBean.getId() == metadataSetVO.getId()) {
						List<ColumnBean> columnList = metadataSetVO.getColumnList();
						columnList.add(columnBean);
						metadataSetVO.setColumnList(columnList);
					}
				}
				// 遍历数据包集合,对比ID
				for (DataPackageBean dataPackageBean : packageBeans) {
					if (dataPackageBean.getId() == metadataSetVO.getId()) {
						List<DataPackageBean> packageList = metadataSetVO.getPackageList();
						packageList.add(dataPackageBean);
						metadataSetVO.setPackageList(packageList);
					}
				}
			}

		}
		PageInfo<MetadataSetVO> pageInfo = new PageInfo<MetadataSetVO>(meta);
		return new PageVO<MetadataSetVO>(metadataTermsVO.getCurrent(), metadataTermsVO.getPageSize(), pageInfo.getTotal(),
				pageInfo.getList());
	}

	@Override
	@Async
	public void uploadPdf(byte[] bytes, String bookId, String suffix) {
		// 状态更新为上传中
		MetadataBean metadataBean = new MetadataBean();
		metadataBean.setUniqueId(bookId);
		metadataBean.setBookPdf(PdfStatusEnum.UPLOADING.getKey());
		metadataMapper.updateMetadata(metadataBean);
		String fileDir = pdfDir + bookId;
		String fileName = bookId + suffix;
		File dest = new File(fileDir, fileName);
		// 判断文件父目录是否存在
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdir();
		} else {
			// 重新上传删除之前文件
			for (File childFile : dest.getParentFile().listFiles()) {
				childFile.delete();
			}
		}
		OutputStream output = null;
		BufferedOutputStream bufferedOutput = null;
		try {
			output = new FileOutputStream(dest);
			bufferedOutput = new BufferedOutputStream(output);
			bufferedOutput.write(bytes);
			amqpTemplate.convertAndSend(pdfQueueName, new MessageBean(fileDir, fileName, bookId));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedOutput != null) {
				try {
					bufferedOutput.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void uploadXmlChapter(MultipartFile file, String bookId) {
		MetadataBean metadataBean = this.getMetaByUniqueId(bookId);
		if (metadataBean == null) {
			throw new RmcbsException(String.format("%s书籍不存在", bookId));
		}

		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		if (!FileTypeEnum.XML.getKey().equals(suffix)) {
			throw new RmcbsException("文件格式错误，只能是xml格式！");
		}

		File xmlDirectory = new File(xmlDir, bookId);
		log.info(String.format("%s的xml文件夹为:%s", bookId, xmlDirectory.getAbsolutePath()));

		if (!xmlDirectory.exists()) {
			log.info(String.format("%s的xml文件夹%s不存在", bookId, xmlDirectory.getAbsolutePath()));
			xmlDirectory.mkdirs();
		}

		String fileName = bookId + "_chapter" + suffix;
		File xmlFile = new File(xmlDir, fileName);

		// 是否是重新上传
		if (xmlFile.exists()) {
			log.info(String.format("重新上传%s的xml章节,删除索引文档", bookId));

			// 删除索引文档
			DeleteQuery deleteQuery = new DeleteQuery();
			BoolQueryBuilder query = QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("bookId", bookId));
			log.info(String.format("删除章节DSL:%s", query));
			deleteQuery.setIndex(chapterIndexName);
			deleteQuery.setType(chapterIndexType);
			deleteQuery.setQuery(query);
			elasticsearchTemplate.delete(deleteQuery);

			// 删除文件
			xmlFile.delete();
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(xmlFile);
			out.write(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		log.info(String.format("%s的xml章节已上传完成,文件位置为:%s,开始对xml章节进行索引", bookId, xmlFile.getAbsolutePath()));

		// 添加索引
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser parser = spf.newSAXParser();
					ChapterHandler handler = new ChapterHandler(CONTENT_NODE, bookId, transportClient, chapterIndexName,
							chapterIndexType, metadataMapper);
					parser.parse(new FileInputStream(xmlFile), handler);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void uploadXmlContent(MultipartFile file, String bookId) {
		MetadataBean metadataBean = this.getMetaByUniqueId(bookId);
		if (metadataBean == null) {
			throw new RmcbsException(String.format("%s书籍不存在", bookId));
		}

		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		if (!FileTypeEnum.XML.getKey().equals(suffix)) {
			throw new RmcbsException("文件格式错误，只能是xml格式！");
		}

		File xmlDirectory = new File(xmlDir);
		log.info(String.format("%s的xml文件夹为:", bookId, xmlDirectory.getAbsolutePath()));

		if (!xmlDirectory.exists()) {
			log.info(String.format("%s的xml文件夹%s不存在", bookId, xmlDirectory.getAbsolutePath()));
			xmlDirectory.mkdirs();
		}

		String fileName = bookId + "_content" + suffix;
		File xmlFile = new File(xmlDir, fileName);
		if (xmlFile.exists()) {
			log.info(String.format("重新上传%s的xml内容,删除索引文档", bookId));

			// 删除索引
			DeleteQuery deleteQuery = new DeleteQuery();
			BoolQueryBuilder query = QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("bookId", bookId));
			log.info(String.format("删除内容DSL:%s", query));
			deleteQuery.setIndex(contentIndexName);
			deleteQuery.setType(contentIndexType);
			deleteQuery.setQuery(query);
			elasticsearchTemplate.delete(deleteQuery);

			// 删除文件
			xmlFile.delete();
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(xmlFile);
			out.write(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		log.info(String.format("%s的xml内容已上传,文件位置为:%s,开始对xml内容进行索引", bookId, xmlFile.getAbsolutePath()));
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser parser = spf.newSAXParser();
					ContentHandler handler = new ContentHandler(PAGE_NODE, bookId, transportClient, contentIndexName,
							contentIndexType, metadataMapper);
					parser.parse(new FileInputStream(xmlFile), handler);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void uploadEpub(MultipartFile file, String bookId) {

		MetadataBean metadataBean = this.getMetaByUniqueId(bookId);
		if (metadataBean == null) {
			throw new RmcbsException(String.format("%s书籍不存在", bookId));
		}

		if (EpubStatusEnum.ENCRYPTING.getKey().equals(metadataBean.getBookEpub())) {
			throw new RmcbsException(String.format("%s书籍正在加密中，不能重新上传", bookId));
		}

		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		if (!FileTypeEnum.EPUB.getKey().equals(suffix)) {
			throw new RmcbsException("文件格式错误，只能是epub格式！");
		}

		File epubDirectory = new File(epubDir);
		if (!epubDirectory.exists()) {
			epubDirectory.mkdirs();
		}

		// 目录：CP唯一标识
		String cpDirectoryStr = PREFIX_CP + bookId;
		File cpDirectory = new File(epubDirectory, cpDirectoryStr);
		if (!cpDirectory.exists()) {
			cpDirectory.mkdir();
		}

		// 文件名称为limit.epub
		String fileName = EPUB_FILE_NAME_LIMIT + suffix;
		File epubFile = new File(cpDirectory, fileName);

		// 是否是重新上传
		if (epubFile.exists()) {

			// 删除文件
			epubFile.delete();
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(epubFile);
			out.write(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		// 更新为上传
		MetadataBean metadata = new MetadataBean();
		metadata.setUniqueId(bookId);
		metadata.setBookEpub(EpubStatusEnum.UPLOADED.getKey());
		metadataMapper.updateMetadata(metadata);

		amqpTemplate.convertAndSend(epubQueueName, new MessageBean(null, null, bookId));
	}

	@Override
	public List<DataPackageVO> getPackageList(long metadataId) {
		List<DataPackageVO> list = metadataMapper.getPackageList();
		List<DataPackageVO> listOfPackage = metadataMapper.getPackageListByMetadataId(metadataId);
		for (int i = 0; i < list.size(); i++) {
			if (listOfPackage.contains(list.get(i))) {
				list.get(i).setChecked(1);
			} else {
				list.get(i).setChecked(0);
			}
		}

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getParentId() == 0) {
				List<DataPackageBean> beanList = dataPackageMapper.getPackageListByParentId(list.get(i).getId());
				int sum = 0;
				for (int j = 0; j < beanList.size(); j++) {
					int num = dataPackageMapper.getBookNumber(beanList.get(j).getId());
					sum += num;
				}
				list.get(i).setBookNumber(sum);
			} else {
				list.get(i).setBookNumber(dataPackageMapper.getBookNumber(list.get(i).getId()));
			}
		}
		return list;
	}

	@Override
	public List<ColumnVO> getColumnList(long metadataId) {
		List<ColumnVO> list = metadataMapper.getColumnList();
		List<ColumnVO> listOfColumn = metadataMapper.getColumnListByMetadataId(metadataId);
		for (int i = 0; i < list.size(); i++) {
			if (listOfColumn.contains(list.get(i))) {
				list.get(i).setChecked(1);
			} else {
				list.get(i).setChecked(0);
			}
		}

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getParentId() == 0) {
				List<ColumnBean> beanList = columnMapper.getColumnListByParentId(list.get(i).getId());
				int sum = 0;
				for (int j = 0; j < beanList.size(); j++) {
					int num = columnMapper.getBookNumber(beanList.get(j).getId());
					sum += num;
				}
				list.get(i).setBookNumber(sum);
			} else {
				list.get(i).setBookNumber(columnMapper.getBookNumber(list.get(i).getId()));
			}
		}
		return list;
	}

	@Override
	public List<MetadataBean> getMetaDataList(List<Integer> bookIds) {
		List<MetadataBean> metaList = metadataMapper.getMetaDataList(bookIds);
		return metaList;
	}

	@Override
	public void uploadXmlMeta(MultipartFile file) {
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		if (!FileTypeEnum.XML.getKey().equals(suffix)) {
			throw new RmcbsException("文件格式错误，只能是xml格式！");
		}
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parser = spf.newSAXParser();
			MetaHandler handler = new MetaHandler("PROPERTIES", metadataMapper);
			parser.parse(file.getInputStream(), handler);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RmcbsException(e.getMessage());
		}

	}

	@Override
	public void sendPdfMq(Integer num) {
		MetadataVO metadataVO = new MetadataVO();
		metadataVO.setBookPdf("2");
		metadataVO.setPageSize(num);
		List<MetadataBean> list = metadataMapper.getMetaLimit(metadataVO);
		for(MetadataBean metadataBean: list){
			String bookId = metadataBean.getUniqueId();
			amqpTemplate.convertAndSend(pdfQueueName, new MessageBean(pdfDir.concat(metadataBean.getUniqueId()), bookId.concat(".pdf"), bookId));
		}
	}

}
