package com.zhph.util;

import com.aspose.cells.Workbook;
import com.aspose.words.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by zhouliang on 2017/12/28.
 */
@Component
public class AsposeHelper {
    public static final Logger logger = LogManager.getLogger(AsposeHelper.class);

    /**
     * 转换word为其它文档
     *
     * @Title: convertDocument
     * @param @param is
     * @param @param os
     * @return void
     * @throws
     */
    public void convertWord(InputStream is, OutputStream os, int saveFormat) throws Exception {
        try {
            Document doc = new Document(is);
            doc.save(os, saveFormat);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 转换word为其它文档
     *
     * @Title: convertWord
     * @param @param fileData
     * @param @param saveFormat
     * @param @return
     * @param @throws Exception
     * @return byte[]
     * @throws
     */
    public byte[] convertWord(byte[] fileData, int saveFormat) throws Exception {
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        try {
            bais = new ByteArrayInputStream(fileData);
            baos = new ByteArrayOutputStream();
            convertWord(bais, baos, saveFormat);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (baos != null)
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (bais != null)
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
    }

    /**
     * 转换word为doc（任意版本word转为Microsoft Word 97 - 2003 文档 (.doc)）
     *
     * @Title: convertWord2Doc
     * @param @param is
     * @param @param os
     * @param @throws Exception
     * @return void
     * @throws
     */
    public void convertWord2Doc(InputStream is, OutputStream os) throws Exception {
        convertWord(is, os, com.aspose.words.SaveFormat.DOC);
    }

    /**
     * 转换word为doc（任意版本word转为Microsoft Word 97 - 2003 文档 (.doc)）
     *
     * @Title: convertWord2Doc
     * @param @param fileData
     * @param @return
     * @param @throws Exception
     * @return byte[]
     * @throws
     */
    public byte[] convertWord2Doc(byte[] fileData) throws Exception {
        return convertWord(fileData, com.aspose.words.SaveFormat.DOC);
    }

    /**
     * 转换word为docx（任意版本word转为Microsoft Word 2007 文档 (.docx)）
     *
     * @Title: convertWord2Docx
     * @param @param is
     * @param @param os
     * @param @throws Exception
     * @return void
     * @throws
     */
    public void convertWord2Docx(InputStream is, OutputStream os) throws Exception {
        convertWord(is, os, com.aspose.words.SaveFormat.DOCX);
    }

    /**
     * 转换word为docx（任意版本word转为Microsoft Word 2007 文档 (.docx)）
     *
     * @Title: convertWord2Docx
     * @param @param fileData
     * @param @return
     * @param @throws Exception
     * @return byte[]
     * @throws
     */
    public byte[] convertWord2Docx(byte[] fileData) throws Exception {
        return convertWord(fileData, com.aspose.words.SaveFormat.DOCX);
    }

    /**
     * 转换word为pdf
     *
     * @Title: convertWord2Pdf
     * @param @param is
     * @param @param os
     * @param @throws Exception
     * @return void
     * @throws
     */
    public void convertWord2Pdf(InputStream is, OutputStream os) throws Exception {
        convertWord(is, os, com.aspose.words.SaveFormat.PDF);
    }

    /**
     * 转换word为pdf
     *
     * @Title: convertWord2Pdf
     * @param @param fileData
     * @param @return
     * @param @throws Exception
     * @return byte[]
     * @throws
     */
    public byte[] convertWord2Pdf(byte[] fileData) throws Exception {
        return convertWord(fileData, com.aspose.words.SaveFormat.PDF);
    }

    /**
     * 转换word为bmp图片
     *
     * @Title: convertWord2Bmp
     * @param @param is
     * @param @param os
     * @param @throws Exception
     * @return void
     * @throws
     */
    @Deprecated
    public void convertWord2Bmp(InputStream is, OutputStream os) throws Exception {
        convertWord(is, os, com.aspose.words.SaveFormat.BMP);
    }

    /**
     * 转换word为bmp图片
     *
     * @Title: convertWord2Bmp
     * @param @param fileData
     * @param @return
     * @param @throws Exception
     * @return byte[]
     * @throws
     */
    @Deprecated
    public byte[] convertWord2Bmp(byte[] fileData) throws Exception {
        return convertWord(fileData, com.aspose.words.SaveFormat.BMP);
    }

    /**
     * 转换word为jpg图片
     *
     * @Title: convertWord2Jpg
     * @param @param is
     * @param @param os
     * @param @throws Exception
     * @return void
     * @throws
     */
    @Deprecated
    public void convertWord2Jpg(InputStream is, OutputStream os) throws Exception {
        convertWord(is, os, com.aspose.words.SaveFormat.JPEG);
    }

    /**
     * 转换word为jpg图片
     *
     * @Title: convertWord2Jpg
     * @param @param fileData
     * @param @return
     * @param @throws Exception
     * @return byte[]
     * @throws
     */
    @Deprecated
    public byte[] convertWord2Jpg(byte[] fileData) throws Exception {
        return convertWord(fileData, com.aspose.words.SaveFormat.JPEG);
    }

    /**
     * 转换word为png图片
     *
     * @Title: convertWord2Png
     * @param @param is
     * @param @param os
     * @param @throws Exception
     * @return void
     * @throws
     */
    @Deprecated
    public void convertWord2Png(InputStream is, OutputStream os) throws Exception {
        convertWord(is, os, com.aspose.words.SaveFormat.PNG);
    }

    /**
     * 转换word为png图片
     *
     * @Title: convertWord2Png
     * @param @param fileData
     * @param @return
     * @param @throws Exception
     * @return byte[]
     * @throws
     */
    @Deprecated
    public byte[] convertWord2Png(byte[] fileData) throws Exception {
        return convertWord(fileData, com.aspose.words.SaveFormat.PNG);
    }

    /**
     * 转换word为html
     *
     * @Title: convertWord2Html
     * @param @param is
     * @param @param os
     * @param @throws Exception
     * @return void
     * @throws
     */
    @Deprecated
    public void convertWord2Html(InputStream is, OutputStream os) throws Exception {
        convertWord(is, os, com.aspose.words.SaveFormat.HTML);
    }

    /**
     * 转换word为html
     *
     * @Title: convertWord2Html
     * @param @param fileData
     * @param @return
     * @param @throws Exception
     * @return byte[]
     * @throws
     */
    @Deprecated
    public byte[] convertWord2Html(byte[] fileData) throws Exception {
        return convertWord(fileData, com.aspose.words.SaveFormat.HTML);
    }

    /**
     * 转换excel为其它文档
     *
     * @Title: convertWord
     * @param @param is
     * @param @param os
     * @param @param saveFormat
     * @param @throws Exception
     * @return void
     * @throws
     */
    public void convertExcel(InputStream is, OutputStream os, int saveFormat) throws Exception {
        try {
            Workbook wb = new Workbook(is);
            wb.save(os, saveFormat);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 转换excel为其它文档
     *
     * @Title: convertExcel
     * @param @param fileData
     * @param @param saveFormat
     * @param @return
     * @param @throws Exception
     * @return byte[]
     * @throws
     */
    public byte[] convertExcel(byte[] fileData, int saveFormat) throws Exception {
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        try {
            bais = new ByteArrayInputStream(fileData);
            baos = new ByteArrayOutputStream();
            convertExcel(bais, baos, saveFormat);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (baos != null)
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (bais != null)
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
    }

    /**
     * 转换excel为低版本xls
     *
     * @Title: convertExcel2Xls
     * @param @param is
     * @param @param os
     * @param @throws Exception
     * @return void
     * @throws
     */
    public void convertExcel2Xls(InputStream is, OutputStream os) throws Exception {
        convertExcel(is, os, com.aspose.cells.SaveFormat.EXCEL_97_TO_2003);
    }

    /**
     * 转换excel为低版本xls
     *
     * @Title: convertExcel2Xls
     * @param @param fileData
     * @param @return
     * @param @throws Exception
     * @return byte[]
     * @throws
     */
    public byte[] convertExcel2Xls(byte[] fileData) throws Exception {
        return convertExcel(fileData, com.aspose.cells.SaveFormat.EXCEL_97_TO_2003);
    }

    /**
     * 转换excel为高版本xlsx
     *
     * @Title: convertExcel2Xlsx
     * @param @param is
     * @param @param os
     * @param @throws Exception
     * @return void
     * @throws
     */
    public void convertExcel2Xlsx(InputStream is, OutputStream os) throws Exception {
        convertExcel(is, os, com.aspose.cells.SaveFormat.XLSX);
    }

    /**
     * 转换excel为高版本xlsx
     *
     * @Title: convertExcel2Xlsx
     * @param @param fileData
     * @param @return
     * @param @throws Exception
     * @return byte[]
     * @throws
     */
    public byte[] convertExcel2Xlsx(byte[] fileData) throws Exception {
        return convertExcel(fileData, com.aspose.cells.SaveFormat.XLSX);
    }

    /**
     * 转换excel为pdf
     *
     * @Title: convertExcel2XPdf
     * @param @param is
     * @param @param os
     * @param @throws Exception
     * @return void
     * @throws
     */
    public void convertExcel2Pdf(InputStream is, OutputStream os) throws Exception {
        convertExcel(is, os, com.aspose.cells.SaveFormat.PDF);
    }

    /**
     * 转换excel为pdf
     *
     * @Title: convertExcel2XPdf
     * @param @param fileData
     * @param @return
     * @param @throws Exception
     * @return byte[]
     * @throws
     */
    public byte[] convertExcel2Pdf(byte[] fileData) throws Exception {
        return convertExcel(fileData, com.aspose.cells.SaveFormat.PDF);
    }

    /**
     * 转换excel为html
     *
     * @Title: convertExcel2Html
     * @param @param is
     * @param @param os
     * @param @throws Exception
     * @return void
     * @throws
     */
    @Deprecated
    public void convertExcel2Html(InputStream is, OutputStream os) throws Exception {
        convertExcel(is, os, com.aspose.cells.SaveFormat.HTML);
    }

    /**
     * 转换excel为html
     *
     * @Title: convertExcel2Html
     * @param @param fileData
     * @param @return
     * @param @throws Exception
     * @return byte[]
     * @throws
     */
    @Deprecated
    public byte[] convertExcel2Html(byte[] fileData) throws Exception {
        return convertExcel(fileData, com.aspose.cells.SaveFormat.HTML);
    }
}
