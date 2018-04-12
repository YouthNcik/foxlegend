package com.zhph.service.common.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.zhph.commons.Constant;
import com.zhph.config.UrlConfig;
import com.zhph.mapper.hqclcf.HqclcfEmpFileMapper;
import com.zhph.mapper.sys.SysConfigTypeMapper;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfEmpFile;
import com.zhph.model.hqclcf.HqclcfEmpTempFile;
import com.zhph.model.sys.SysConfigType;
import com.zhph.model.sys.SysMidFileType;
import com.zhph.model.sys.SysUser;
import com.zhph.service.common.FileUploadService;
import com.zhph.util.CommonUtil;
import com.zhph.util.FileUpload;
import com.zhph.util.FileUtil;

/**
 * @author lxp
 * @date 2018年1月18日 上午11:25:43
 * @parameter
 * @return
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FileUploadServiceImpl implements FileUploadService {

	public static final Logger logger = LogManager.getLogger(FileUploadServiceImpl.class);

	@Autowired
	private UrlConfig urlConfig;

	@Autowired
	private SysConfigTypeMapper sysConfigTypeMapper;

	@Autowired
	private HqclcfEmpFileMapper hqclcfEmpFileMapper;

	/**
	 * 员工附件上传
	 * 
	 * @param request
	 * @param emp
	 * @param flag
	 * @throws Exception
	 */
	@Override
	public void uploadFile(MultipartHttpServletRequest request, HqclcfEmp emp) throws Exception {
		Integer businessLine = emp.getBusinessLine();
		switch (businessLine) {
		case 1:// 总部
			Iterator<Map.Entry<String, Integer>> hqIter = buildFileMapName(Constant.ZB_FILE).entrySet().iterator();
			uploadFile2Hdfs(hqIter, emp, request, Constant.ZHPHHQ);
			break;
		case 2:// 消分
			Iterator<Map.Entry<String, Integer>> xfIter = buildFileMapName(Constant.XF_FILE).entrySet().iterator();
			uploadFile2Hdfs(xfIter, emp, request, Constant.ZHPHXJ);
			break;
		case 3:// 信贷
			Iterator<Map.Entry<String, Integer>> xdIter = buildFileMapName(Constant.XD_FILE).entrySet().iterator();
			uploadFile2Hdfs(xdIter, emp, request, Constant.ZHXD);
			break;
		}
	}

	/**
	 * 员工照片展示
	 * 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@Override
	public void showImg(HttpServletRequest req, HttpServletResponse res) throws Exception {

		HqclcfEmpFile hqclcfEmpFile = new HqclcfEmpFile();
		hqclcfEmpFile.setEmpNo(req.getParameter("empNo"));
		hqclcfEmpFile.setFileType(Integer.valueOf(req.getParameter("fileType")));
		hqclcfEmpFile.setBusinessLine(Integer.valueOf(req.getParameter("businessLine")));

		List<HqclcfEmpFile> hqclcfEmpFiles = hqclcfEmpFileMapper.queryFileByParam(hqclcfEmpFile.getEmpNo(), hqclcfEmpFile.getFileType());
		HqclcfEmpFile empFile = hqclcfEmpFiles.size() > 0 ? hqclcfEmpFiles.get(0) : null;
		if (empFile != null) {
			String fileTypeFlag = empFile.getFileTypeFlag();
			// 查询文件表记录了这这个员工图片没有
			List<HqclcfEmpFile> files = hqclcfEmpFileMapper.queryEmpFile(hqclcfEmpFile);
			switch (fileTypeFlag) {
			case "oldSalary":
				List<String> list = bulidPathByEmpOldFile(empFile);
				if (files != null && files.size() > 0) {
					FileUtil.showImg(res, list, urlConfig);
				}
				break;
			case "newSalary":
				StringBuffer stringBuffer = bulidPathByEmpNewFile(empFile);
				if (files != null && files.size() > 0) {
					FileUtil.showImg(res, stringBuffer.toString(), urlConfig);
				}
				break;
			}
		}
	}

	/**
	 * 
	 * 验证附件是否可以预览
	 */
	@Override
	public JSONObject checkFileIfCanPreview(HttpServletRequest req) throws Exception {

		String empNo = req.getParameter("empNo");
		String fileType = req.getParameter("fileType");
		String fid = req.getParameter("fid");
		String flag = req.getParameter("flag");

		String fileName = "";
		if ("1".equals(flag)) {
			List<HqclcfEmpFile> hqclcfEmpFileList = hqclcfEmpFileMapper.queryFileByParam(empNo, Integer.valueOf(fileType));
			HqclcfEmpFile hqclcfEmpFile = hqclcfEmpFileList.size() > 0 ? hqclcfEmpFileList.get(0) : null;
			fileName = hqclcfEmpFile.getFileName();
		} else if ("2".equals(flag)) {
			List<HqclcfEmpTempFile> hqclcfEmpTempFileList = hqclcfEmpFileMapper.queryTempFileByParam(empNo, Integer.valueOf(fileType), fid);
			HqclcfEmpTempFile hqclcfEmpTemppFile = hqclcfEmpTempFileList.size() > 0 ? hqclcfEmpTempFileList.get(0) : null;
			fileName = hqclcfEmpTemppFile.getFileName();
		}

		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();

		JSONObject obj = new JSONObject();
		obj.put("code", 200);
		if (suffix.equals("txt")) {
			obj.put("code", 500);
			obj.put("msg", "txt格式的文件不支持预览！");
		}
		if (suffix.equals("rar")) {
			obj.put("code", 500);
			obj.put("msg", "rar格式的文件不支持预览！");
		}
		if (suffix.equals("zip")) {
			obj.put("code", 500);
			obj.put("msg", "zip格式的文件不支持预览！");
		}

		return obj;
	}

	/**
	 * 预览文件
	 * 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@Override
	public void previewFile(HttpServletRequest req, HttpServletResponse res) throws Exception {

		String empNo = req.getParameter("empNo");
		String fileType = req.getParameter("fileType");
		String fid = req.getParameter("fid");
		String flag = req.getParameter("flag");

		if ("1".equals(flag)) {
			List<HqclcfEmpFile> hqclcfEmpFileList = hqclcfEmpFileMapper.queryFileByParam(empNo, Integer.valueOf(fileType));
			HqclcfEmpFile hqclcfEmpFile = hqclcfEmpFileList.size() > 0 ? hqclcfEmpFileList.get(0) : null;

			if (hqclcfEmpFile != null) {
				String fileTypeFlag = hqclcfEmpFile.getFileTypeFlag();
				if ("oldSalary".equals(fileTypeFlag)) {
					List<String> list = bulidPathByEmpOldFile(hqclcfEmpFile);
					// 从hdfs上拉取文件预览
					FileUtil.showFile(res, list, hqclcfEmpFile.getFileName(), urlConfig);
				} else {
					StringBuffer stringBuffer = bulidPathByEmpNewFile(hqclcfEmpFile);
					FileUtil.showFile(res, stringBuffer.toString(), hqclcfEmpFile.getFileName(), urlConfig);
				}
			}
		} else if ("2".equals(flag)) {
			List<HqclcfEmpTempFile> hqclcfEmpTempFileList = hqclcfEmpFileMapper.queryTempFileByParam(empNo, Integer.valueOf(fileType), fid);
			HqclcfEmpTempFile hqclcfEmpTemppFile = hqclcfEmpTempFileList.size() > 0 ? hqclcfEmpTempFileList.get(0) : null;

			HqclcfEmpFile hqclcfEmpFile = new HqclcfEmpFile();
			hqclcfEmpFile.setEmpNo(hqclcfEmpTemppFile.getEmpNo());
			hqclcfEmpFile.setBusinessLine(hqclcfEmpTemppFile.getBusinessLine());
			hqclcfEmpFile.setFileType(hqclcfEmpTemppFile.getFileType());
			hqclcfEmpFile.setFileName(hqclcfEmpTemppFile.getFileName());

			StringBuffer stringBuffer = bulidPathByEmpNewFile(hqclcfEmpFile);
			FileUtil.showFile(res, stringBuffer.toString(), hqclcfEmpTemppFile.getFileName(), urlConfig);

		}
	}

	/**
	 * 下载文件
	 * 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@Override
	public void downloadFile(HttpServletRequest req, HttpServletResponse res) throws Exception {

		String empNo = req.getParameter("empNo");
		String fileType = req.getParameter("fileType");
		String fid = req.getParameter("fid");
		String flag = req.getParameter("flag");

		if ("1".equals(flag)) {
			List<HqclcfEmpFile> hqclcfEmpFileList = hqclcfEmpFileMapper.queryFileByParam(empNo, Integer.valueOf(fileType));
			HqclcfEmpFile hqclcfEmpFile = hqclcfEmpFileList.size() > 0 ? hqclcfEmpFileList.get(0) : null;

			if (hqclcfEmpFile != null) {
				String fileTypeFlag = hqclcfEmpFile.getFileTypeFlag();
				if ("oldSalary".equals(fileTypeFlag)) {
					List<String> list = bulidPathByEmpOldFile(hqclcfEmpFile);
					FileUtil.downFileForHdfs(res, list, hqclcfEmpFile.getFileName(), urlConfig);
				} else {
					StringBuffer sb = bulidPathByEmpNewFile(hqclcfEmpFile);
					FileUtil.downFileForHdfs(res, sb.toString(), hqclcfEmpFile.getFileName(), urlConfig);
				}
			}

		} else if ("2".equals(flag)) {
			List<HqclcfEmpTempFile> hqclcfEmpTempFileList = hqclcfEmpFileMapper.queryTempFileByParam(empNo, Integer.valueOf(fileType), fid);
			HqclcfEmpTempFile hqclcfEmpTemppFile = hqclcfEmpTempFileList.size() > 0 ? hqclcfEmpTempFileList.get(0) : null;

			HqclcfEmpFile hqclcfEmpFile = new HqclcfEmpFile();
			hqclcfEmpFile.setEmpNo(hqclcfEmpTemppFile.getEmpNo());
			hqclcfEmpFile.setBusinessLine(hqclcfEmpTemppFile.getBusinessLine());
			hqclcfEmpFile.setFileType(hqclcfEmpTemppFile.getFileType());
			hqclcfEmpFile.setFileName(hqclcfEmpTemppFile.getFileName());

			StringBuffer stringBuffer = bulidPathByEmpNewFile(hqclcfEmpFile);
			FileUtil.downFileForHdfs(res, stringBuffer.toString(), hqclcfEmpFile.getFileName(), urlConfig);
		}
	}

	/**
	 * 附件上传与附件数据保存
	 * 
	 * @param hqIter
	 * @param emp
	 * @param request
	 * @param zhPath
	 * @param flag
	 *            '1' 员工附件正式表 '2'员工附件临时表
	 * @throws Exception
	 */
	private void uploadFile2Hdfs(Iterator<Map.Entry<String, Integer>> hqIter, HqclcfEmp emp, MultipartHttpServletRequest request, String zhPath) throws Exception {
		SysUser onlineUser = CommonUtil.getOnlineUser();
		while (hqIter.hasNext()) {
			Map.Entry<String, Integer> hqNext = hqIter.next();
			Integer fileType = hqNext.getValue();
			String fileKey = hqNext.getKey();
			MultipartFile file = request.getFile(fileKey);
			if (file != null) {
				if ("".equals(file.getOriginalFilename())) {
					continue;
				}

				String flag = String.valueOf(request.getAttribute("flag"));
				String fid = String.valueOf(request.getAttribute("fid"));
				if ("1".equals(flag)) {
					HqclcfEmpFile empFile = new HqclcfEmpFile();
					empFile.setBusinessLine(emp.getBusinessLine());
					empFile.setCreateTime(new Date());
					empFile.setCreator(onlineUser.getFullName());
					empFile.setEmpNo(emp.getEmpNo());
					empFile.setFileName(file.getOriginalFilename());
					empFile.setFileType(fileType);
					empFile.setFileExtend(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
					try {
						FileUpload.upload(file, emp.getEmpNo(), zhPath, empFile.getFileType().toString(), empFile.getFileName(), logger, urlConfig.getFileUpload());
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					hqclcfEmpFileMapper.deleteFile(empFile.getEmpNo(), empFile.getFileType());
					hqclcfEmpFileMapper.insertempFile(empFile);
				} else {
					HqclcfEmpTempFile hqclcfEmpTempFile = new HqclcfEmpTempFile();
					hqclcfEmpTempFile.setFid(fid);
					hqclcfEmpTempFile.setBusinessLine(emp.getBusinessLine());
					hqclcfEmpTempFile.setEmpNo(emp.getEmpNo());
					hqclcfEmpTempFile.setFileName(file.getOriginalFilename());
					hqclcfEmpTempFile.setFileType(fileType);
					hqclcfEmpTempFile.setFileExtend(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
					hqclcfEmpTempFile.setCreateTime(new Date());
					hqclcfEmpTempFile.setCreator(onlineUser.getFullName());
					try {
						FileUpload.upload(file, emp.getEmpNo(), zhPath, hqclcfEmpTempFile.getFileType().toString(), hqclcfEmpTempFile.getFileName(), logger, urlConfig.getFileUpload());
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					hqclcfEmpFileMapper.delTempTempFile(hqclcfEmpTempFile.getEmpNo(), String.valueOf(hqclcfEmpTempFile.getFileType()), hqclcfEmpTempFile.getFid());
					hqclcfEmpFileMapper.insertEmpTempFile(hqclcfEmpTempFile);
				}
			}
		}
	}

	/**
	 * 附件类型转换
	 * 
	 * @param sysCode
	 * @return
	 * @throws Exception
	 */
	private Map<String, Integer> buildFileMapName(String sysCode) throws Exception {
		Map<String, Integer> fileMap = new HashMap<>();
		List<SysConfigType> listFiles = sysConfigTypeMapper.getConfigByPSysCode(sysCode);
		List<SysConfigType> empimgpotos = sysConfigTypeMapper.getConfigByPSysCode(Constant.EMPIMGPOTO);
		SysConfigType config = empimgpotos.size() == 1 ? empimgpotos.get(0) : null;
		fileMap.put(config.getSysCode(), config.getSysValue());
		for (SysConfigType configType : listFiles) {
			fileMap.put(configType.getSysCode(), configType.getSysValue());
		}
		return fileMap;
	}

	/**
	 * 新系统新增的文件路径构建
	 * 
	 * @param empFile
	 * @return
	 * @throws Exception
	 */
	private StringBuffer bulidPathByEmpNewFile(HqclcfEmpFile hqclcfEmpFile) throws Exception {
		StringBuffer path = new StringBuffer();
		path.append(Constant.HDFS_OPT_PATH);
		path.append(File.separator);
		switch (hqclcfEmpFile.getBusinessLine()) {
		case 2:// 消分
			path.append(Constant.ZHPHXJ);
			break;
		case 1:// 总部
			path.append(Constant.ZHPHHQ);
			break;
		case 3:// 信贷
			path.append(Constant.ZHXD);
			break;
		}
		path.append(File.separator);
		path.append(hqclcfEmpFile.getEmpNo());
		path.append(File.separator);
		path.append(hqclcfEmpFile.getFileType());
		path.append(File.separator);
		path.append(hqclcfEmpFile.getFileName());
		return path;
	}

	/**
	 * 通过老系统的文件类型构建文件路径
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private List<String> bulidPathByEmpOldFile(HqclcfEmpFile hqclcfEmpFile) throws Exception {

		SysMidFileType midFileType = new SysMidFileType();
		midFileType.setBusinessline(hqclcfEmpFile.getBusinessLine());
		midFileType.setNewFileType(hqclcfEmpFile.getFileType());

		List<String> paths = new ArrayList<>();
		StringBuffer path = new StringBuffer();
		path.append(Constant.HDFS_OPT_PATH);
		path.append(File.separator);
		switch (hqclcfEmpFile.getBusinessLine()) {
		case 2:// 消分
			path.append(Constant.ZHPHXJ);
			break;
		case 1:// 总部
			path.append(Constant.ZHPHHQ);
			break;
		case 3:// 信贷
			path.append(Constant.ZHXD);
			break;
		}
		path.append(File.separator);
		path.append(hqclcfEmpFile.getEmpNo());
		path.append(File.separator);

		List<Map<String, Object>> list = hqclcfEmpFileMapper.queryMidFileType(midFileType);
		for (Map<String, Object> map : list) {
			Object oldFileType = map.get("OLD_FILE_TYPE");
			if (oldFileType != null) {
				path.append(oldFileType.toString());
				path.append(File.separator);
				path.append(hqclcfEmpFile.getFileName());
				paths.add(path.toString());
			}
		}
		return paths;
	}

}
