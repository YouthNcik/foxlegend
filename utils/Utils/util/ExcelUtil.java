package com.zhph.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.StringUtils;

import com.zhph.commons.Constant;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.ExcelXorHtmlUtil;
import cn.afterturn.easypoi.excel.entity.ExcelToHtmlParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * @author 导表工具类 作者 E-mail: date 创建时间：2017-9-28 下午4:25:23
 * @version 1.0
 * @parameter
 * @return
 * @since
 */
public class ExcelUtil {
	private Log log = LogFactory.getLog(ExcelUtil.class);

	/**
	 * 复杂表头导出 使用方法参建demo
	 *
	 * @param colTitleAry
	 * @param colWidthAry
	 * @param convStr
	 * @param numColAry
	 * @param response
	 * @param fileName
	 * @param regions
	 * @param startRowIndex
	 * @throws Exception
	 */
	public synchronized void writeExecl(String[][] colTitleAry, short[] colWidthAry, Object[][] convStr, int[][] numColAry, HttpServletResponse response, String fileName, List<CellRangeAddress> regions, Integer startColIndex, Integer startRowIndex) throws Exception {
		String filePath = Constant.EXPORT_FILE_PATH + File.separator + fileName;
		FileUtil.deletePath(filePath);
		File file = new File(filePath);
		boolean delFlag = file.delete();
		if (!delFlag) {
			// log.error("删除失败");
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		int rowCount = 0;
		HSSFFont font = wb.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		String[] spanColTitleAry = colTitleAry[0];

		HSSFCellStyle colTitleStyle = createColTitleStyle(wb, font);
		HSSFRow lastRow = null;
		for (int i = 0; i < spanColTitleAry.length; i++) {
			HSSFRow row = null;
			HSSFCell cell = null;
			try {
				CellRangeAddress region = regions.get(i);
				sheet.addMergedRegion(region);
				if (sheet.getRow(region.getFirstRow()) == null) {
					row = sheet.createRow(region.getFirstRow());
					rowCount++;
				} else {
					row = sheet.getRow(region.getFirstRow());
				}
				row.setHeight((short) (30 * 16));
				cell = row.createCell(region.getFirstColumn());
			} catch (Exception e) {
				row = lastRow;
				row.setHeight((short) (30 * 16));
				cell = row.createCell(i);
			}
			lastRow = row;
			cell.setCellStyle(colTitleStyle);
			cell.setCellValue(spanColTitleAry[i]);
			sheet.setColumnWidth(i, (36 * colWidthAry[i]));
		}

		createColTitleRowBak(sheet, colTitleStyle, rowCount, colTitleAry[1], colWidthAry, startColIndex);
		this.writeExcel(wb, font, sheet, convStr, numColAry, rowCount, fileName, filePath, response, startRowIndex);
	}

	/**
	 * 读取exl
	 *
	 * @return
	 */
	public synchronized <T> List<T> readExecl(InputStream file, Class<?> clazz, ImportParams params) throws Exception {
		return ExcelImportUtil.importExcel(file, clazz, params);
	}

	/**
	 * exl转为html
	 *
	 * @param wb
	 */
	public synchronized void ExecltoHtml(Workbook wb) {
		ExcelToHtmlParams excelToHtmlParams = new ExcelToHtmlParams(wb);
		ExcelXorHtmlUtil.excelToHtml(excelToHtmlParams);
	}

	/**
	 * @param colTitleAry
	 *            表头
	 * @param colWidthAry
	 *            宽度
	 * @param convStr
	 *            内容
	 * @param numColAry
	 * @param response
	 * @param fileName
	 *            文件名
	 * @throws Exception
	 */

	public synchronized void writeExecl(String[] colTitleAry, short[] colWidthAry, Object[][] convStr, int[][] numColAry, HttpServletResponse response, String fileName, Integer startRowIndex) throws Exception {
		String filePath = Constant.EXPORT_FILE_PATH + File.separator + fileName;
		FileUtil.deletePath(filePath);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		int rowCount = 0;
		HSSFFont font = wb.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);

		HSSFCellStyle colTitleStyle = createColTitleStyle(wb, font);
		createColTitleRow(sheet, colTitleStyle, rowCount, colTitleAry, colWidthAry);
		this.writeExcel(wb, font, sheet, convStr, numColAry, rowCount, fileName, filePath, response, startRowIndex);
	}

	private void writeExcel(HSSFWorkbook wb, HSSFFont font, HSSFSheet sheet, Object[][] convStr, int[][] numColAry, int rowCount, String fileName, String filePath, HttpServletResponse response, Integer startRowIndex) throws IOException {
		HSSFCellStyle contentStyle = createDefaultStyle(wb, font);

		List<HSSFSheet> sheetsList = new ArrayList<HSSFSheet>();
		sheetsList.add(sheet);
		if (convStr != null && convStr.length > 0) {
			Double lenght = (double) convStr.length;
			Double sheetEnd = 65535.00;// sheet临界值
			int rowNum = (int) (lenght % sheetEnd);// 最后一页条数
			int sheetPage = (int) Math.ceil(lenght / sheetEnd);// 分sheet页数
			int rowC = 0;// 每页累加数

			for (int i = 1; i < sheetPage; i++) {
				sheetsList.add(wb.cloneSheet(0));
			}

			for (int i = 0; i < sheetPage; i++) {
				if (i == (sheetPage - 1)) {
					writeContent(sheetsList.get(i), contentStyle, convStr, numColAry, (i == 0) ? startRowIndex : 1, rowC, rowC + rowNum);// 如果是第一个sheet，则从startRowIndex行开始录入数据，否则从第一行开始
					break;
				}
				writeContent(sheetsList.get(i), contentStyle, convStr, numColAry, (i == 0) ? startRowIndex : 1, rowC, rowC + 65535);// 如果是第一个sheet，则从startRowIndex行开始录入数据，否则从第一行开始
				rowC = rowC + 65535;
			}
		}

		if (StringUtils.isEmpty(fileName)) {
			fileName = "my_excel";
		}

		FileUtil.mkDirs(filePath);
		String newFile = filePath + File.separator + fileName + ".xls";
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(newFile);
			wb.write(fos);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (null != wb) {
					wb.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String zipFile = filePath + File.separator + fileName + ".zip";
		ZipUtil.compress(newFile, zipFile);
		InputStream is = null;
		OutputStream os = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			response.setCharacterEncoding("utf-8");
			response.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8") + ".zip");
			is = new FileInputStream(zipFile);
			bis = new BufferedInputStream(is);
			os = response.getOutputStream();
			bos = new BufferedOutputStream(os);

			int read;
			byte[] bytes = new byte[8072];
			while ((read = bis.read(bytes, 0, bytes.length)) != -1) {
				bos.write(bytes, 0, read);
			}
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private static HSSFCellStyle createColTitleStyle(HSSFWorkbook wb, HSSFFont font) {
		HSSFCellStyle titleStyle = wb.createCellStyle();
		titleStyle.setFont(font);
		titleStyle.setBorderLeft((short) 1);
		titleStyle.setBorderRight((short) 1);
		titleStyle.setBorderTop((short) 1);
		titleStyle.setBorderBottom((short) 1);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titleStyle.setWrapText(true);
		titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titleStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		return titleStyle;
	}

	private static HSSFCellStyle createDefaultStyle(HSSFWorkbook wk, HSSFFont font) {
		HSSFCellStyle style = wk.createCellStyle();
		style.setBorderTop((short) 1);
		style.setBorderLeft((short) 1);
		style.setBorderBottom((short) 1);
		style.setBorderRight((short) 1);
		style.setWrapText(true);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		return style;
	}

	private static void createColTitleRow(HSSFSheet sheet, HSSFCellStyle style, int rowNum, String[] colTitleAry, short[] colWidthAry) {
		HSSFRow row = sheet.createRow(rowNum);
		row.setHeight((short) (30 * 16));
		for (int i = 0; i < colTitleAry.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(colTitleAry[i]);
			sheet.setColumnWidth(i, (36 * 150));
		}
	}

	private static void createColTitleRowBak(HSSFSheet sheet, HSSFCellStyle style, int rowNum, String[] colTitleAry, short[] colWidthAry, Integer startColIndex) {
		HSSFRow row = sheet.createRow(rowNum);
		row.setHeight((short) (30 * 16));
		startColIndex = startColIndex == null ? Integer.valueOf(0) : startColIndex;
		for (int i = 0; i < colTitleAry.length; i++) {
			HSSFCell cell = row.createCell(startColIndex + i);
			cell.setCellStyle(style);
			cell.setCellValue(colTitleAry[i]);
			sheet.setColumnWidth(i, (36 * colWidthAry[i]));
		}
	}

	private void writeContent(HSSFSheet sheet, HSSFCellStyle style, Object[][] convStr, int[][] numColAry, int rowCount, int off, int len) {
		for (int i = off; i < len; i++) {
			HSSFRow row = sheet.createRow(rowCount);
			for (int j = 0; j < convStr[i].length; j++) {
				HSSFCell cell = row.createCell(j);
				Object obj = convStr[i][j] == null ? "" : convStr[i][j];
				try {
					if (obj.getClass() == BigDecimal.class) {
						obj = "".equals(obj) ? "0" : obj;
						obj = ((BigDecimal) obj).doubleValue();
					}
					if (obj.getClass() == Double.class) {
						obj = "".equals(obj) ? "0" : obj;
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue((Double) obj);
					} else if (obj.getClass() == Integer.class) {
						obj = "".equals(obj) ? "0" : obj;
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue((Integer) obj);
					} else {
						cell.setCellValue((String) obj);
					}
				} catch (Exception e) {
					cell.setCellValue((String) obj);
				}
				cell.setCellStyle(style);
			}
			rowCount++;
		}
	}

	/**
	 * list去重， 保持顺序
	 *
	 * @param list
	 * @return
	 */
	public static <T> List<T> removeDuplicate(List<T> list) {
		Set<T> set = new HashSet<T>();
		List<T> newList = new ArrayList<T>();
		for (Iterator<T> iter = list.iterator(); iter.hasNext();) {
			T element = (T) iter.next();
			if (set.add(element)) {
				newList.add(element);
			}
		}
		return newList;
	}
}
