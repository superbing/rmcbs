package com.bfd.mq;

import com.alibaba.fastjson.JSONArray;
import com.bfd.bean.MessageBean;
import com.bfd.bean.MetadataBean;
import com.bfd.dao.mapper.MetadataMapper;
import com.bfd.enums.EpubStatusEnum;
import com.bfd.enums.PdfStatusEnum;
import com.bfd.service.ExtractService;
import com.bfd.utils.HttpTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author: bing.shen
 * @date: 2018/9/4 15:11
 * @Description:
 */
@Component
@Slf4j
public class RabbitMqReceiver {
    
    @Value("${online.epub.url}")
    private String onlineEpubUrl;

    @Autowired
    private MetadataMapper metadataMapper;

    @Autowired
    private ExtractService extractService;

    /**
     * 最多同时解析20本PDF
     */
    private static Semaphore semaphore = new Semaphore(20, true);

    @RabbitListener(queues="pdf-img")
    public void receivePdf(MessageBean messageBean){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            String bookId = messageBean.getBookId();
            log.info("开始抽取bookId=" + bookId);
            String fileDir = messageBean.getFileDir().concat("//");
            int pageNum;
            try {
                semaphore.acquire();
                pageNum = extractService.done(bookId, fileDir + messageBean.getFileName(),
                        fileDir, fileDir, fileDir, 120);
                MetadataBean metadataBean = new MetadataBean();
                metadataBean.setUniqueId(bookId);
                metadataBean.setPageNumPdf(pageNum);
                metadataBean.setBookPdf(PdfStatusEnum.UPLOAD.getKey());
                metadataMapper.updateMetadata(metadataBean);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("抽取失败bookId=" + bookId);
            } finally {
                semaphore.release();
                executorService.shutdown();
            }
            log.info("结束抽取bookId=" + bookId);
        });
    }

    @RabbitListener(queues="epub-encrypt")
    public void receiveEpub(MessageBean messageBean) {
        String bookId = messageBean.getBookId();
        log.info("开始加密EPUB,bookId=" + bookId);
        // 调用在线加密接口
        HttpTool httpTool = new HttpTool();
        Map<String, String> param = new HashMap<>(5);
        List<String> docIds = new ArrayList<>();
        docIds.add(bookId);
        param.put("docIds", JSONArray.toJSONString(docIds));
        httpTool.post(this.onlineEpubUrl, param);

        MetadataBean metadataBean = new MetadataBean();
        metadataBean.setUniqueId(bookId);
        metadataBean.setBookEpub(EpubStatusEnum.ENCRYPTING.getKey());
        metadataMapper.updateMetadata(metadataBean);
        log.info("结束加密EPUB,bookId=" + bookId);
    }
}
