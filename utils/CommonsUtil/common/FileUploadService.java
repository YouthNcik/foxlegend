package com.zhph.service.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.zhph.model.hqclcf.HqclcfEmp;

/**
 * @author lxp
 * @date 2018年1月18日 上午11:24:06
 * @parameter
 * @return
 */
public interface FileUploadService {

	/**
	 * 员工附件上传
	 * 
	 * @param request
	 */
	public void uploadFile(MultipartHttpServletRequest request, HqclcfEmp emp) throws Exception;

	/**
	 * 员工照片展示
	 * 
	 * @param hqclcfEmpFile
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void showImg(HttpServletRequest req, HttpServletResponse res) throws Exception;

	/**
	 * 验证文件是否预览
	 * 
	 * @param req
	 * @return
	 */

	public JSONObject checkFileIfCanPreview(HttpServletRequest req) throws Exception;

	/**
	 * 预览文件
	 * 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void previewFile(HttpServletRequest req, HttpServletResponse res) throws Exception;

	/**
	 * 下载文件
	 * 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void downloadFile(HttpServletRequest req, HttpServletResponse res) throws Exception;

}
