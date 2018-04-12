package com.zhph.util;

import com.zhph.commons.Constant;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfPersonTransfer;
import com.zhph.model.sys.SysConfigType;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/25.
 */
public class FileUpload {
    /**
     * 适用于springmvc上传组件
     *
     * @param multipartFile 文件主体
     * @param empNo         员工编号
     * @param operPath      额外的目录
     * @param fileType      文件类型
     * @param fileName      文件名
     * @param log
     */
    public static boolean upload(MultipartFile multipartFile, String empNo, String operPath, String fileType, String fileName,
                                 Logger log, String hdfsUrl) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(Constant.HDFS_OPT_PATH);//员工信息大目录
        sb.append(File.separator);
        if (operPath != null && !"".equals(operPath)) {
            sb.append(operPath);//额外的目录
            sb.append(File.separator);
        }
        sb.append(empNo);
        sb.append(File.separator);
        sb.append(fileType);
        sb.append(File.separator);
        sb.append(fileName);
        return FileUtil.upLoadFileForHdfs(multipartFile.getInputStream(), sb.toString(), hdfsUrl);
    }

}
