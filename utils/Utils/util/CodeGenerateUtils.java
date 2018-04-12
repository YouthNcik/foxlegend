package com.zhph.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhph.model.sys.SysAutoCode;

import freemarker.template.Template;

public class CodeGenerateUtils {

	private static String packagePath = "";
	private static String moduleMath = "";
	private static String tableCode = "";
	private static String ChangetableCode = "";
	private static String tableComment = "";
	private static String diskPath = "D://autocode//";

	public static void generate(SysAutoCode sysAutoCode, List<SysAutoCode> saclist) throws Exception {
		try {
			// 生成Model文件
			generateFileByTemplate("Model.ftl", "Model.java", sysAutoCode, saclist);

			// 生成Controller文件
			generateFileByTemplate("Controller.ftl", "Controller.java", sysAutoCode, saclist);

			// 生成Service文件
			generateFileByTemplate("Service.ftl", "Service.java", sysAutoCode, saclist);

			// 生成ServiceImpl文件
			generateFileByTemplate("ServiceImpl.ftl", "ServiceImpl.java", sysAutoCode, saclist);

			// 生成Mapper.java文件
			generateFileByTemplate("MapperJava.ftl", "Mapper.java", sysAutoCode, saclist);

			// 生成Mapper.xml文件
			generateFileByTemplate("MapperXml.ftl", "Mapper.xml", sysAutoCode, saclist);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {

		}
	}

	private static void generateFileByTemplate(String templateName, String suffix, SysAutoCode sysAutoCode, List<SysAutoCode> saclist) throws Exception {

		packagePath = CommonUtil.StringIfNullOrEmpty(sysAutoCode.getPackagePath()) ? "com.zhph" : sysAutoCode.getPackagePath();
		moduleMath = CommonUtil.StringIfNullOrEmpty(sysAutoCode.getModuleMath()) ? "sys" : sysAutoCode.getModuleMath();
		tableCode = CommonUtil.StringIfNullOrEmpty(sysAutoCode.getTableCode()) ? "dual" : sysAutoCode.getTableCode();
		ChangetableCode = replaceUnderLineAndUpperCase(tableCode);
		tableComment = saclist.get(0).getTableName();

		File filePath = new File(diskPath);
		if (!filePath.exists()) {
			filePath.mkdir();
		}

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("packagePath", packagePath);
		dataMap.put("moduleMath", moduleMath);
		dataMap.put("tableCode", tableCode);
		dataMap.put("ChangetableCode", ChangetableCode);
		dataMap.put("tableComment", tableComment);

		dataMap.put("author", CommonUtil.getOnlineFullName());
		dataMap.put("curDate", DateUtil.parseDateFormat(new Date(), "yyyy-MM-DD"));

		dataMap.put("data", sysAutoCode);
		String queryCulmun = sysAutoCode.getQueryColumn();
		String[] qls = queryCulmun.split(",");
		String changeQueryCulmun = "";
		for (int i = 0; i < qls.length; i++) {
			changeQueryCulmun = changeQueryCulmun + replaceUnderLineAndLowCase(qls[i]) + ",";
		}
		dataMap.put("queryCulmun", changeQueryCulmun);

		List<SysAutoCode> changesacList = new ArrayList<SysAutoCode>();
		if (saclist.size() > 0) {
			for (SysAutoCode ss : saclist) {
				SysAutoCode obj = new SysAutoCode();
				obj.setColumnCode(ss.getColumnCode());
				obj.setChangecolumnCode(replaceUnderLineAndLowCase(ss.getColumnCode()));
				obj.setColumnName(ss.getColumnName());
				obj.setColumnType(ss.getColumnType());

				changesacList.add(obj);
			}
		}
		dataMap.put("model_column", changesacList);

		String path = diskPath + ChangetableCode + suffix;
		File mapperFile = new File(path);
		FileOutputStream fos = new FileOutputStream(mapperFile);

		Writer out = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"), 10240);
		Template template = FreeMarkerTemplateUtils.getTemplate(templateName);
		template.process(dataMap, out);
	}

	// 用 _分割的字符首席字母大写
	public static String replaceUnderLineAndUpperCase(String str) {
		StringBuffer sb = new StringBuffer();
		String[] arr = str.split("_");
		for (int i = 0; i < arr.length; i++) {
			String s1 = arr[i];
			sb.append(s1.substring(0, 1).toUpperCase() + s1.substring(1).toLowerCase());
		}
		return sb.toString();
	}

	// 去除_分割的字符第一个首席字母
	public static String replaceUnderLineAndLowCase(String str) {
		StringBuffer sb = new StringBuffer();
		String[] arr = str.split("_");
		for (int i = 0; i < arr.length; i++) {
			String s1 = arr[i];
			if (i <= 0) {
				sb.append(s1.toLowerCase());
			} else {
				sb.append(s1.substring(0, 1).toUpperCase() + s1.substring(1).toLowerCase());
			}
		}
		return sb.toString();
	}

}