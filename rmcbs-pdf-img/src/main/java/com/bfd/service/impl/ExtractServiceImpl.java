package com.bfd.service.impl;

import com.alibaba.fastjson.JSON;
import com.bfd.bean.XmlChapter;
import com.bfd.bean.XmlContent;
import com.bfd.service.ExtractService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.PageDrawer;
import org.apache.pdfbox.rendering.PageDrawerParameters;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author: bing.shen
 * @date: 2018/9/19 15:42
 * @Description:
 */
@Service
@Slf4j
public class ExtractServiceImpl implements ExtractService{
    
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Value("${xml.chapter.indexName}")
    private String chapterIndexName;

    @Value("${xml.chapter.indexType}")
    private String chapterIndexType;

    @Value("${xml.content.indexName}")
    private String contentIndexName;

    @Value("${xml.content.indexType}")
    private String contentIndexType;

    private final static String v = "0.5f";

    private final int level = 1;

    @Autowired
    private BulkProcessor bulkProcessor;

    @Override
    public int done(String bookId, String pdfFilename, String jpgFilename, String jsFileName, String txtFileName, int dpi)
            throws Exception {
        int p = 1;
        PDDocument document;
        float qulity =0.5f;
        try {
            qulity = Float.valueOf(v);
        }catch (Exception e){qulity =0.5f;}

        File pdfFile = new File(pdfFilename);
        document = PDDocument.load(pdfFile);
        //书签抽取
        PDDocumentOutline outline = document.getDocumentCatalog().getDocumentOutline();
        if(outline != null)
        {
            //清空书签ES
            this.deleteIndex(bookId);
            //上传书签ES
            bookMark(outline, level, bookId);
        }

        log.debug("processing" + jpgFilename );
        ExtractServiceImpl.MyPDFRender pdfRenderer = new ExtractServiceImpl.MyPDFRender(document);

        int pagescount=document.getNumberOfPages();
        BufferedImage bim;
        int errorCount =0;

        for (int page = 0; page < pagescount; ++page) {
            FileOutputStream fop =null;
            if (page % 10 ==0 ){
                System.gc();
                System.out.print("System gc end." + page +" of " + pagescount + "\r");
            }
            try {
                bim= pdfRenderer.renderImageWithDPI(page, dpi, ImageType.RGB);
                File jpgFile = new File(jpgFilename + "(" + p + ").jpg");
                fop = new FileOutputStream(jpgFile);
                ImageIOUtil.writeImage(bim, "jpg", fop, dpi, qulity);
                pdfRenderer.CreateJs(jsFileName + "(" + p + ").js", txtFileName + "(" + p + ").txt", bookId, p);
                p++;

            } catch (Throwable e) {
                // 这里必须用Throwable ，否则无法捕捉到pdfbox的异常
                errorCount++;
                log.info("error " + pdfFilename + " "+e.getMessage());
                log.debug("error " + pdfFilename + " "+e.getMessage() );
            } finally{
                fop.close();
                bim =null;
            }
        }
        document.close();
        if (errorCount>=p-1){
            throw new Exception("Error on pdfrender");
        }
        log.debug("process" + jpgFilename + " finished.");
        return p-1;
    }

    private class MyPDFRender extends PDFRenderer {
        private PageDrawer myDrawer = null;
        MyPDFRender(PDDocument document) {
            super(document);
        }
        @Override
        protected PageDrawer createPageDrawer(PageDrawerParameters parameters) throws IOException {
            myDrawer = new ExtractServiceImpl.MyPageDrawer(parameters);
            return myDrawer;
        }

        public void CreateJs(String fp2, String fp, String bookId, int pageNum) throws IOException {
            ((ExtractServiceImpl.MyPageDrawer) myDrawer).getJs(fp2, fp, bookId, pageNum);
        }
    }

    private class LineRe {
        private float CountedHeight = 0;
        private int heightAvgCount = 0;

        public float getHeight() {
            if (heightAvgCount == 0){
                return 0;
            }
            return CountedHeight / heightAvgCount;
        }

        public void addHeight(float h) {
            CountedHeight += h;
            heightAvgCount++;
        }

        public Map<Float, String> getRow() {
            return Row;
        }

        public void setRow(Map<Float, String> row) {
            Row = row;
        }

        private Map<Float, String> Row;
    }

    private class MyPageDrawer extends PageDrawer {

        private Map<Float, ExtractServiceImpl.LineRe> mapLines = new TreeMap<Float, ExtractServiceImpl.LineRe>(new Comparator<Float>() {
            @Override
            public int compare(Float obj1, Float obj2) {
                // 降序排序
                return obj1.compareTo(obj2);
            }
        });

        public void getJs(String fp2, String fp, String bookId, int pageNum) throws IOException {

            StringBuilder s1 =new StringBuilder("var lines=[");
            StringBuilder s3 = new StringBuilder("var linesHeight=[");
            StringBuilder s2 = new StringBuilder("var lineAr=[];\r\n");

            StringBuilder text = new StringBuilder();
            StringBuilder txt = new StringBuilder();
            int k = 0;
            int linesCount = mapLines.keySet().size();

            for (Map.Entry<Float, ExtractServiceImpl.LineRe> entryL: mapLines.entrySet()) {
                Float y = entryL.getKey();
                ExtractServiceImpl.LineRe row = entryL.getValue();

                if (k == linesCount - 1) {
                    s1.append(y);
                    s3.append(row.getHeight()) ;
                } else {
                    s1.append(y + ",") ;
                    s3.append(row.getHeight() + ",") ;
                }

                s2.append("lineAr[" + k + "]=[") ;

                Map<Float, String> rowText = row.getRow();
                int j = 0;

                int rowStringCount = rowText.keySet().size();

                for (Map.Entry<Float, String> entry: rowText.entrySet()) {

                    Float x = entry.getKey();
                    String Text = entry.getValue();

                    if (j == rowStringCount - 1) {
                        s2.append("[" + x + ",'" + Text + "']");

                    } else {
                        s2.append("[" + x + ",'" + Text + "']" + ",");
                    }
                    text.append(Text);
                    txt.append(Text);
                    j++;
                }

                text.append("\r\n");
                s2.append("];\r\n");
                k++;
            }
            s1.append( "];") ;
            s3.append("];\r\n");
            File f = new File(fp2);
            if (!f.exists()) {
                f.createNewFile();
            }
            FileWriter fw = new FileWriter(f);
            fw.write(s1.append("\r\n").append(s3).append("\r\n").append(s2).toString());
            fw.flush();
            fw.close();

            f = new File(fp);
            if (!f.exists()) {
                f.createNewFile();
            }
            fw = new FileWriter(f);
            fw.write(text.toString());
            fw.flush();
            fw.close();
            mapLines =null;

            if(StringUtils.isNotBlank(txt.toString().replace(" ", ""))){
                //书签插入ES
                XmlContent xmlContent = new XmlContent();
                xmlContent.setBookId(bookId);
                xmlContent.setPageContent(txt.toString());
                xmlContent.setPageNum(pageNum);
                bulkProcessor.add(new IndexRequest(contentIndexName, contentIndexType).source(JSON.toJSONString(xmlContent)));
            }
        }

        MyPageDrawer(PageDrawerParameters parameters) throws IOException {
            super(parameters);
        }

        @Override
        protected Paint getPaint(PDColor color) throws IOException {
            return super.getPaint(color);
        }

        @Override
        protected void showGlyph(Matrix textRenderingMatrix, PDFont font, int code, String unicode, Vector displacement)
                throws IOException {
            super.showGlyph(textRenderingMatrix, font, code, unicode, displacement);
            if (StringUtils.isBlank(unicode)){
                return;
            }
            Shape bbox = new Rectangle2D.Float(0, 0, font.getWidth(code) / 1000, 1);
            AffineTransform at = textRenderingMatrix.createAffineTransform();
            bbox = at.createTransformedShape(bbox);

            Graphics2D graphics = getGraphics();
            AffineTransform trans = graphics.getTransform();
            double TY = trans.getTranslateY();
            double TX = trans.getTranslateX();

            double SX = trans.getScaleX();
            double SY = trans.getScaleY();

            float x = 0;
            float y = 0;

            // TODO 这里固定的认为dpi必须为144
            x = Math.round((float) (bbox.getBounds2D().getX() * SX + TX));
            y = Math.round((float) (bbox.getBounds2D().getY() * SY + TY));

            /**** 记录每个字的坐标 ****/
            if (unicode.equals(" ")){
                return;
            }

            if (mapLines.containsKey(y)) {
                // 获得目前的行数据
                ExtractServiceImpl.LineRe line = mapLines.get(y);
                Map<Float, String> mapRow = line.getRow();
                mapRow.put(Float.valueOf(x), unicode);
                line.addHeight((float) (bbox.getBounds2D().getHeight() * Math.abs(SY)));
            } else {
                Map<Float, String> mapRow = new TreeMap<Float, String>(new Comparator<Float>() {
                    @Override
                    public int compare(Float obj1, Float obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
                mapRow.put(Float.valueOf(x), unicode);
                ExtractServiceImpl.LineRe line = new ExtractServiceImpl.LineRe();
                line.setRow(mapRow);
                line.addHeight((float) (bbox.getBounds2D().getHeight() * Math.abs(SY)));
                mapLines.put(y, line);
            }
        }

        @Override
        public void fillPath(int windingRule) throws IOException {
            super.fillPath(windingRule);
        }

        @Override
        public void showAnnotation(PDAnnotation annotation) throws IOException {
            super.showAnnotation(annotation);
        }
    }

    public void bookMark(PDOutlineNode pdOutlineNode, int level, String bookId) throws IOException
    {
        PDOutlineItem current = pdOutlineNode.getFirstChild();
        while( current != null )
        {
            int pages =0;
            String title = current.getTitle();
            if (current.getDestination() instanceof PDPageDestination)
            {
                PDPageDestination pd = (PDPageDestination) current.getDestination();
                pages = (pd.retrievePageNumber() + 1);
            }
            if (current.getAction() instanceof PDActionGoTo)
            {
                PDActionGoTo gta = (PDActionGoTo) current.getAction();
                if (gta.getDestination() instanceof PDPageDestination)
                {
                    PDPageDestination pd = (PDPageDestination) gta.getDestination();
                    pages = (pd.retrievePageNumber() + 1);
                }
            }
            if (pages > 0){
                XmlChapter xmlChapter = new XmlChapter();
                xmlChapter.setBookId(bookId);
                xmlChapter.setLevel(level);
                xmlChapter.setPageNum(pages);
                xmlChapter.setTitle(title);
                bulkProcessor.add(new IndexRequest(chapterIndexName, chapterIndexType).source(JSON.toJSONString(xmlChapter)));
                // 递归调用
                bookMark(current, level + 1, bookId);
            }
            current = current.getNextSibling();
        }
    }

    public void deleteIndex(String bookId){
        log.info("删除索引开始".concat(bookId));
        BoolQueryBuilder query = QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("bookId", bookId));
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices(chapterIndexName)
                .withTypes(chapterIndexType)
                .withQuery(query)
                .build();
        if(elasticsearchTemplate.count(searchQuery) > 0){
            log.info("删除章节索引开始".concat(bookId));
            DeleteQuery deleteChapterQuery = new DeleteQuery();
            deleteChapterQuery.setIndex(chapterIndexName);
            deleteChapterQuery.setType(chapterIndexType);
            deleteChapterQuery.setQuery(query);
            elasticsearchTemplate.delete(deleteChapterQuery);
            log.info("删除章节索引结束".concat(bookId));
        }
        searchQuery = new NativeSearchQueryBuilder()
                .withIndices(contentIndexName)
                .withTypes(contentIndexType)
                .withQuery(query)
                .build();
        if(elasticsearchTemplate.count(searchQuery) > 0){
            log.info("删除内容索引开始".concat(bookId));
            DeleteQuery deleteContentQuery = new DeleteQuery();
            deleteContentQuery.setIndex(contentIndexName);
            deleteContentQuery.setType(contentIndexType);
            deleteContentQuery.setQuery(query);
            elasticsearchTemplate.delete(deleteContentQuery);
            log.info("删除内容索引结束".concat(bookId));
        }
        log.info("删除索引结束".concat(bookId));
    }
}
