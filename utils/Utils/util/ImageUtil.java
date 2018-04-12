package com.zhph.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/12/28.
 */
public class ImageUtil {

    private ImageUtil() {
    }

    /**
     * 根据给定大小调整源图片的大小
     *
     * @Title: resizeImage
     * @param @param srcImg
     * @param @param toWidth
     * @param @param toHeight
     * @param @return
     * @return BufferedImage
     * @throws
     */
    public static BufferedImage resizeImage(BufferedImage srcImg, int toWidth, int toHeight) {
        BufferedImage result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);
        result.getGraphics().drawImage(srcImg.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        return result;
    }

    /**
     * 根据给定大小调整源图片的大小
     *
     * @Title: resizeImage
     * @param @param srcImgData
     * @param @param toWidth
     * @param @param toHeight
     * @param @return
     * @param @throws IOException
     * @return byte[]
     * @throws
     */
    public static byte[] resizeImage(byte[] srcImgData, int toWidth, int toHeight) throws IOException {
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        try {
            bais = new ByteArrayInputStream(srcImgData);
            BufferedImage srcImg = ImageIO.read(bais);
            BufferedImage result = resizeImage(srcImg, toWidth, toHeight);
            baos = new ByteArrayOutputStream();
            ImageIO.write(result, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
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
     * 根据给定缩放比例调整源图片的大小
     *
     * @Title: zoomImage
     * @param @param srcImgData
     * @param @param ratio
     * @param @return
     * @param @throws IOException
     * @return byte[]
     * @throws
     */
    public static byte[] zoomImage(byte[] srcImgData, float ratio) throws IOException {
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(srcImgData);
            BufferedImage srcImg = ImageIO.read(bais);
            int sw = srcImg.getWidth();
            int sh = srcImg.getHeight();
            int nw = (int) (sw * ratio);
            int nh = (int) (sh * ratio);
            return resizeImage(srcImgData, nw, nh);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (bais != null)
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * 将图片转为pdf，转换后pdf为A4大小，并支持图片大小自动适应
     *
     * @Title: convertImage2Pdf
     * @param @param imgData
     * @param @return
     * @param @throws DocumentException
     * @param @throws IOException
     * @return byte[]
     * @throws
     */
    public static byte[] convertImage2PdfA4(byte[] imgData) throws DocumentException, IOException {
        ByteArrayOutputStream baos = null;
        try {
            Document doc = new Document(PageSize.A4, 10, 10, 10, 10);
            // 图片缩放处理
            Rectangle docSize = doc.getPageSize();
            // 文档内容大小
            float docw = docSize.getWidth() - doc.leftMargin() - doc.rightMargin();
            float doch = docSize.getHeight() - doc.topMargin() - doc.bottomMargin();
            Image image = Image.getInstance(imgData);
            // 图片大小
            float imgw = image.getWidth();
            float imgh = image.getHeight();
            // 计算缩放比例
            float pw = docw / imgw;
            float ph = doch / imgh;
            byte[] newImgData = null;
            if (pw < 1 || ph < 1) {
                float p = pw > ph ? ph : pw;// 取最小缩放比例
                newImgData = ImageUtil.zoomImage(imgData, p);
            } else
                newImgData = imgData;

            Image newImg = Image.getInstance(newImgData);
            newImg.setAlignment(Element.ALIGN_CENTER);// 图片居中

            baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(doc, baos);
            doc.open();
            doc.add(newImg);
            doc.close();
            return baos.toByteArray();
        } catch (BadElementException e) {
            e.printStackTrace();
            throw e;
        } catch (DocumentException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (baos != null)
                baos.close();
        }
    }

    /**
     * 将图片转为pdf，pdf的尺寸随着图片尺寸调整
     *
     * @Title: convertImage2Pdf
     * @param @param imgData
     * @param @return
     * @param @throws DocumentException
     * @param @throws IOException
     * @return byte[]
     * @throws
     */
    public static byte[] convertImage2Pdf(byte[] imgData) throws DocumentException, IOException {
        ByteArrayOutputStream baos = null;
        try {
            Document doc = new Document(null, 0, 0, 0, 0);
            Image image = Image.getInstance(imgData);
            // 图片大小
            float imgw = image.getWidth();
            float imgh = image.getHeight();
            // 文档内容大小
            float docw = imgw + doc.leftMargin() + doc.rightMargin();
            float doch = imgh + doc.topMargin() + doc.bottomMargin();
            Rectangle docSize = new Rectangle(docw, doch);
            doc.setPageSize(docSize);

            // 计算缩放比例
            float pw = docw / imgw;
            float ph = doch / imgh;
            byte[] newImgData = null;
            if (pw < 1 || ph < 1) {
                float p = pw > ph ? ph : pw;// 取最小缩放比例
                newImgData = ImageUtil.zoomImage(imgData, p);
            } else
                newImgData = imgData;

            Image newImg = Image.getInstance(newImgData);
            newImg.setAlignment(Element.ALIGN_CENTER);// 图片居中

            baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(doc, baos);
            doc.open();
            doc.add(newImg);
            doc.close();
            return baos.toByteArray();
        } catch (BadElementException e) {
            e.printStackTrace();
            throw e;
        } catch (DocumentException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (baos != null)
                baos.close();
        }
    }
}
