package com.zhph.util;

import com.zhph.commons.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: cailin
 * Date: 2017/11/6
 * Time: 14:30
 */
public class ZipUtil {
	 public static final Logger logger = LogManager.getLogger(ZipUtil.class);

    private static final int BUFFER = 1024;

    /**
     * 压缩.
     *
     * @param srcPath 压缩的源文件目录 如: c:/upload或者c:/upload/abc.txt
     * @param dstPath 生成的目标文件 如：c:/upload.zip
     */
    public static void compress(String srcPath, String dstPath) throws IOException {
        File srcFile = new File(srcPath);
        File dstFile = new File(dstPath);
        if (!srcFile.exists()) {
            logger.error(srcPath + "does not exists");
            throw new FileNotFoundException(srcPath + "does not exists");
        }
        FileOutputStream out = null;
        ZipOutputStream zipOut = null;
        try {
            out = new FileOutputStream(dstFile);
            zipOut = new ZipOutputStream(new BufferedOutputStream(out));
            zipOut.setEncoding("GBK");
            String baseDir = "";
            compress(srcFile, zipOut, baseDir);
        } catch (Throwable ex) {
            logger.error("压缩文件异常" + ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            if (null != zipOut) {
                zipOut.close();
            }

            if (null != out) {
                out.close();
            }
        }
    }


    private static void compress(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (file.isDirectory()) {
            compressDirectory(file, zipOut, baseDir);
        } else {
            compressFile(file, zipOut, baseDir);
        }
    }

    /**
     * 压缩一个目录.
     */
    private static void compressDirectory(File dir, ZipOutputStream zipOut, String baseDir) throws IOException {

        File[] files =null;
        if (null!=dir) {
            files = dir.listFiles();
        }
        if (null!=files) {
            for (int i = 0; i < files.length; i++) {
                compress(files[i], zipOut, baseDir + dir.getName() + Constant.BACK_SLANT);
            }
            if (null != dir && dir.exists()) {//如果目录存在，删除目录
                dir.delete();
            }
        }
    }

    /**
     * 压缩一个文件.
     */
    private static void compressFile(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (null==file) {
            return;
        }
        if (!file.exists()) {
            return;
        }

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zipOut.putNextEntry(entry);
            int count;
            byte[] data = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                zipOut.write(data, 0, count);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if (null != bis) {
                bis.close();
            }
            if (null != file && file.exists()) {//压缩完成后，删掉源文件
                file.delete();
            }
        }
    }


    /**
     * 解压文件.
     *
     * @param zipFile
     * @param dstPath
     * @throws java.io.IOException
     */
    public static void decompress(String zipFile, String dstPath) throws IOException {
        File pathFile = new File(dstPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);
        for (Enumeration entries = zip.getEntries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = null;
            OutputStream out = null;
            try {
                in = zip.getInputStream(entry);
                String outPath = (dstPath + Constant.BACK_SLANT + zipEntryName).replaceAll("\\*", Constant.BACK_SLANT);
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }

                out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            } finally {
                if (null != in) {
                    try {
                        in.close();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                if (null != out) {
                    out.close();
                }
            }
        }
    }
}