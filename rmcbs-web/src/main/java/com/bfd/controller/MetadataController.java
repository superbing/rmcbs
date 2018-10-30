package com.bfd.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.pdfbox.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bfd.bean.MetadataBean;
import com.bfd.common.exception.RmcbsException;
import com.bfd.common.vo.PageVO;
import com.bfd.common.vo.Result;
import com.bfd.enums.FileTypeEnum;
import com.bfd.enums.PdfStatusEnum;
import com.bfd.param.vo.MetadataSetVO;
import com.bfd.param.vo.MetadataTermsVO;
import com.bfd.param.vo.MetadataVO;
import com.bfd.service.MetadataService;
import com.bfd.utils.IsbnUtils;
import com.bfd.utils.ValidatorUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 元数据添加、删除、编辑、打分类
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年7月25日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RestController
@RequestMapping(value = "/metadata", produces = { "application/json;charset=UTF-8" })
@Api(value = "元数据", consumes = "application/x-www-form-urlencoded", description = "元数据")
public class MetadataController {

	@Autowired
	MetadataService metadataService;

	/**
	 * 添加元数据
	 * 
	 * @param metadata
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@PostMapping(value = "/add")
	@ApiOperation(value = "添加元数据")
	public Object add(@RequestBody MetadataBean metadata) {
		ValidatorUtils.validateEntity(metadata);
		metadataService.addMetadata(metadata);
		return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "");
	}

	/**
	 * 编辑元数据
	 * 
	 * @param metadata
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@PostMapping(value = "/update")
	@ApiOperation(value = "编辑元数据")
	@Valid()
	public Object update(@RequestBody MetadataBean metadata) {
		ValidatorUtils.validateEntity(metadata);
		metadataService.updateMetadata(metadata);
		return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "");
	}

	/**
	 * 根据id查询元数据
	 * 
	 * @param id
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@GetMapping(value = "/get{id}")
	@ApiOperation(value = "根据唯一标识(uniqueId)查询元数据")
	public Object getMetaById(@PathVariable("id") String id) {
		MetadataBean metadata = metadataService.getMetaByUniqueId(id);
		return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), metadata);
	}

	/**
	 * 校验ISBN编码
	 * 
	 * @param isbn
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@GetMapping(value = "/checkIsbn/{isbn}")
	@ApiOperation(value = "校验ISBN编码")
	public Object checkIsbn(@PathVariable("isbn") String isbn) {
		return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), IsbnUtils.checkISBN(isbn));
	}

	/**
	 * 查询元数据
	 * 
	 * @param metadataVo
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@PostMapping(value = "/getMeta")
	@ApiOperation(value = "查询元数据")
	public Object getMeta(@RequestBody MetadataVO metadataVo) {
		ValidatorUtils.validateEntity(metadataVo);
		PageVO<MetadataBean> pageVo = metadataService.getMeta(metadataVo);
		return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), pageVo);
	}

	@PostMapping(value = "/getBookOptionsList")
	@ApiOperation(value = "获取图书设置展示界面")
	public Object getBookOptionsList(@RequestBody MetadataTermsVO metadataTermsVO) {
		ValidatorUtils.validateEntity(metadataTermsVO);
		PageVO<MetadataSetVO> pageVo = metadataService.getBookOptionsList(metadataTermsVO);
		return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), pageVo);
	}

	/**
	 * 根据id删除元数据
	 * 
	 * @param id
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	// TODO 1.删除时，必须将书与分类的关系删除
	// TODO 2.删除时，是否需要删除已上传的书籍
	@DeleteMapping(value = "/delete{id}")
	@ApiOperation(value = "根据唯一标识(uniqueId)删除元数据")
	public Object deleteMetaById(@PathVariable("id") String id) {
		metadataService.deleteById(id);
		return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "");
	}

	/**
	 * 为元数据添加栏目
	 * 
	 * @param metadataId
	 * @param columnIdList
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@PostMapping("/addColumnRelation")
	@ApiOperation(value = "为元数据添加栏目")
	public Object addColumnRelation(@RequestParam Long metadataId, @RequestParam List<Long> columnIdList) {
		metadataService.addColumnRelation(metadataId, columnIdList);
		return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "");
	}

	/**
	 * 为元数据添加数据包
	 * 
	 * @param metadataId
	 * @param packageIdList
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@PostMapping("/addDataPakcageRelation")
	@ApiOperation(value = "为元数据添加数据包")
	public Object addDataPakcageRelation(@RequestParam Long metadataId, @RequestParam List<Long> packageIdList) {
		metadataService.addDataPakcageRelation(metadataId, packageIdList);
		return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "");
	}

	// @GetMapping("/getUniqueId")
	// @ApiOperation(value = "获取元数据唯一标识")
	// public Object getUniqueId() {
	// Map<String, String> uniqueIdMap = metadataService.getUniqueId();
	// return new Result<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), uniqueIdMap);
	// }

	@PostMapping(value = "/uploadPdf")
	@ApiOperation(value = "上传pdf")
	public Result<Object> uploadPdf(@RequestParam MultipartFile file, @RequestParam String bookId) throws IOException {
		// 获得文件后缀名
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		if (!FileTypeEnum.PDF.getKey().equals(suffix)) {
			throw new RmcbsException("文件格式错误");
		}

		MetadataBean metadataBean = metadataService.getMetaByUniqueId(bookId);
		if (metadataBean == null) {
			throw new RmcbsException("文档编号不存在！");
		}
		if (PdfStatusEnum.UPLOADING.getKey().equals(metadataBean.getBookPdf())) {
			throw new RmcbsException("文档正在解析中！");
		}
		metadataService.uploadPdf(IOUtils.toByteArray(file.getInputStream()), bookId, suffix);
		return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), true);
	}

	@PostMapping(value = "/uploadXmlChapter")
	@ApiOperation(value = "上传xml目录")
	public Result<Object> uploadXmlChapter(@RequestParam MultipartFile file, @RequestParam String bookId) {
		metadataService.uploadXmlChapter(file, bookId);
		return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null);
	}

	@PostMapping(value = "/uploadXmlContent")
	@ApiOperation(value = "上传xml内容")
	public Result<Object> uploadXmlContent(@RequestParam MultipartFile file, @RequestParam String bookId) {
		metadataService.uploadXmlContent(file, bookId);
		return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null);
	}

	@PostMapping(value = "/uploadEpub")
	@ApiOperation(value = "上传epub")
	public Result<Object> uploadEpub(@RequestParam MultipartFile file, @RequestParam String bookId) {
		metadataService.uploadEpub(file, bookId);
		return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null);
	}

	@ApiOperation(value = "获取数据包分级树结构(包含已勾选状态)")
	@RequestMapping(value = "/getPackageList", method = RequestMethod.GET)
	public Object getPackageList(@ApiParam("元数据id") @RequestParam long metadataId) {
		return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), metadataService.getPackageList(metadataId));
	}

	@ApiOperation(value = "获取栏目分级树结构(包含已勾选状态)")
	@RequestMapping(value = "/getColumnList", method = RequestMethod.GET)
	public Object getColumnList(@ApiParam("元数据id") @RequestParam long metadataId) {
		return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), metadataService.getColumnList(metadataId));
	}

	@GetMapping(value = "/exportMeta")
	@ApiOperation(value = "导出元数据")
	public void exportMeta(HttpServletResponse response, @RequestParam List<Integer> bookIds) {
		List<MetadataBean> metaList = metadataService.getMetaDataList(bookIds);
		if (metaList.isEmpty()) {
			throw new RmcbsException("未查询到元数据！");
		}
		//
		else {
			XMLWriter writer = null;
			OutputStream out = null;
			try {

				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setEncoding("UTF-8");
				writer = new XMLWriter(format);

				// 创建根
				Element root = DocumentHelper.createElement("MetaViewDataS");
				Document document = DocumentHelper.createDocument(root);

				for (MetadataBean meta : metaList) {
					Element metaViewDataElement = root.addElement("MetaViewData");
					metaViewDataElement.addAttribute("Version", "6.0");
					Element propertiesElement = metaViewDataElement.addElement("PROPERTIES");

					propertiesElement.addElement("CHNLNAME").addCDATA("图书基本信息");
					propertiesElement.addElement("RESOURCEUNIQUEID").addCDATA(meta.getUniqueId());
					propertiesElement.addElement("TITLE").addCDATA(meta.getBookName());
					propertiesElement.addElement("CREATOR").addCDATA(meta.getAuthor());
					propertiesElement.addElement("ISBN").addCDATA(meta.getBookIsbn());
					propertiesElement.addElement("KEYWORDS").addCDATA(meta.getKeywords());
					Element publisher = propertiesElement.addElement("PUBLISHER").addCDATA(meta.getPress());
					publisher.addAttribute("RealValue", meta.getPress());

					// 装帧方式
					Element frameType = propertiesElement.addElement("FRAMETYPE").addCDATA(meta.getBindingType());
					frameType.addAttribute("RealValue", meta.getBindingType());

					// 初版时间
					propertiesElement.addElement("PUBDATE").addCDATA(meta.getPublishDate());

					// 开本、开本1、开本2
					propertiesElement.addElement("FORMAT").addCDATA(meta.getBookSize());
					propertiesElement.addElement("FORMATSIZE1").addCDATA(meta.getBookSizeOne());
					propertiesElement.addElement("FORMATSIZE2").addCDATA(meta.getBookSizeTwo());

					// 制作者、制作时间
					propertiesElement.addElement("CRUSER").addCDATA(meta.getBookMaker());
					propertiesElement.addElement("CRTIME").addCDATA(meta.getMakeTime());

					// 中图分类、语种
					propertiesElement.addElement("TYPE").addCDATA(meta.getBookCategory());
					propertiesElement.addElement("LANGUAGE").addCDATA(meta.getLanguage());

					// 版次、版次年、月
					propertiesElement.addElement("PUBCOUNT").addCDATA(meta.getEditOrder());
					propertiesElement.addElement("PRINTCOUNTYEAR").addCDATA(meta.getEditOrderYear());
					propertiesElement.addElement("PRINTCOUNTMONTH").addCDATA(meta.getEditOrderMonth());

					// 增加空标签
					metaViewDataElement.addElement("WCMAPPENDIXS");
				}
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				String dateStr = df.format(date);
				long dateTemp = date.getTime();
				String fileName = URLEncoder.encode(dateStr + "_" + dateTemp, "UTF-8");
				response.setContentType("application/xml;charset=UTF-8");
				response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xml");
				out = response.getOutputStream();
				writer.setOutputStream(out);
				writer.write(document);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != out) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@PostMapping(value = "/uploadXmlMeta")
	@ApiOperation(value = "上传xml元数据")
	public Result<Object> uploadXmlMeta(@RequestParam MultipartFile file) {
		metadataService.uploadXmlMeta(file);
		return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null);
	}

	@GetMapping(value = "/onlineQueryPDF")
	@ApiOperation(value = "在线查询,判断pdf是否存在")
	public Result<Object> onlineQueryPDF(@RequestParam String uniqueId) throws IOException {
		return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), metadataService.onlineQueryPDF(uniqueId));
	}

	@GetMapping(value = "/onlineQueryEpub")
	@ApiOperation(value = "在线查询,判断Epub是否存在")
	public Result<Object> onlineQueryEpub(@RequestParam String uniqueId) throws IOException {
		return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), metadataService.onlineQueryEpub(uniqueId));
	}

	@GetMapping(value = "/onlineUploadPDF")
	@ApiOperation(value = "在线上传pdf")
	public Result<Object> onlineUploadPDF(@RequestParam String fileName, @RequestParam String bookId) {
		metadataService.onlineUploadPDF(fileName, bookId);
		return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), true);
	}

	@GetMapping(value = "/onlineUploadEpub")
	@ApiOperation(value = "在线上传Epub")
	public Result<Object> onlineUploadEpub(@RequestParam String fileName, @RequestParam String bookId) {
		metadataService.onlineUploadEpub(fileName, bookId);
		return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), true);
	}

	@GetMapping(value = "/pdfSendMq")
	@ApiOperation(value = "将加密中的PDF再次发送至MQ")
	public Result<Object> pdfSendMq(Integer num){
		metadataService.sendPdfMq(num);
		return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),true);
	}

}
