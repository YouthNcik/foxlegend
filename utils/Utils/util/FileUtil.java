package com.zhph.util;

import com.alibaba.fastjson.JSONObject;
import com.zhph.config.UrlConfig;
import com.zhph.exception.AppException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.io.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 文件管理工具.
 */
public class FileUtil {

	public static final Logger logger = LogManager.getLogger(FileUtil.class);

	public static final int IMG = 1;
	private static String SALARY = "/salary";
	private static final String ATTACHMENT = "attachment;filename=";
	private static final String UTF8CODE = "UTF-8";

	public FileUtil() {
	}

	/**
	 * 删除单个文件.
	 *
	 * @param sPath
	 * @return
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		//
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	// 删除目录下文件
	public static void deletePath(String filepath) throws Exception {
		File f = new File(filepath);// 定义文件路径
		if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
			// 若有则把文件放进数组，并判断是否有下级目录
			File[] delFile = f.listFiles();
			int i = f.listFiles().length;
			for (int j = 0; j < i; j++) {
				if (delFile[j].isDirectory()) {
					deletePath(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
				}
				delFile[j].delete();// 删除文件
			}
		}
	}

	/**
	 * @param filePath
	 *            目录路径
	 * @return
	 * @throws Exception
	 * @功能描述：判断文件目录是否存在如果不存在则创建目录
	 * @创建时间
	 * @author zst
	 */
	public static void mkDirs(String filePath) {
		// 判断路径是否存在
		File directory = new File(filePath.toString());
		if (directory.exists() && directory.isDirectory()) {
			return;
		} else {
			// 如果不存在则创建目录
			directory.mkdirs();
		}
	}

	/**
	 * 上传文件到hdfs
	 * 
	 * @param ins
	 * @param filePath
	 * @return
	 */
	public static boolean upLoadFileForHdfs(InputStream ins, String filePath, String hdfsUrl) throws Exception{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = null;
		boolean result = false;
			post = new HttpPost(hdfsUrl);
			if (!filePath.startsWith(SALARY)) {
				filePath = SALARY + filePath;
			}
			String hdfsPath = filePath.replace("\\", "/");
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.setCharset(Charset.forName("utf8"));
			builder.addTextBody("filePath", hdfsPath, ContentType.TEXT_PLAIN.withCharset("utf-8"));
			builder.addBinaryBody("file", ins, ContentType.DEFAULT_BINARY, "");
			post.setEntity(builder.build());
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				JSONObject obj = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
				if ("success".equals(obj.getString("result"))) {
					result = true;
					logger.error("上传文件成功,路径=====" + hdfsPath);
				} else {
					throw new AppException(obj.getString("exception"));
				}
			}else{
				throw new AppException("连接到文件服务器失败！");
			}
		return result;
	}

	/**
	 * 员工照片初始化展示
	 * 
	 * @param response
	 * @param path
	 * @param config
	 * @return
	 */
	public static boolean showImg(HttpServletResponse response, String path, UrlConfig config) throws Exception{
			InputStream fileIs = null;
			if (!path.startsWith(SALARY)) {
				path = SALARY + path;
			}
			String hdfsPath = path.replace("\\", "/");
			byte[] bytes = null;
			if ((bytes = getBytesForHdfs(hdfsPath, config)) != null && bytes.length > 0) {
				fileIs = new ByteArrayInputStream(bytes);
			} else {
				fileIs = Resources.getResourceAsStream("static/ui/images/headImg.png");
			}
			int i = fileIs.available();
			byte[] data = new byte[i];
			fileIs.read(data);
			response.setContentType("image/*");
			OutputStream outStream = null;
			try{
				outStream = response.getOutputStream();
				outStream.write(data);
				outStream.flush();
				outStream.close();
				fileIs.close();
			}catch(Exception e){
				logger.debug("员工照片初始化展示 IOException :"+e.getMessage());
				throw new Exception("员工照片初始化展示 IOException :"+e.getMessage());
			}finally{
				if (outStream != null) {
					try {
						outStream.close(); // 关闭流
					} catch (IOException e) {
						logger.debug("员工照片初始化展示 outStream IOException : "+e.getMessage());
						throw new Exception("员工照片初始化展示 outStream IOException :"+e.getMessage());
					}
				}
				if (fileIs != null) {
					try {
						fileIs.close(); // 关闭流
					} catch (IOException e) {
						logger.debug("员工照片初始化展示 fileIs IOException : "+e.getMessage());
						throw new Exception("员工照片初始化展示 fileIs IOException :"+e.getMessage());
					}
				}
			}

			return true;
	}

	/**
	 * 根据老系统的文件类型展示员工照片
	 * @param response
	 * @param paths
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static boolean showImg(HttpServletResponse response, List<String> paths, UrlConfig config) throws Exception{
		InputStream fileIs = null;
		byte[] bytes=null;
		for (String path:paths) {
			if (!path.startsWith(SALARY)) {
				path = SALARY + path;
			}
			String hdfsPath = path.replace("\\", "/");
			bytes= getBytesForHdfs(hdfsPath, config);
			if (bytes!=null&&bytes.length>0){
				break;
			}
		}
		if (bytes!= null && bytes.length > 0) {
			fileIs = new ByteArrayInputStream(bytes);
		} else {
			//无员工头像 或则没找到照片的显示
			fileIs = Resources.getResourceAsStream("static/ui/images/headImg.png");
		}
		int i = fileIs.available();
		byte[] data = new byte[i];
		fileIs.read(data);
		response.setContentType("image/*");
		OutputStream outStream = null;
		try{
			outStream = response.getOutputStream();
			outStream.write(data);
			outStream.flush();
			outStream.close();
			fileIs.close();
		}catch(Exception e){
			logger.debug("根据老系统的文件类型展示员工照片 outStream IOException:"+e.getMessage());
			throw new Exception("根据老系统的文件类型展示员工照片 outStream IOException:"+e.getMessage());
		}finally{
			if (outStream != null) {
				try {
					outStream.close(); // 关闭流
				} catch (IOException e) {
					logger.debug("根据老系统的文件类型展示员工照片 outStream IOException : "+e.getMessage());
					throw new Exception("根据老系统的文件类型展示员工照片 outStream IOException:"+e.getMessage());
				}
			}
			if (fileIs != null) {
				try {
					fileIs.close(); // 关闭流
				} catch (IOException e) {
					logger.debug("根据老系统的文件类型展示员工照片 fileIs IOException : "+e.getMessage());
					throw new Exception("根据老系统的文件类型展示员工照片 fileIs IOException:"+e.getMessage());
				}
			}
		}

		return true;
	}


	/**
	 * 测试本地文件的预览
	 * 
	 * @param response
	 * @throws Exception
	 */
	public static void showFileTestlocal(HttpServletResponse response) throws Exception {
		InputStream fileIs = Resources.getResourceAsStream("static/ui/images/headImg.png");
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = fileIs.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] data = swapStream.toByteArray();
		data = convertFile2Pdf(data, "headImg.png", response);

		OutputStream outStream = null;
		try{
			outStream = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
			outStream.write(data, 0, data.length);
			outStream.flush();
			outStream.close();
			fileIs.close();
		}catch(Exception e ){
			logger.debug("测试本地文件的预览 IOException  : "+e.getMessage());
			throw new Exception("测试本地文件的预览 IOException  : "+e.getMessage());
		}finally{
			if (outStream != null) {
				try {
					outStream.close(); // 关闭流
				} catch (IOException e) {
					logger.debug("测试本地文件的预览 outStream IOException : "+e.getMessage());
					throw new Exception("测试本地文件的预览outStream IOException  : "+e.getMessage());
				}
			}
			if (fileIs != null) {
				try {
					fileIs.close(); // 关闭流
				} catch (IOException e) {
					logger.debug("测试本地文件的预览 fileIs IOException : "+e.getMessage());
					throw new Exception("测试本地文件的预览fileIs IOException  : "+e.getMessage());
				}
			}
		}
	}

	/**
	 * 预览文件
	 * 
	 * @param res
	 * @param hdfsPath
	 *            文件路径
	 * @param fileName
	 *            文件完整名字
	 * @param config
	 *            地址
	 * @throws Exception
	 */
	public static void showFile(HttpServletResponse res, String hdfsPath, String fileName, UrlConfig config) throws Exception {
		String path = hdfsPath.replace("\\", "/");
		byte[] bytes = getBytesForHdfs(path, config);
		bytes = convertFile2Pdf(bytes, fileName, res);
		OutputStream outStream = null;
		try{
			outStream = res.getOutputStream(); // 得到向客户端输出二进制数据的对象
			outStream.write(bytes, 0, bytes.length);
			outStream.flush();
			outStream.close();
		}catch(Exception e ){
			logger.debug("预览文件 IOException     : "+e.getMessage());
			throw new Exception("预览文件 IOException     : "+e.getMessage());
		}finally{
			if (outStream != null) {
				try {
					outStream.close(); // 关闭流
				} catch (IOException e) {
					logger.debug("预览文件 IOException : "+e.getMessage());
					throw new Exception("预览文件 IOException     : "+e.getMessage());
				}
			}
		}
	}

	/**
	 * 一个附件多个路径的情况
	 * @param res
	 * @param hdfsPaths
	 * @param fileName
	 * @param config
	 * @throws Exception
	 */
	public static void showFile(HttpServletResponse res, List<String> hdfsPaths, String fileName, UrlConfig config) throws Exception {
		byte[] bytes = null;
		for (String hdfsPath : hdfsPaths) {
			String path = hdfsPath.replace("\\", "/");
			bytes = getBytesForHdfs(path, config);
			if (bytes != null) {
				break;
			}
		}
		bytes = convertFile2Pdf(bytes, fileName, res);
		OutputStream outStream = null;
		try{
			outStream = res.getOutputStream() ; // 得到向客户端输出二进制数据的对象
			outStream.write(bytes, 0, bytes.length);
			outStream.flush();
			outStream.close();
		}catch(Exception e ){
			logger.debug(" 一个附件多个路径 IOException  : "+e.getMessage());
			throw new Exception(" 一个附件多个路径 IOException  : "+e.getMessage());
		}finally{
			if (outStream != null) {
				try {
					outStream.close(); // 关闭流
				} catch (IOException e) {
					logger.debug("一个附件多个路径的 IOException : "+e.getMessage());
					throw new Exception(" 一个附件多个路径 IOException  : "+e.getMessage());
				}
			}
		}
	}


	/**
	 * @throws Exception 将文件转为pdf
	 *
	 * @Title: convert2Pdf @param @param data @param @return @return
	 * byte[] @throws
	 */
	private static byte[] convertFile2Pdf(byte[] data, String fileName, HttpServletResponse res) throws Exception {
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
		AsposeHelper asposeHelper = SpringContextUtils.getBeanByClass(AsposeHelper.class);
		switch (FileTypeUtil.fileType(suffix)) {
		case "OTHER":
			break;
		case "PDF":
			if ("pdf".equalsIgnoreCase(suffix)) {
				res.setContentType("application/pdf");
				return data;
			} else if ("doc".equalsIgnoreCase(suffix)) {
				res.setContentType("application/pdf");
				return asposeHelper.convertWord2Pdf(data);
			} else if ("docx".equalsIgnoreCase(suffix)) {
				res.setContentType("application/pdf");
				return asposeHelper.convertWord2Pdf(data);
			}else if ("xls".equalsIgnoreCase(suffix)) {
				res.setContentType("application/pdf");
				return asposeHelper.convertExcel2Pdf(data);
			} else if ("xlsx".equalsIgnoreCase(suffix)) {
				res.setContentType("application/pdf");
				return asposeHelper.convertExcel2Pdf(data);
			} else {
				res.setContentType("image/*");
				res.setContentType("application/pdf");
				throw new Exception("类型" + suffix + "不支持转换为pdf预览");
			}
		case "IMAGE":
			res.setContentType("application/pdf");
			return ImageUtil.convertImage2Pdf(data);
		default:
			break;
		}
		return null;
	}

	private static byte[] getBytesForHdfs(String hdfsPath, UrlConfig config) throws IOException {
		byte[] bytes = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(config.getFileDownload());
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setCharset(Charset.forName("utf8"));
		builder.addTextBody("filePath", hdfsPath, ContentType.TEXT_PLAIN.withCharset("utf-8"));
		post.setEntity(builder.build());
		HttpResponse httpResponse = httpClient.execute(post);
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(httpResponse.getEntity()));
			if ("success".equals(jsonObject.getString("result"))) {
				bytes = jsonObject.getBytes("fileBytes");
			}
		}
		return bytes;
	}

	public static boolean downFileForHdfs(HttpServletResponse res, String hdfsPath, String fileName, UrlConfig urlConfig) throws Exception {
		boolean result = false;
		try {
			String path = hdfsPath.replace("\\", "/");
			byte[] fileBytes = getBytesForHdfs(path, urlConfig);
			result = outThisBytes2Browser(fileBytes, result, res, fileName);
		} catch (Exception e) {
			result = false;
			logger.error("下载文件失败====", e);
			throw new Exception("文件" + fileName + "不存在!");
		}finally{

		}
		return result;
	}

	public static boolean downFileForHdfs(HttpServletResponse res, List<String> paths, String fileName, UrlConfig urlConfig) throws Exception {
		boolean result = false;
		byte[] fileBytes=null;
		try {
			for (String path:paths) {
				String hdsfPath = path.replace("\\", "/");
				fileBytes = getBytesForHdfs(hdsfPath, urlConfig);
				if (fileBytes!=null&&fileBytes.length>0){
					break;
				}
			}
			result = outThisBytes2Browser(fileBytes, result, res, fileName);
		} catch (Exception e) {
			result = false;
			logger.error("下载文件失败====", e);
			throw new Exception("文件" + fileName + "不存在!");
		}
		return  result;
	}

	private static boolean outThisBytes2Browser(byte[] fileBytes,boolean result,HttpServletResponse res, String fileName) throws Exception{
		if (fileBytes != null && fileBytes.length > 0) {
			res.setContentType("application/x-msdownload");
			res.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, UTF8CODE));
			BufferedOutputStream bos = null;
			OutputStream fos = null;
			ByteArrayInputStream bis = null;
			try {
				bis = new ByteArrayInputStream(fileBytes);
				fos = res.getOutputStream();
				bos = new BufferedOutputStream(fos);
				int bytesRead = 0;
				byte[] buffer = new byte[2048];
				while ((bytesRead = bis.read(buffer, 0, 2048)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
				bos.flush();
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("文件" + fileName + "下载失败!");
			} finally {
				if (bis != null) {
					bis.close();
				}
				if (fos != null) {
					fos.close();
				}
				if (bos != null) {
					bos.close();
				}
			}
			result = true;
		}
		return result;
	}
}
