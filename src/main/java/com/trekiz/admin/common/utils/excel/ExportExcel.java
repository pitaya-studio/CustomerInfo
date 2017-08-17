/**
 *
 */
package com.trekiz.admin.common.utils.excel;

import java.awt.image.BufferedImage;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFBorderFormatting;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.Encodes;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.Reflections;
import com.trekiz.admin.common.utils.excel.annotation.ExcelField;
import com.trekiz.admin.modules.mtourfinance.json.CostJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.InComeJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.SettlementJsonBean;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;

/**
 * 导出Excel文件（导出“XLSX”格式，支持大数据量导出 @see org.apache.poi.ss.SpreadsheetVersion）
 * 
 * @author zj
 * @version 2013-11-19
 */
public class ExportExcel {

	private static Logger log = LoggerFactory.getLogger(ExportExcel.class);

	/**
	 * 工作薄对象
	 */
	private SXSSFWorkbook wb;

	/**
	 * 工作表对象
	 */
	private Sheet sheet;

	/**
	 * 样式列表
	 */
	private Map<String, CellStyle> styles;

	/**
	 * 当前行号
	 */
	private int rownum;

	/**
	 * 注解列表（Object[]{ ExcelField, Field/Method }）
	 */
	List<Object[]> annotationList = Lists.newArrayList();

	/**
	 * 构造函数
	 * 
	 * @param title
	 *            表格标题，传“空值”，表示无标题
	 * @param cls
	 *            实体对象，通过annotation.ExportField获取标题
	 */
	public ExportExcel(String title, Class<?> cls) {
		this(title, cls, 1);
	}

	/**
	 * 构造函数
	 * 
	 * @param title
	 *            表格标题，传“空值”，表示无标题
	 * @param cls
	 *            实体对象，通过annotation.ExportField获取标题
	 * @param type
	 *            导出类型（1:导出数据；2：导出模板）
	 * @param groups
	 *            导入分组
	 */
	public ExportExcel(String title, Class<?> cls, int type, int... groups) {
		// Get annotation field
		Field[] fs = cls.getDeclaredFields();
		for (Field f : fs) {
			ExcelField ef = f.getAnnotation(ExcelField.class);
			if (ef != null && (ef.type() == 0 || ef.type() == type)) {
				if (groups != null && groups.length > 0) {
					boolean inGroup = false;
					for (int g : groups) {
						if (inGroup) {
							break;
						}
						for (int efg : ef.groups()) {
							if (g == efg) {
								inGroup = true;
								annotationList.add(new Object[] { ef, f });
								break;
							}
						}
					}
				} else {
					annotationList.add(new Object[] { ef, f });
				}
			}
		}
		// Get annotation method
		Method[] ms = cls.getDeclaredMethods();
		for (Method m : ms) {
			ExcelField ef = m.getAnnotation(ExcelField.class);
			if (ef != null && (ef.type() == 0 || ef.type() == type)) {
				if (groups != null && groups.length > 0) {
					boolean inGroup = false;
					for (int g : groups) {
						if (inGroup) {
							break;
						}
						for (int efg : ef.groups()) {
							if (g == efg) {
								inGroup = true;
								annotationList.add(new Object[] { ef, m });
								break;
							}
						}
					}
				} else {
					annotationList.add(new Object[] { ef, m });
				}
			}
		}
		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField) o1[0]).sort())
						.compareTo(new Integer(((ExcelField) o2[0]).sort()));
			};
		});
		// Initialize
		List<String> headerList = Lists.newArrayList();
		for (Object[] os : annotationList) {
			String t = ((ExcelField) os[0]).title();
			// 如果是导出模板，则去掉注释
			if (type == 1) {
				String[] ss = StringUtils.split(t, "**", 2);
				if (ss.length == 2) {
					t = ss[0];
				}
			}
			headerList.add(t);
		}
		initialize(title, headerList);
	}

	/**
	 * 构造函数
	 * 
	 * @param title
	 *            表格标题，传“空值”，表示无标题
	 * @param headers
	 *            表头数组
	 */
	public ExportExcel(String title, String[] headers) {
		initialize(title, Lists.newArrayList(headers));
	}

	/**
	 * 构造函数
	 * 
	 * @param title
	 *            表格标题，传“空值”，表示无标题
	 * @param headerList
	 *            表头列表
	 */
	public ExportExcel(String title, List<String> headerList) {
		initialize(title, headerList);
	}

	/**
	 * 初始化函数
	 * 
	 * @param title
	 *            表格标题，传“空值”，表示无标题
	 * @param headerList
	 *            表头列表
	 */
	private void initialize(String title, List<String> headerList) {
		this.wb = new SXSSFWorkbook(500);
		this.sheet = wb.createSheet("Export");
		this.styles = createStyles(wb);
		// Create title
		if (StringUtils.isNotBlank(title)) {
			Row titleRow = sheet.createRow(rownum++);
			titleRow.setHeightInPoints(30);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(styles.get("title"));
			titleCell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(),
					titleRow.getRowNum(), titleRow.getRowNum(), headerList
							.size() - 1));
		}
		// Create header
		if (headerList == null) {
			throw new RuntimeException("headerList not null!");
		}
		Row headerRow = sheet.createRow(rownum++);
		headerRow.setHeightInPoints(16);
		for (int i = 0; i < headerList.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellStyle(styles.get("header"));
			String[] ss = StringUtils.split(headerList.get(i), "**", 2);
			if (ss.length == 2) {
				cell.setCellValue(ss[0]);
				Comment comment = this.sheet.createDrawingPatriarch()
						.createCellComment(
								new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3,
										(short) 5, 6));
				comment.setString(new XSSFRichTextString(ss[1]));
				cell.setCellComment(comment);
			} else {
				cell.setCellValue(headerList.get(i));
			}
			sheet.autoSizeColumn(i);
		}
		for (int i = 0; i < headerList.size(); i++) {
			int colWidth = sheet.getColumnWidth(i) * 2;
			sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
		}
		log.debug("Initialize success.");
	}

	/**
	 * 创建表格样式
	 * 
	 * @param wb
	 *            工作薄对象
	 * @return 样式列表
	 */
	private Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font titleFont = wb.createFont();
		titleFont.setFontName("Arial");
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(titleFont);
		styles.put("title", style);

		style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		Font dataFont = wb.createFont();
		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 10);
		style.setFont(dataFont);
		styles.put("data", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_LEFT);
		styles.put("data1", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_CENTER);
		styles.put("data2", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		styles.put("data3", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		// style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font headerFont = wb.createFont();
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(headerFont);
		styles.put("header", style);

		return styles;
	}

	/**
	 * 添加一行
	 * 
	 * @return 行对象
	 */
	public Row addRow() {
		return sheet.createRow(rownum++);
	}

	/**
	 * 添加一个单元格
	 * 
	 * @param row
	 *            添加的行
	 * @param column
	 *            添加列号
	 * @param val
	 *            添加值
	 * @return 单元格对象
	 */
	public Cell addCell(Row row, int column, Object val) {
		return this.addCell(row, column, val, 0, Class.class);
	}

	/**
	 * 添加一个单元格
	 * 
	 * @param row
	 *            添加的行
	 * @param column
	 *            添加列号
	 * @param val
	 *            添加值
	 * @param align
	 *            对齐方式（1：靠左；2：居中；3：靠右）
	 * @return 单元格对象
	 */
	public Cell addCell(Row row, int column, Object val, int align,
			Class<?> fieldType) {
		Cell cell = row.createCell(column);
		CellStyle style = styles.get("data"
				+ (align >= 1 && align <= 3 ? align : ""));
		try {
			if (val == null) {
				cell.setCellValue("");
			} else if (val instanceof String) {
				cell.setCellValue((String) val);
			} else if (val instanceof Integer) {
				cell.setCellValue((Integer) val);
			} else if (val instanceof Long) {
				cell.setCellValue((Long) val);
			} else if (val instanceof Double) {
				cell.setCellValue((Double) val);
			} else if (val instanceof Float) {
				cell.setCellValue((Float) val);
			} else if (val instanceof Date) {
				DataFormat format = wb.createDataFormat();
				style.setDataFormat(format.getFormat("yyyy-MM-dd"));
				cell.setCellValue((Date) val);
			} else {
				if (fieldType != Class.class) {
					cell.setCellValue((String) fieldType.getMethod("setValue",
							Object.class).invoke(null, val));
				} else {
					cell.setCellValue((String) Class
							.forName(
									this.getClass()
											.getName()
											.replaceAll(
													this.getClass()
															.getSimpleName(),
													"fieldtype."
															+ val.getClass()
																	.getSimpleName()
															+ "Type"))
							.getMethod("setValue", Object.class)
							.invoke(null, val));
				}
			}
		} catch (Exception ex) {
			log.info("Set cell value [" + row.getRowNum() + "," + column
					+ "] error: " + ex.toString());
			cell.setCellValue(val.toString());
		}
		cell.setCellStyle(style);
		return cell;
	}

	/**
	 * 添加数据（通过annotation.ExportField添加数据）
	 * 
	 * @return list 数据列表
	 */
	public <E> ExportExcel setDataList(List<E> list) {
		for (E e : list) {
			int colunm = 0;
			Row row = this.addRow();
			StringBuilder sb = new StringBuilder();
			for (Object[] os : annotationList) {
				ExcelField ef = (ExcelField) os[0];
				Object val = null;
				// Get entity value
				try {
					if (StringUtils.isNotBlank(ef.value())) {
						val = Reflections.invokeGetter(e, ef.value());
					} else {
						if (os[1] instanceof Field) {
							val = Reflections.invokeGetter(e,
									((Field) os[1]).getName());
						} else if (os[1] instanceof Method) {
							val = Reflections.invokeMethod(e,
									((Method) os[1]).getName(), new Class[] {},
									new Object[] {});
						}
					}
					// If is dict, get dict label
					if (StringUtils.isNotBlank(ef.dictType())) {
						val = DictUtils.getDictLabel(
								val == null ? "" : val.toString(),
								ef.dictType(), "");
					}
				} catch (Exception ex) {
					// Failure to ignore
					log.info(ex.toString());
					val = "";
				}
				this.addCell(row, colunm++, val, ef.align(), ef.fieldType());
				sb.append(val + ", ");
			}
			log.debug("Write success: [" + row.getRowNum() + "] "
					+ sb.toString());
		}
		return this;
	}

	/**
	 * 输出数据流
	 * 
	 * @param os
	 *            输出数据流
	 */
	public ExportExcel write(OutputStream os) throws IOException {
		wb.write(os);
		return this;
	}

	/**
	 * 输出到客户端
	 * 
	 * @param fileName
	 *            输出文件名
	 */
	public ExportExcel write(HttpServletResponse response, String fileName)
			throws IOException {
		response.reset();
		response.setContentType("application/octet-stream; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ Encodes.urlEncode(fileName));
		write(response.getOutputStream());
		return this;
	}

	/**
	 * 输出到文件
	 * 
	 * @param fileName
	 *            输出文件名
	 */
	public ExportExcel writeFile(String name) throws FileNotFoundException,
			IOException {
		FileOutputStream os = new FileOutputStream(name);
		this.write(os);
		return this;
	}

	/**
	 * 清理临时文件
	 */
	public ExportExcel dispose() {
		wb.dispose();
		return this;
	}

	/**
	 * 导出图片到excel文件
	 */
	public static void ExportImageToExcel(HSSFWorkbook wb, HSSFSheet sheet,
			HttpServletRequest request) {
		BufferedImage bufferImg = null;// 图片一
		try {
			// 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			// 将两张图片读到BufferedImage
			String realPath = request.getSession().getServletContext()
					.getRealPath("/WEB-INF/image/luozijiaqi.jpg");
			bufferImg = ImageIO.read(new File(realPath));
			ImageIO.write(bufferImg, "png", byteArrayOut);
			// 在表中创建图片
			HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
			/**
			 * 该构造函数有8个参数 前四个参数是控制图片在单元格的位置，分别是图片距离单元格left，top，right，bottom的像素距离
			 * 后四个参数，前连个表示图片左上角所在的cellNum和 rowNum，后天个参数对应的表示图片右下角所在的cellNum和
			 * rowNum， excel中的cellNum和rowNum的index都是从0开始的
			 * 
			 */
			// 将图片都出到单元格
			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0,
					(short) 0, 1, (short) 4, 6);

			// 插入图片
			patriarch
					.createPicture(anchor, wb.addPicture(
							byteArrayOut.toByteArray(),
							HSSFWorkbook.PICTURE_TYPE_JPEG));

		} catch (IOException io) {
			io.printStackTrace();
			System.out.println("io erorr : " + io.getMessage());
		}
	}

	/**
	 * 522需求--
	 * 
	 * @param fileName
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void exportOfficeActivityExcel(String fileName,List<Object[]> resultList,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HSSFWorkbook workBook = new HSSFWorkbook();
		int rowNum = 0;
		// 创建一个sheet表
		HSSFSheet sheet = workBook.createSheet("sheet1");
		sheet.setDefaultColumnWidth(12);
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeight((short) 600);

		String[] firstRowTitle = { "批发商名称", "产品名称", "团号", "出团日期", "出发城市",
				"同行价", " ", " ", "定价策略", " ", " " };
		String[] secondRowTitle = { "成人价", "儿童价", "特殊人群价" };

		// 创建行
		HSSFRow titleRow = sheet.createRow((short) rowNum++);

		// 字体设置
		HSSFFont font = workBook.createFont();
		font.setFontName("宋体");
		font.setColor(HSSFFont.COLOR_NORMAL);

		// 样式
		HSSFCellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setFont(font);
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex()); // 背景色-灰色
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		getNewCenterStyle(cellStyle);
		// 设置第一行表头
		for (int i = 0; i < firstRowTitle.length; i++) {
			HSSFCell rowFirstCell = titleRow.createCell(i);
			rowFirstCell.setCellStyle(cellStyle);
			rowFirstCell.setCellValue(firstRowTitle[i]);
		}
		HSSFRow secondRow = sheet.createRow(rowNum++);
		// 设置第二行的价格
		for (int i = 0; i < firstRowTitle.length; i++) {
			HSSFCell rowFirstCell = secondRow.createCell(i);
			rowFirstCell.setCellStyle(cellStyle);
		}
		for (int a = 0; a < secondRowTitle.length; a++) {
			secondRow.getCell(5 + a).setCellValue(secondRowTitle[a]);
		}
		for (int a = 0; a < secondRowTitle.length; a++) {
			secondRow.getCell(8 + a).setCellValue(secondRowTitle[a]);
		}

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 7));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 10));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
		//填充数据
		for(Object[] o : resultList){
			int length = o.length - 1;
			HSSFRow valueRow = sheet.createRow(rowNum++);
			for(int a = 0; a < firstRowTitle.length; a++){
				HSSFCell valueCell = valueRow.createCell(a);
				valueCell.setCellValue(o[a] == null ? "" : o[a].toString());
			}
			
			List<Object[]> listPrice = (List<Object[]>) o[10];
			for(Object[] obj : listPrice){
				int b = length++;
				for(int c = 0 ; c< obj.length;c++){
					HSSFCell valueCell = valueRow.createCell(b+c);	
					valueCell.setCellValue(obj[c] == null ? "" : obj[c].toString());	
				}
			}
		}
		// 导出excel文档
		exportExcel(fileName, workBook, request, response);
	}

	/**
	 * 530导出询价记录
	 * 
	 * @param fileName
	 * @param activityList
	 * @param list
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void excelEstimateExcel(String fileName,
			List<Object[]> activityList, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 标题行数据
		String[] firstTitle = { "序号", "销售", "询价日期", "询价客户", "预计出团日期", "国家", "计调",
				"报价状态" };
		int rowNum = 0;
		// 创建文件
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建sheet表
		HSSFSheet sheet = createSheet(workbook, fileName);
		// 创建行
		HSSFRow titleRow = sheet.createRow((short) rowNum++);
		// 调用默认（自定义的）电脑格样式表
		HSSFCellStyle cellStyle = createExcelDefaultStyle(workbook);
		// 输出第一行
		for (int i = 0; i < firstTitle.length; i++) {
			HSSFCell titleCell = titleRow.createCell(i);
			titleCell.setCellStyle(cellStyle);
			titleCell.setCellValue(firstTitle[i]);
		}
		// 输出第二行。。。
		for (int i = 0; i < activityList.size(); i++) {
			// 根据list集合的长度创建数组
			HSSFRow valueRow = sheet.createRow(i + 1);
			Object[] objects = activityList.get(i);
			// 根据数组的长度创建单元格并为单元格赋值
			HSSFCell valueCell1 = valueRow.createCell(0); // 每一行的第一个单元格为序号，需要为其单独创建单元格并赋值
			valueCell1.setCellValue(i + 1);
			for (int j = 0; j < objects.length; j++) {
				HSSFCell valueCell = valueRow.createCell(j + 1);
				valueCell.setCellValue(objects[j].toString());
			}
		}
		// 导出excel文件
		exportExcel(fileName, workbook, request, response);

	}

	/**
	 * 为sheet表设置默认的单元格样式
	 * 
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle createExcelDefaultStyle(HSSFWorkbook workbook) {
		// 字体设置
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setColor(HSSFFont.COLOR_NORMAL);
		// 样式
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setFont(font);
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex()); // 背景色-灰色
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		getNewCenterStyle(cellStyle);
		return cellStyle;
	}

	/**
	 * 导出excel文件
	 * 
	 * @param fileName
	 * @param workbook
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void exportExcel(String fileName, HSSFWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// 导出excel文档
		OutputStream op = null;
		fileName = fileName + ".xls";
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		setFileDownloadHeader(request, response, fileName);

		op = response.getOutputStream();
		workbook.write(op);
		op.close();
	}

	/**
	 * new 544-导出团空单(new)
	 * 
	 * @param fileName
	 * @param Obejcts
	 * @param list
	 * @param request
	 * @param response
	 */
	public static void newExportExcel(String fileName, String groupCode,
			String[] countrys, List<Object[]> list, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String country = "";
			for (int i = 0; i < countrys.length; i++) {
				country = country + " " + countrys[i];
			}
			String[] title = { "名称", "人数", "姓名", "性别", "英文名", "出生日期", "出生地",
					"护照号", "签发地", "签发日期", "护照有效期", "备注", "电话", "房间分配" };
			int rowNum = 0;
			HSSFWorkbook workBook = new HSSFWorkbook();
			HSSFSheet sheet = createSheet(workBook, fileName);
			ExportImageToExcel(workBook, sheet, request);

			// 创建title行
			HSSFRow titleRow = sheet.createRow((short) rowNum + 6);

			// 全局样式、字体设置
			HSSFFont font = workBook.createFont();
			font.setFontName("宋体");
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setColor(HSSFFont.COLOR_NORMAL);

			HSSFCellStyle allCellStyle = workBook.createCellStyle();
			allCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			allCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			allCellStyle.setFont(font);
			allCellStyle.setBorderBottom(HSSFCellStyle.BORDER_HAIR); // 下边框
			allCellStyle.setBorderLeft(HSSFCellStyle.BORDER_HAIR);// 左边框
			allCellStyle.setBorderTop(HSSFCellStyle.BORDER_HAIR);// 上边框
			allCellStyle.setBorderRight(HSSFCellStyle.BORDER_HAIR);// 右边框

			// 标题行颜色、样式
			HSSFCellStyle cellStyle = workBook.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cellStyle.setFont(font);
			cellStyle
					.setFillForegroundColor(IndexedColors.DARK_TEAL.getIndex()); // 背景色-灰色
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_HAIR);// 左边框
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_HAIR);// 右边框
			// 设置表头的字体颜色
			HSSFFont font1 = workBook.createFont();
			font1.setColor(IndexedColors.WHITE.getIndex());
			font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			cellStyle.setFont(font1);

			getNewCenterStyle(cellStyle);
			// 创建标题行并为其赋值和样式
			for (int i = 0; i < title.length; i++) {
				HSSFCell titleCell = titleRow.createCell(i);
				titleCell.setCellValue(title[i]);
				titleCell.setCellStyle(cellStyle);
			}

			HSSFRow firstTitleRow1 = sheet.createRow((short) 2);
			HSSFRow firstTitleRow2 = sheet.createRow((short) 3);
			HSSFRow firstTitleRow3 = sheet.createRow((short) 5);

			HSSFCell createCell1 = firstTitleRow1.createCell(7);
			HSSFCell createCell2 = firstTitleRow2.createCell(7);
			HSSFCell createCell3 = firstTitleRow3.createCell(7);

			HSSFCellStyle titleTableCellStyle = workBook.createCellStyle();
			titleTableCellStyle.setFont(font);
			titleTableCellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_LEFT);

			createCell1.setCellValue("团   号： " + groupCode);
			createCell1.setCellStyle(titleTableCellStyle);
			createCell2.setCellValue("航空公司： ");
			createCell2.setCellStyle(titleTableCellStyle);
			createCell3.setCellValue("前往国家： " + country);
			createCell3.setCellStyle(titleTableCellStyle);
			sheet.addMergedRegion(new CellRangeAddress(2, 2, 7, 13));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 7, 13));
			sheet.addMergedRegion(new CellRangeAddress(4, 4, 7, 13));
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 7, 13));
			// 为表一填入数据
			for (int a = 0; a < list.size(); a++) {
				Object[] objects = list.get(a);
				HSSFRow valueRow = sheet.createRow(rowNum + 7 + a);
				int length = title.length;
				for (int b = 0; b < length; b++) {
					HSSFCell valueCell = valueRow.createCell(b);
					if (objects[b] == null) {
						valueCell.setCellValue(" ");
					} else {
						valueCell.setCellValue(objects[b].toString());
					}
					// 自动设置序号列 排序
					HSSFCell cell = valueRow.createCell(1);
					cell.setCellValue(a + 1);

					cell.setCellStyle(allCellStyle);
					valueCell.setCellStyle(allCellStyle);
				}
			}

			// 创建 “房间数：。。。。。”行
			int lastRowNum = sheet.getLastRowNum();
			createNoValueRow(sheet, lastRowNum + 1, title.length);
			for (int i = 0; i < title.length; i++) {
				setBorder(workBook, sheet, lastRowNum, i, "bottom",
						HSSFCellStyle.BORDER_THICK);
				setBorder(workBook, sheet, lastRowNum + 1, i, "bottom",
						HSSFCellStyle.BORDER_HAIR);
			}
			// 创建空白行
			int lastRowNum2 = sheet.getLastRowNum() + 1;
			createNoValueRow(sheet, lastRowNum2, title.length);
			// 创建空白行 -- 作为表2 的表头
			int lastRowNum3 = sheet.getLastRowNum() + 1;

			createNoValueRow(sheet, lastRowNum3, title.length);
			HSSFRow row = sheet.getRow(lastRowNum3);
			HSSFCell table2TitleCell = row.getCell(0);
			table2TitleCell.setCellValue("单地接");
			HSSFCellStyle alignCenterStyle = workBook.createCellStyle();
			alignCenterStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			alignCenterStyle.setFont(font);
			alignCenterStyle
					.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			table2TitleCell.setCellStyle(alignCenterStyle);

			// 创建表2 的 标题行
			int lastRowNum4 = sheet.getLastRowNum() + 1;
			HSSFRow table2Row = sheet.createRow(lastRowNum4);
			for (int i = 0; i < title.length; i++) {
				HSSFCell titleCell = table2Row.createCell(i);
				titleCell.setCellValue(title[i]);
				titleCell.setCellStyle(cellStyle);
			}
			int lastRowNum5 = sheet.getLastRowNum();
			for (int i = 7; i < lastRowNum5; i++) {
				setBorder(workBook, sheet, i, title.length - 1, "right",
						HSSFCellStyle.BORDER_THICK);
			}
			setBorder(workBook, sheet, lastRowNum, title.length - 1, "right",
					HSSFCellStyle.BORDER_THICK);
			sheet.getRow(lastRowNum).getCell(title.length - 1).getCellStyle()
					.setBorderBottom(HSSFCellStyle.BORDER_THICK);
			HSSFCellStyle lastCellStyle = workBook.createCellStyle();
			lastCellStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
			for (int s = 2; s < 4; s++) {
				sheet.getRow(lastRowNum + s).getCell(title.length - 1)
						.setCellStyle(lastCellStyle);
			}
			exportExcel(fileName, workBook, request, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 绘制边框 输入单元格坐标和要设置的边框位置
	 * 
	 * @param sheet
	 * @param rowNum
	 * @param cellNum
	 * @param cellStyle
	 * @param border
	 */
	public static void setBorder(HSSFWorkbook workBook, HSSFSheet sheet,
			int rowNum, int cellNum, String border, short borderLineStyle) {
		// 创建字体样式
		HSSFFont font = workBook.createFont();
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFFont.COLOR_NORMAL);
		// 创建单元格样式
		HSSFCellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setFont(font);
		if (border == "bottom") {
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_HAIR); // 下边框
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_HAIR);// 左边框
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_HAIR);// 上边框
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_HAIR);// 右边框
		} else if (border == "right") {
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_HAIR); // 下边框
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_HAIR);// 左边框
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_HAIR);// 上边框
		}

		HSSFRow row = sheet.getRow(rowNum);
		HSSFCell cell = row.getCell(cellNum);

		if (border == "top") {
			cellStyle.setBorderTop(borderLineStyle);// 上边框
			cell.setCellStyle(cellStyle);
		} else if (border == "bottom") {
			cellStyle.setBorderBottom(borderLineStyle); // 下边框
			cell.setCellStyle(cellStyle);
		} else if (border == "left") {
			cellStyle.setBorderLeft(borderLineStyle);// 左边框
			cell.setCellStyle(cellStyle);
		} else if (border == "right") {
			cellStyle.setBorderRight(borderLineStyle);// 右边框
			cell.setCellStyle(cellStyle);
		}

	}

	/**
	 * 创建空白行，并合并空白行
	 * 
	 * @param sheet
	 * @param rowNum
	 * @param cellNum
	 */
	public static void createNoValueRow(HSSFSheet sheet, int rowNum, int cellNum) {
		HSSFRow roomNumRow = sheet.createRow(rowNum);
		for (int i = 0; i < cellNum; i++) {
			HSSFCell titleCell = roomNumRow.createCell(i);
			titleCell.setCellValue(" ");
		}
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0,
				cellNum - 1));
	}

	/**
	 * 为workbook创建sheet1表，并设置行高和列宽
	 * 
	 * @param workBook
	 * @param fileName
	 * @return
	 */
	public static HSSFSheet createSheet(HSSFWorkbook workBook, String fileName) {
		// 创建一个sheet表
		HSSFSheet sheet = workBook.createSheet("sheet1");
		sheet.setDefaultColumnWidth(12);
		sheet.autoSizeColumn(1);
		sheet.setDefaultRowHeight((short) 410);

		setPersonalPara(fileName, sheet);
		return sheet;
	}

	/**
	 * 团控单的设计表--0507
	 * 
	 * @param fileName
	 * @param list
	 * @param firstRowTitle
	 * @param secondRowTitle
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author zhanyu.gu
	 */
	public static void createGroupExcel(String fileName, Object[] Obejcts,
			List<Object[]> list, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 第一行、第二行的标题
		// String[] firstRowTitle = {"团号:","同行价:","领队"};
		String[] secondRowTitle = { "序号", "销售", "渠道名称", "游客", "性别", "人员关系",
				"联系方式", "美签", "加签", "定金", "团款", "押金", "特殊需求", "报名时间", "护照所在",
				"住房" };
		int rowNum = 0;
		// 穿件一个excel文件
		HSSFWorkbook workBook = new HSSFWorkbook();
		// 创建一个sheet表
		HSSFSheet sheet = workBook.createSheet("sheet1");
		sheet.setDefaultColumnWidth(12);
		sheet.autoSizeColumn(1);
		sheet.setDefaultRowHeight((short) 390);

		setPersonalPara(fileName, sheet);
		// 创建行
		HSSFRow titleRow = sheet.createRow((short) rowNum++);

		// 字体设置
		HSSFFont font = workBook.createFont();
		font.setFontName("宋体");
		font.setColor(HSSFFont.COLOR_NORMAL);
		// 样式
		HSSFCellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setFont(font);
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex()); // 背景色-灰色
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		getNewCenterStyle(cellStyle);

		// 设置第一行
		HSSFCell titleCell1 = titleRow.createCell(0);

		titleCell1.setCellStyle(cellStyle);
		titleCell1.setCellValue("团号：");
		HSSFCell titleCell2 = titleRow.createCell(2);
		titleCell2.setCellStyle(cellStyle);
		HSSFCell titleCell3 = titleRow.createCell(7);
		titleCell3.setCellStyle(cellStyle);
		titleCell3.setCellValue("同行价：");
		HSSFCell titleCell4 = titleRow.createCell(9);
		titleCell4.setCellStyle(cellStyle);
		// 设置第二行
		HSSFRow rowTwo = sheet.createRow(rowNum++);

		HSSFFont rowTwoCellFont = workBook.createFont();
		rowTwoCellFont.setFontName("宋体");
		rowTwoCellFont.setColor(HSSFFont.COLOR_NORMAL);
		rowTwoCellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		HSSFCellStyle rowTwoCellStyle = workBook.createCellStyle();
		rowTwoCellStyle.setFont(rowTwoCellFont);
		rowTwoCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		rowTwoCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		getNewCenterStyle(rowTwoCellStyle);

		for (int i = 0; i < secondRowTitle.length; i++) {
			HSSFCell rowTwoCell = rowTwo.createCell(i);
			rowTwoCell.setCellStyle(rowTwoCellStyle);
			rowTwoCell.setCellValue(secondRowTitle[i]);
		}
		// 设置第三行
		HSSFRow rowThird = sheet.createRow(rowNum++);

		HSSFFont rowThirdCellFont = workBook.createFont();
		rowThirdCellFont.setFontName("宋体");
		rowThirdCellFont.setColor(HSSFFont.COLOR_NORMAL);

		HSSFFont rowThirdCellFont1 = workBook.createFont();
		rowThirdCellFont1.setFontName("宋体");
		rowThirdCellFont1.setColor(HSSFFont.COLOR_RED);

		HSSFCellStyle rowThreeCellStyle = workBook.createCellStyle();
		rowThreeCellStyle.setFont(rowThirdCellFont);
		rowThreeCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		rowThreeCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		getNewCenterStyle(rowThreeCellStyle);
		HSSFCellStyle rowThreeCellStyle1 = workBook.createCellStyle();
		rowThreeCellStyle1.setFont(rowThirdCellFont1);
		rowThreeCellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		rowThreeCellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		getNewCenterStyle(rowThreeCellStyle1);

		HSSFCell thirdRowCell1 = rowThird.createCell(0);
		thirdRowCell1.setCellStyle(rowThreeCellStyle1);
		thirdRowCell1.setCellValue("1");
		HSSFCell thirdRowCell2 = rowThird.createCell(1);
		thirdRowCell2.setCellStyle(rowThreeCellStyle);
		thirdRowCell2.setCellValue("领队:");
		HSSFCell thirdRowCell3 = rowThird.createCell(3);
		thirdRowCell3.setCellStyle(rowThreeCellStyle);
		// 设置金额对齐向左对齐
		HSSFCellStyle moneycellStyle = workBook.createCellStyle();
		moneycellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		moneycellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		getNewCenterStyle(moneycellStyle);

		// 填充第一、三行的数据
		/*
		 * for (int a = 0; a < activityList.size(); a++) { Object[] ob =
		 * activityList.get(a); titleCell2.setCellValue(ob[0].toString());
		 * titleCell4.setCellValue(ob[1].toString());
		 * thirdRowCell3.setCellValue(ob[2].toString()); }
		 */
		// 填充第四行之后的数据
		Map<Integer, Object> idmap = new HashMap<Integer, Object>();
		for (int j = 0; j < list.size(); j++) {
			Object[] o = (Object[]) list.get(j);
			HSSFRow dataRow = sheet.createRow(rowNum++);
			for (int n = 1; n < o.length - 2; n++) {
				// 给第一列--序号列设置数值和样式
				HSSFCell dataCel1 = dataRow.createCell(0);
				dataCel1.setCellStyle(rowThreeCellStyle1);
				dataCel1.setCellValue(String.valueOf(rowNum - 2));
				// 给第一列之后的列赋值(2,3,4.....)
				HSSFCell dataCel = dataRow.createCell(n);
				dataCel.setCellStyle(rowThreeCellStyle);

				if (o[n + 2] != null) {
					dataCel.setCellValue(o[n + 2].toString());
				}

				Object pid = o[0];
				idmap.put(rowNum - 1, pid);
			}
			dataRow.getCell(9).setCellStyle(moneycellStyle);
			dataRow.getCell(10).setCellStyle(moneycellStyle);

			// 替换字符创中的","
			HSSFCell dataCel2 = dataRow.getCell(2);
			String agentInfo1 = getCellValue(dataCel2);
			if (StringUtils.isNotBlank(agentInfo1)) {
				String replaceAll = agentInfo1.replaceAll(",", " ");
				dataCel2.setCellValue(replaceAll);
			} else {
				dataCel2.setCellValue("");
			}

		}
		// 补边框
		HSSFCellStyle newLeftStyle = workBook.createCellStyle();
		newLeftStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// getNewCenterStyle(newLeftStyle);
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
		sheet.getRow(0).getCell(0).setCellStyle(rowThreeCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 6));
		sheet.getRow(0).getCell(2).setCellStyle(rowThreeCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));
		sheet.getRow(0).getCell(7).setCellStyle(rowThreeCellStyle);
		sheet.getRow(0).createCell(16).setCellStyle(newLeftStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 9, 15));
		sheet.getRow(0).getCell(9).setCellStyle(rowThreeCellStyle);
		sheet.getRow(0).getCell(9).setCellStyle(moneycellStyle);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 2));
		sheet.getRow(2).getCell(1).setCellStyle(rowThreeCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 3, 15));
		sheet.getRow(2).getCell(3).setCellStyle(rowThreeCellStyle);
		sheet.getRow(2).createCell(16).setCellStyle(newLeftStyle);
		// 读取表格中的信息
		for (int j = 3; j < sheet.getLastRowNum(); j++) {
			// 在合并之前首先判断是否属于同一笔订单的信息
			if (idmap.get(j) != null && idmap.get(j).equals(idmap.get(j + 1))
					|| idmap.get(j) == idmap.get(j + 1)) {
				// 合并销售列
				sheet.addMergedRegion(new CellRangeAddress(j, j + 1, 1, 1));
				// 合并渠道列
				sheet.addMergedRegion(new CellRangeAddress(j, j + 1, 2, 2));
				// 合并定金列
				sheet.addMergedRegion(new CellRangeAddress(j, j + 1, 9, 9));
				// 合并团款列
				sheet.addMergedRegion(new CellRangeAddress(j, j + 1, 10, 10));
				// 合并日期
				sheet.addMergedRegion(new CellRangeAddress(j, j + 1, 13, 13));
			}
		}
		ExportImageToExcel(workBook, sheet, request);
		// 导出excel文档
		OutputStream op = null;
		fileName = fileName + ".xls";
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		setFileDownloadHeader(request, response, fileName);

		op = response.getOutputStream();
		workBook.write(op);
		op.close();
	}

	/**
	 * 定制单元格边框
	 * 
	 */
	private static void getNewCenterStyle(HSSFCellStyle style) {

		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);

		/* return style; */
	}

	/**
	 * 用于获取excel表中指定单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellValue(HSSFCell cell) {
		if (cell == null) {
			return null;
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getRichStringCellValue().getString().trim();
		case Cell.CELL_TYPE_NUMERIC:
			return String.valueOf(cell.getNumericCellValue());
		case Cell.CELL_TYPE_BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		default:
			return null;
		}
	}

	/**
	 * 生成、下载Excel文件(统计用)
	 * 
	 * @param fileName
	 *            ：文件名称
	 * @param list
	 *            ：要生成数据
	 * @param cellTitle
	 *            ：Excle各列名称
	 * @param firstTitle
	 *            ：Excel首行标题
	 */
	public static void createExcle(String fileName, List<Object[]> list,
			String[] cellTitle, String firstTitle, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int rowNum = 0;
		// 创建一个excel文件
		HSSFWorkbook workBook = new HSSFWorkbook();
		// 创建一个sheet表
		HSSFSheet sheet = workBook.createSheet("sheet1");
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeight((short) 390);

		setPersonalPara(fileName, sheet);

		if ("供应商交易服务费信息".equals(fileName)) {
			sheet.setColumnWidth(4, 100 * 20);
			sheet.setColumnWidth(6, 100 * 20);
			sheet.setColumnWidth(8, 100 * 20);
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 4, 5));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 7));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 8, 9));
		} else if (fileName.indexOf("价格策略设置情况统计表") != -1) {
			sheet.setColumnWidth(0, 100 * 20);
			sheet.setColumnWidth(1, 100 * 80);
			sheet.setColumnWidth(2, 100 * 80);
			sheet.setColumnWidth(3, 100 * 80);
			sheet.setColumnWidth(4, 100 * 32);
			sheet.setColumnWidth(5, 100 * 20);
			sheet.setColumnWidth(6, 100 * 35);
			sheet.setColumnWidth(7, 100 * 35);
			sheet.setColumnWidth(8, 100 * 35);
			sheet.setColumnWidth(9, 100 * 35);
			sheet.setColumnWidth(10, 100 * 35);
			sheet.setColumnWidth(11, 100 * 35);
			sheet.setColumnWidth(12, 100 * 35);
			sheet.setColumnWidth(13, 100 * 35);
			sheet.setColumnWidth(14, 100 * 35);
			sheet.setColumnWidth(15, 100 * 35);
			sheet.setColumnWidth(16, 100 * 35);
			sheet.setColumnWidth(17, 100 * 35);
			sheet.setColumnWidth(18, 100 * 35);
			sheet.setColumnWidth(19, 100 * 35);
			sheet.setColumnWidth(20, 100 * 35);
			// 合并单元格
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 8));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 9, 11));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 12, 14));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 15, 17));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 18, 20));
		}
		// 创建行
		HSSFRow titleRow = sheet.createRow((short) rowNum++);
		// 字体设置
		HSSFFont font = workBook.createFont();
		font.setFontName("宋体");
		font.setFontHeight((short) HSSFFont.BOLDWEIGHT_NORMAL);
		font.setColor(HSSFFont.COLOR_NORMAL);

		// 样式
		HSSFCellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setFont(font);

		short countColumn = 0;
		if (cellTitle != null) {
			countColumn = (short) cellTitle.length;
		}
		HSSFCell titleCell = titleRow.createCell((countColumn / 2));
		titleRow.setHeight((short) 500);// 设置首行高度
		titleCell.setCellValue(firstTitle);
		titleCell.setCellStyle(cellStyle);
		// 设置第二行
		HSSFRow rowTwo = sheet.createRow(rowNum++);

		HSSFFont rowTwoCellFont = workBook.createFont();
		rowTwoCellFont.setFontName("宋体");
		rowTwoCellFont.setColor(HSSFFont.COLOR_NORMAL);

		HSSFCellStyle rowTwoCellStyle = workBook.createCellStyle();
		rowTwoCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		rowTwoCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		rowTwoCellStyle.setFont(rowTwoCellFont);

		for (int i = 0; i < cellTitle.length; i++) {
			HSSFCell rowTwoCell = rowTwo.createCell(i);
			rowTwoCell.setCellStyle(rowTwoCellStyle);
			rowTwoCell.setCellValue(cellTitle[i]);
		}

		for (int j = 0; j < list.size(); j++) {
			Object[] o = (Object[]) list.get(j);
			HSSFRow dataRow = sheet.createRow(rowNum++);
			for (int n = 0; n < o.length; n++) {
				HSSFCell dataCel = dataRow.createCell(n);
				dataCel.setCellStyle(rowTwoCellStyle);
				if (o[n] != null) {
					dataCel.setCellValue(o[n].toString());
				}
			}
		}
		// 导出excel文档
		OutputStream op = null;
		fileName = fileName + ".xls";
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		setFileDownloadHeader(request, response, fileName);

		op = response.getOutputStream();
		workBook.write(op);
		op.close();
	}

	private static void setPersonalPara(String fileName, HSSFSheet sheet) {
		if ("供应商交易服务费信息".equals(fileName)) {
			sheet.setColumnWidth(4, 100 * 20);
			sheet.setColumnWidth(6, 100 * 20);
			sheet.setColumnWidth(8, 100 * 20);
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 4, 5));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 7));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 8, 9));
		}
	}

	public static void createExcleOfAgentList(String fileName, List<Map<Object, Object>> list, 
			String[] cellTitle, String firstTitle, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int rowNum = 0;
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet("sheet1");
		CellRangeAddress region = new CellRangeAddress(0, 0, 0, cellTitle.length - 1);
		sheet.addMergedRegion(region);// 合并首行
		// sheet.setColumnWidth(2, 10);
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeight((short) 390);

		HSSFRow titleRow = sheet.createRow((short) rowNum++);
		// 字体设置
		HSSFFont font = workBook.createFont();
		font.setFontName("宋体");
		font.setFontHeight((short) HSSFFont.BOLDWEIGHT_NORMAL);
		font.setColor(HSSFFont.COLOR_NORMAL);

		// 样式
		HSSFCellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setFont(font);

		short countColumn = 0;
		if (cellTitle != null) {
			countColumn = (short) cellTitle.length;
		}
		HSSFCell titleCell = titleRow.createCell(0);
		titleRow.setHeight((short) 500);// 设置首行高度
		// sheet.addMergedRegion(region)
		titleCell.setCellValue(firstTitle);
		titleCell.setCellStyle(cellStyle);
		HSSFRow rowTwo = sheet.createRow(rowNum++);

		HSSFFont rowTwoCellFont = workBook.createFont();
		rowTwoCellFont.setFontName("宋体");
		rowTwoCellFont.setColor(HSSFFont.COLOR_NORMAL);

		HSSFCellStyle rowTwoCellStyle = workBook.createCellStyle();
		rowTwoCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		rowTwoCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		rowTwoCellStyle.setFont(rowTwoCellFont);

		for (int i = 0; i < cellTitle.length; i++) {
			HSSFCell rowTwoCell = rowTwo.createCell(i);
			rowTwoCell.setCellStyle(rowTwoCellStyle);
			rowTwoCell.setCellValue(cellTitle[i]);
		}

		for (int j = 0; j < list.size(); j++) {
			Map<Object, Object> o = (Map<Object, Object>) list.get(j);
			HSSFRow dataRow = sheet.createRow(rowNum++);
			int n = 0;
			HSSFCell dataCel = dataRow.createCell(n);
			dataCel.setCellStyle(rowTwoCellStyle);
			dataCel.setCellValue(j + 1 + "");
			n++;
			dataCel = dataRow.createCell(n);
			dataCel.setCellStyle(rowTwoCellStyle);
			dataCel.setCellValue(o.get("agentName") == null ? 
					"" : o.get("agentName").toString());
			n++;
			dataCel = dataRow.createCell(n);
			dataCel.setCellStyle(rowTwoCellStyle);
			dataCel.setCellValue(o.get("agentBrand") == null ? 
					"" : o.get("agentBrand").toString());
			n++;
			dataCel = dataRow.createCell(n);
			dataCel.setCellStyle(rowTwoCellStyle);
			dataCel.setCellValue(o.get("salesRoom") == null ? 
					"" : o.get("salesRoom").toString());
			n++;
			dataCel = dataRow.createCell(n);
			dataCel.setCellStyle(rowTwoCellStyle);
			dataCel.setCellValue(o.get("agentAddressStreet") == null ? 
					"" : o.get("agentAddressStreet").toString());
			n++;
			dataCel = dataRow.createCell(n);
			dataCel.setCellStyle(rowTwoCellStyle);
			dataCel.setCellValue(o.get("agentTel") == null ? 
					"" : o.get("agentTelAreaCode").toString() + "-" + o.get("agentTel").toString());
			n++;
			dataCel = dataRow.createCell(n);
			dataCel.setCellStyle(rowTwoCellStyle);
			dataCel.setCellValue(o.get("contacts") == null ? 
					"" : o.get("contacts").toString());
			n++;
			dataCel = dataRow.createCell(n);
			dataCel.setCellStyle(rowTwoCellStyle);
			dataCel.setCellValue(o.get("sumTotalMoney") == null ? 
					"" : o.get("sumTotalMoney").toString());
			n++;
			dataCel = dataRow.createCell(n);
			dataCel.setCellStyle(rowTwoCellStyle);
			dataCel.setCellValue(o.get("sumPayedMoney") == null ? 
					"" : o.get("sumPayedMoney").toString());
			n++;

			dataCel = dataRow.createCell(n);
			dataCel.setCellStyle(rowTwoCellStyle);
			dataCel.setCellValue(o.get("sumAccountedMoney") == null ? 
					"" : o.get("sumAccountedMoney").toString());
			n++;

			dataCel = dataRow.createCell(n);
			dataCel.setCellStyle(rowTwoCellStyle);
			dataCel.setCellValue(o.get("sumOrderNum") == null ? 
					"" : o.get("sumOrderNum").toString());
			n++;
			dataCel = dataRow.createCell(n);
			dataCel.setCellStyle(rowTwoCellStyle);
			dataCel.setCellValue(o.get("agentSalerId") == null ? 
					"" : UserUtils.getSalersFromIdStr(o.get("agentSalerId").toString())
					.get("salerNameStrWithStop").toString());
			n++;
		}
		// 导出excel文档
		OutputStream op = null;
		fileName = fileName + ".xls";
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		setFileDownloadHeader(request, response, fileName);

		op = response.getOutputStream();
		workBook.write(op);
		op.close();
	}

	// 根据当前用户的浏览器不同，对文件的名字进行不同的编码设置，从而解决不同浏览器下文件名中文乱码问题
	public static void setFileDownloadHeader(HttpServletRequest request,
			HttpServletResponse response, String fileName) {
		String encodedfileName;
		try {
			// 中文文件名支持
			if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {// IE浏览器
				encodedfileName = URLEncoder.encode(fileName, "UTF-8");
			} else if (request.getHeader("User-Agent").toLowerCase()
					.indexOf("firefox") > 0
					|| request.getHeader("User-Agent").toLowerCase()
							.indexOf("opera") > 0) {// google,火狐浏览器
				encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
			} else {
				encodedfileName = URLEncoder.encode(fileName, "UTF-8");// 其他浏览器
			}

			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ encodedfileName + "\"");// 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
		} catch (UnsupportedEncodingException e) {
		}
	}

	/**
	 * 生成、下载Excel文件(统计用)
	 * 
	 * @param fileName
	 *            ：文件名称
	 * @param list
	 *            ：要生成数据
	 * @param cellTitle
	 *            ：Excle各列名称
	 * @param firstTitle
	 *            ：Excel首行标题
	 */
	public static void createExcleNew(String fileName,
			List<Map<Object, Object>> list, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet("sheet1");
		workBook.setSheetName(0, fileName);
		sheet.setDefaultColumnWidth(15);
		sheet.setDefaultRowHeight((short) 390);
		// 字体设置
		HSSFFont font = workBook.createFont();
		font.setFontName("宋体");
		// font.setFontHeight((short) HSSFFont.BOLDWEIGHT_NORMAL);
		font.setFontHeightInPoints((short) 11);
		font.setColor(HSSFFont.COLOR_NORMAL);
		// 样式
		HSSFCellStyle cellStyle = workBook.createCellStyle();
		// cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// cellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中
		cellStyle.setFont(font);
		List<Map<String, Object>> list2 = excelHeadList();
		int x = 0;
		for (int i = 0; i < list2.size(); i++) {
			Map<String, Object> map = list2.get(i);

			int flag = 0;
			if (list2.get(i).size() == 15) {
				HSSFRow row = sheet.createRow(x);
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					HSSFCell cell = row.createCell(flag);
					flag++;
					cell.setCellValue(entry.getValue().toString());
					cell.setCellStyle(cellStyle);
				}
			} else {
				HSSFRow row = sheet.createRow(x);
				HSSFCell cell = row.createCell(1);
				cell.setCellValue(list2.get(1).get("accountDate").toString());
				cell.setCellStyle(cellStyle);
				HSSFCell cell2 = row.createCell(11);
				cell2.setCellValue(list2.get(1).get("sumAccountMoney")
						.toString());
				cell2.setCellStyle(cellStyle);
				HSSFCell cell3 = row.createCell(12);
				cell3.setCellValue(list2.get(1).get("accountMoney").toString());
				cell3.setCellStyle(cellStyle);
			}
			x++;
			CellRangeAddress cra = new CellRangeAddress(0, 1, 0, 0);
			sheet.addMergedRegion(cra);
			CellRangeAddress cra2 = new CellRangeAddress(0, 1, 2, 2);
			sheet.addMergedRegion(cra2);
			CellRangeAddress cra3 = new CellRangeAddress(0, 1, 3, 3);
			sheet.addMergedRegion(cra3);
			CellRangeAddress cra4 = new CellRangeAddress(0, 1, 4, 4);
			sheet.addMergedRegion(cra4);
			CellRangeAddress cra5 = new CellRangeAddress(0, 1, 5, 5);
			sheet.addMergedRegion(cra5);
			CellRangeAddress cra6 = new CellRangeAddress(0, 1, 6, 6);
			sheet.addMergedRegion(cra6);
			CellRangeAddress cra7 = new CellRangeAddress(0, 1, 7, 7);
			sheet.addMergedRegion(cra7);
			CellRangeAddress cra8 = new CellRangeAddress(0, 1, 8, 8);
			sheet.addMergedRegion(cra8);
			CellRangeAddress cra9 = new CellRangeAddress(0, 1, 9, 9);
			sheet.addMergedRegion(cra9);
			CellRangeAddress cra10 = new CellRangeAddress(0, 1, 10, 10);
			sheet.addMergedRegion(cra10);
			CellRangeAddress cra13 = new CellRangeAddress(0, 1, 13, 13);
			sheet.addMergedRegion(cra13);
			CellRangeAddress cra14 = new CellRangeAddress(0, 1, 14, 14);
			sheet.addMergedRegion(cra14);
		}
		// 创建表体
		int count = 2;
		for (int i = 0; i < list.size(); i++) {
			HSSFRow row = sheet.createRow(count + i);
			HSSFCell cell = row.createCell(0);
			cell.setCellValue(i + 1);
			cell.setCellStyle(cellStyle);
			HSSFCell cell1 = row.createCell(1);
			if (list.get(i).get("odate").toString().length() > 0)
				cell1.setCellValue(StrToDate(list.get(i).get("odate")
						.toString()));
			else
				cell1.setCellValue("");
			cell1.setCellStyle(cellStyle);
			HSSFCell cell2 = row.createCell(2);
			cell2.setCellValue(list.get(i).get("orderNum").toString());
			cell2.setCellStyle(cellStyle);

			HSSFCell cell3 = row.createCell(3);
			cell3.setCellValue(list.get(i).get("groupCode").toString());
			cell3.setCellStyle(cellStyle);

			HSSFCell cell4 = row.createCell(4);
			cell4.setCellValue(list.get(i).get("productName").toString());
			cell4.setCellStyle(cellStyle);

			HSSFCell cell5 = row.createCell(5);
			cell5.setCellValue(list.get(i).get("sName").toString());
			cell5.setCellStyle(cellStyle);

			HSSFCell cell6 = row.createCell(6);
			cell6.setCellValue(list.get(i).get("suuName").toString());
			cell6.setCellStyle(cellStyle);

			HSSFCell cell7 = row.createCell(7);
			cell7.setCellValue(list.get(i).get("jd_Name").toString());
			cell7.setCellStyle(cellStyle);

			HSSFCell cell8 = row.createCell(8);
			cell8.setCellValue(list.get(i).get("agentName").toString());
			cell8.setCellStyle(cellStyle);

			HSSFCell cell9 = row.createCell(9);
			cell9.setCellValue(list.get(i).get("payerName").toString());
			cell9.setCellStyle(cellStyle);

			HSSFCell cell10 = row.createCell(10);
			cell10.setCellValue(list.get(i).get("toBankNname").toString());
			cell10.setCellStyle(cellStyle);

			HSSFCell cell11 = row.createCell(11);
			if ("7a45838277a811e5bc1e000c29cf2586".equals(UserUtils.getUser()
					.getCompany().getUuid())) {
				if (!"".equals(list.get(i).get("total_money").toString()))
					cell11.setCellValue(list.get(i).get("total_money")
							.toString());
				else
					cell11.setCellValue("");
			}

			if (!"7a45838277a811e5bc1e000c29cf2586".equals(UserUtils.getUser()
					.getCompany().getUuid())) {
				if (!"".equals(list.get(i).get("total_money").toString()))
					cell11.setCellValue(list.get(i).get("total_money")
							.toString());
				else
					cell11.setCellValue("");
			}

			cell11.setCellStyle(cellStyle);

			HSSFCell cell12 = row.createCell(12); // 已付金额
			cell12.setCellValue(list.get(i).get("payed_money").toString());
			cell12.setCellStyle(cellStyle);

			HSSFCell cell13 = row.createCell(13);
			if ("1".equals(list.get(i).get("payType").toString()))
				cell13.setCellValue("支票");
			if ("2".equals(list.get(i).get("payType").toString()))
				cell13.setCellValue("POS机付款");
			if ("3".equals(list.get(i).get("payType").toString()))
				cell13.setCellValue("现金支付");
			if ("4".equals(list.get(i).get("payType").toString()))
				cell13.setCellValue("汇款");
			if ("5".equals(list.get(i).get("payType").toString()))
				cell13.setCellValue("快速支付");
			if ("6".equals(list.get(i).get("payType").toString()))
				cell13.setCellValue("银行转账");
			if ("7".equals(list.get(i).get("payType").toString()))
				cell13.setCellValue("汇票");
			if ("8".equals(list.get(i).get("payType").toString()))
				cell13.setCellValue("POS机刷卡");
			cell13.setCellStyle(cellStyle);

			HSSFCell cell14 = row.createCell(14);
			String printFlag = "";
			if ("1".equals(list.get(i).get("printFlag").toString()))
				printFlag = "已打印";
			else
				printFlag = "未打印";
			cell14.setCellValue(printFlag);
			cell14.setCellStyle(cellStyle);

			HSSFRow rownext = sheet.createRow(i + 1 + count);
			HSSFCell cell_1 = rownext.createCell(1);
			if (list.get(i).get("accountDate").toString().length() > 0) {
				if ("1".equals(list.get(i).get("isAsAccount").toString())) {
					cell_1.setCellValue(StrToDate(list.get(i)
							.get("accountDate").toString()));
				} else {
					cell_1.setCellValue("");
				}

			} else {
				cell_1.setCellValue("");
			}
			cell_1.setCellStyle(cellStyle);
			HSSFCell cell_11 = rownext.createCell(11); // / 累计到账金额
			if ("7a45838277a811e5bc1e000c29cf2586".equals(UserUtils.getUser()
					.getCompany().getUuid())) {
				if (!"".equals(list.get(i).get("no_pay_money")))
					cell_11.setCellValue(list.get(i).get("no_pay_money")
							.toString());
				if (!"".equals(list.get(i).get("accounted_money")))
					cell_11.setCellValue(list.get(i).get("accounted_money")
							.toString());
				else
					cell_11.setCellValue("");
			}
			if (!"7a45838277a811e5bc1e000c29cf2586".equals(UserUtils.getUser()
					.getCompany().getUuid())) {
				if (!"".equals(list.get(i).get("accounted_money").toString())
						&& !"".equals(list.get(i).get("cancleFlag").toString())
						&& "1".equals(list.get(i).get("cancleFlag").toString()
								.substring(5)))
					cell_11.setCellValue(list.get(i).get("accounted_money")
							.toString());
				else
					cell_11.setCellValue("");
			}
			cell_11.setCellStyle(cellStyle);

			HSSFCell cell_12 = rownext.createCell(12); // / 到账金额
			if (!"".equals(list.get(i).get("isAsAccount").toString())
					&& "1".equals(list.get(i).get("isAsAccount").toString()
							.substring(5)))
				cell_12.setCellValue(list.get(i).get("payed_money").toString());
			else
				cell_12.setCellValue("");
			cell_12.setCellStyle(cellStyle);
			CellRangeAddress cra = new CellRangeAddress(count + i, count + i
					+ 1, 0, 0);
			sheet.addMergedRegion(cra);
			CellRangeAddress cra2 = new CellRangeAddress(count + i, count + i
					+ 1, 2, 2);
			sheet.addMergedRegion(cra2);
			CellRangeAddress cra3 = new CellRangeAddress(count + i, count + i
					+ 1, 3, 3);
			sheet.addMergedRegion(cra3);
			CellRangeAddress cra4 = new CellRangeAddress(count + i, count + i
					+ 1, 4, 4);
			sheet.addMergedRegion(cra4);
			CellRangeAddress cra5 = new CellRangeAddress(count + i, count + i
					+ 1, 5, 5);
			sheet.addMergedRegion(cra5);
			CellRangeAddress cra6 = new CellRangeAddress(count + i, count + i
					+ 1, 6, 6);
			sheet.addMergedRegion(cra6);
			CellRangeAddress cra7 = new CellRangeAddress(count + i, count + i
					+ 1, 7, 7);
			sheet.addMergedRegion(cra7);
			CellRangeAddress cra8 = new CellRangeAddress(count + i, count + i
					+ 1, 8, 8);
			sheet.addMergedRegion(cra8);
			CellRangeAddress cra9 = new CellRangeAddress(count + i, count + i
					+ 1, 9, 9);
			sheet.addMergedRegion(cra9);
			CellRangeAddress cra10 = new CellRangeAddress(count + i, count + i
					+ 1, 10, 10);
			sheet.addMergedRegion(cra10);
			CellRangeAddress cra13 = new CellRangeAddress(count + i, count + i
					+ 1, 13, 13);
			sheet.addMergedRegion(cra13);
			CellRangeAddress cra14 = new CellRangeAddress(count + i, count + i
					+ 1, 14, 14);
			sheet.addMergedRegion(cra14);
			count = count + 1;
		}
		OutputStream op = null;
		fileName = fileName + ".xls";
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		setFileDownloadHeader(request, response, fileName);

		op = response.getOutputStream();
		workBook.write(op);
		op.close();
	}

	private static List<Map<String, Object>> excelHeadList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Map<String, Object> map2 = new LinkedHashMap<String, Object>();
		map.put("num", "序号");
		map.put("payTime", "付款日期");
		map.put("orderCode", "订单号");
		map.put("groupCode", "团号");
		map.put("productName", "产品名称");
		map.put("sale", "销售");
		map.put("createBy", "下单人");
		map.put("jd", "计调");
		map.put("channelName", "渠道名称");
		map.put("laikdanwei", "来款单位");
		map.put("acceptBank", "收款银行");
		map.put("orderMoney", "订单金额");
		map.put("payedMoney", "已付金额");
		map.put("payType", "支付方式");
		map.put("printStatus", "打印确认");
		map2.put("accountDate", "银行到账日期");
		map2.put("sumAccountMoney", "累计到账金额");
		map2.put("accountMoney", "到账金额");
		list.add(map);
		list.add(map2);
		return list;
	}

	/**
	 * 生成机票结算通知单
	 * 
	 * @param settlement
	 *            机票结算单对象
	 * @return
	 * @author shijun.liu
	 */
	public static Workbook createSettlementExcel(SettlementJsonBean settlement) {
		Workbook workbook = new HSSFWorkbook();
		Font bigTitleFont = workbook.createFont();
		bigTitleFont.setFontHeightInPoints((short) 14);
		bigTitleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle bigTitleStyle = workbook.createCellStyle();
		bigTitleStyle.setBorderBottom(CellStyle.BORDER_THIN);
		bigTitleStyle.setBorderLeft(CellStyle.BORDER_THIN);
		bigTitleStyle.setBorderRight(CellStyle.BORDER_THIN);
		bigTitleStyle.setBorderTop(CellStyle.BORDER_THIN);
		bigTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		bigTitleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		bigTitleStyle.setFont(bigTitleFont);

		CellStyle titleStyle = workbook.createCellStyle();
		Font titleFont = workbook.createFont();
		titleFont.setFontHeightInPoints((short) 11);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		titleStyle.setBorderBottom(CellStyle.BORDER_THIN);
		titleStyle.setBorderLeft(CellStyle.BORDER_THIN);
		titleStyle.setBorderRight(CellStyle.BORDER_THIN);
		titleStyle.setBorderTop(CellStyle.BORDER_THIN);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		titleStyle.setFont(titleFont);

		CellStyle valueStyle = workbook.createCellStyle();
		Font valueFont = workbook.createFont();
		valueFont.setFontHeightInPoints((short) 11);
		valueStyle.setBorderBottom(CellStyle.BORDER_THIN);
		valueStyle.setBorderLeft(CellStyle.BORDER_THIN);
		valueStyle.setBorderRight(CellStyle.BORDER_THIN);
		valueStyle.setBorderTop(CellStyle.BORDER_THIN);
		valueStyle.setAlignment(CellStyle.ALIGN_CENTER);
		valueStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		valueStyle.setFont(valueFont);

		Sheet sheet = workbook.createSheet();
		sheet.setDefaultColumnWidth(11);
		// 表头
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
		// 空白行
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));
		// 团号单元格合并
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));
		// 人数单元格合并
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 3));
		// 出票日期单元格合并
		sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 3));
		// 行程单元格合并
		sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 3));
		// 航空公司单元格合并
		sheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 3));
		// 机票操作员单元格合并
		sheet.addMergedRegion(new CellRangeAddress(7, 7, 1, 3));
		// 备注单元格合并
		sheet.addMergedRegion(new CellRangeAddress(2, 4, 4, 4));
		// 备注值单元格合并
		sheet.addMergedRegion(new CellRangeAddress(2, 4, 5, 8));
		// 行程时间段单元格合并
		sheet.addMergedRegion(new CellRangeAddress(5, 5, 5, 8));
		// 供应商名称单元格合并
		sheet.addMergedRegion(new CellRangeAddress(6, 6, 5, 8));
		// 销售人员单元格合并
		sheet.addMergedRegion(new CellRangeAddress(7, 7, 5, 8));
		// 收入项
		sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 8));
		float bigTitleHeight = 20.0f; // 大标题所在行的高度
		// 创建表格标题
		for (int i = 1; i < 9; i++) {
			createCellWithVal(sheet, 0, bigTitleHeight, 0, "机票结算通知单",
					bigTitleStyle);
		}
		// 创建空白行
		for (int i = 0; i < 9; i++) {
			createCellWithVal(sheet, 1, bigTitleHeight, i, null, bigTitleStyle);
		}
		// 填充结算单的基本数据信息
		fillSettlementBaseInfo(sheet, titleStyle, valueStyle, settlement);
		// 收入项
		int incomeIndex = fillInCome(sheet, titleStyle, valueStyle, settlement);
		// 成本项单元格合并
		sheet.addMergedRegion(new CellRangeAddress(incomeIndex + 1,
				incomeIndex + 1, 0, 8));
		fillCost(sheet, titleStyle, valueStyle, settlement, incomeIndex + 1);
		return workbook;
	}

	/**
	 * 将结算单基础数据添加到Excel表格
	 * 
	 * @param sheet
	 *            表格对象
	 * @param titleStyle
	 *            标题单元格样式
	 * @param valueStyle
	 *            数据值单元格样式
	 * @param settlement
	 *            结算单数据对象
	 * @author shijun.liu
	 */
	private static void fillSettlementBaseInfo(Sheet sheet,
			CellStyle titleStyle, CellStyle valueStyle,
			SettlementJsonBean settlement) {
		float height = 16.0f; // 标题所在行的高度
		// 团号
		createCellWithVal(sheet, 2, height, 0, "团号", titleStyle);
		// 团号值
		createCellWithVal(sheet, 2, height, 1, settlement.getGroupNo(),
				valueStyle);
		createCellWithVal(sheet, 2, height, 2, null, valueStyle);
		createCellWithVal(sheet, 2, height, 3, null, valueStyle);

		// 备注以及其值
		for (int i = 2; i < 5; i++) {
			createCellWithVal(sheet, i, height, 4, "备注", titleStyle);
			for (int j = 5; j < 9; j++) {
				createCellWithVal(sheet, i, height, j, null, valueStyle);
			}
		}
		// 人数
		createCellWithVal(sheet, 3, height, 0, "人数", titleStyle);
		// 人数值
		createCellWithVal(sheet, 3, height, 1, settlement.getPeopleCount(),
				valueStyle);
		createCellWithVal(sheet, 3, height, 2, null, valueStyle);
		createCellWithVal(sheet, 3, height, 3, null, valueStyle);

		// 出票日期
		createCellWithVal(sheet, 4, height, 0, "出票日期", titleStyle);
		// 出票日期值
		createCellWithVal(sheet, 4, height, 1, settlement.getInvoiceDate(),
				valueStyle);
		createCellWithVal(sheet, 4, height, 2, null, valueStyle);
		createCellWithVal(sheet, 4, height, 3, null, valueStyle);

		// 行程
		createCellWithVal(sheet, 5, height, 0, "行程", titleStyle);
		// 行程值
		createCellWithVal(sheet, 5, height, 1, settlement.getItinerary(),
				valueStyle);
		createCellWithVal(sheet, 5, height, 2, null, valueStyle);
		createCellWithVal(sheet, 5, height, 3, null, valueStyle);

		// 航空公司
		createCellWithVal(sheet, 6, height, 0, "航空公司", titleStyle);
		// 航空公司值
		createCellWithVal(sheet, 6, height, 1, settlement.getAirlineCompany(),
				valueStyle);
		createCellWithVal(sheet, 6, height, 2, null, valueStyle);
		createCellWithVal(sheet, 6, height, 3, null, valueStyle);

		// 机票操作员
		createCellWithVal(sheet, 7, height, 0, "机票操作员", titleStyle);
		// 机票操作员值
		createCellWithVal(sheet, 7, height, 1, settlement.getTicketName(),
				valueStyle);
		createCellWithVal(sheet, 7, height, 2, null, valueStyle);
		createCellWithVal(sheet, 7, height, 3, null, valueStyle);

		// 行程时间段
		createCellWithVal(sheet, 5, height, 4, "行程时间段", titleStyle);
		// 行程时间段值
		createCellWithVal(sheet, 5, height, 5, settlement.getTravelPeriod(),
				valueStyle);
		createCellWithVal(sheet, 5, height, 6, null, valueStyle);
		createCellWithVal(sheet, 5, height, 7, null, valueStyle);
		createCellWithVal(sheet, 5, height, 8, null, valueStyle);

		// 供应商名称
		createCellWithVal(sheet, 6, height, 4, "供应商名称", titleStyle);
		// 供应商名称值
		createCellWithVal(sheet, 6, height, 5, settlement.getSupplierName(),
				valueStyle);
		createCellWithVal(sheet, 6, height, 6, null, valueStyle);
		createCellWithVal(sheet, 6, height, 7, null, valueStyle);
		createCellWithVal(sheet, 6, height, 8, null, valueStyle);

		// 销售人员
		createCellWithVal(sheet, 7, height, 4, "销售人员", titleStyle);
		// 销售人员值
		createCellWithVal(sheet, 7, height, 5, settlement.getOrderer(),
				valueStyle);
		createCellWithVal(sheet, 7, height, 6, null, valueStyle);
		createCellWithVal(sheet, 7, height, 7, null, valueStyle);
		createCellWithVal(sheet, 7, height, 8, null, valueStyle);
	}

	/**
	 * 将收入项的数据添加到收入项的表格中，如果数据不到4条，则Excel用空显示，如果超过4条，则显示相应的条数
	 * 
	 * @param sheet
	 *            表格对象
	 * @param titleStyle
	 *            表题单元格样式
	 * @param valueStyle
	 *            数据值单元格样式
	 * @param settlement
	 *            结算单数据对象
	 * @return 收入合计所在的行号
	 * @author shijun.liu
	 */
	private static int fillInCome(Sheet sheet, CellStyle titleStyle,
			CellStyle valueStyle, SettlementJsonBean settlement) {
		int incomeSumIndex = 14;// 收入合计所在的行号，默认14行
		float titleHeight = 20.0f;
		float headHeight = 24.0f;
		float valeHeight = 16.0f;
		String[] incomeTitles = { "收款日期", "客户名称", "人数", "定金", "尾款(全款)", "总计",
				"币种", "汇率", "折合(RMB)" };
		for (int i = 0; i < incomeTitles.length; i++) {
			createCellWithVal(sheet, 8, titleHeight, i, "收    入     项",
					titleStyle);
		}

		for (int i = 0; i < incomeTitles.length; i++) {
			createCellWithVal(sheet, 9, headHeight, i, incomeTitles[i],
					titleStyle);
		}
		if (null != settlement.getIncomes()) {
			List<InComeJsonBean> incomes = settlement.getIncomes();
			for (int i = 0; i < incomes.size(); i++) {
				String currencyMark = incomes.get(i).getCurrencyMark();
				String deposit = incomes.get(i).getDeposit();
				String balancePayment = incomes.get(i).getBalancePayment();
				if (StringUtils.isNotBlank(deposit)) {
					// 定金显示两位小数，并且按千分位显示
					deposit = currencyMark
							+ MoneyNumberFormat.getThousandsByRegex(deposit, 2);
				}
				if (StringUtils.isNotBlank(balancePayment)) {
					// 尾款显示两位小数，并且按千分位显示
					balancePayment = currencyMark
							+ MoneyNumberFormat.getThousandsByRegex(
									balancePayment, 2);
				}
				createCellWithVal(sheet, i + 10, valeHeight, 0, incomes.get(i)
						.getReceiveDate(), valueStyle);
				createCellWithVal(sheet, i + 10, valeHeight, 1, incomes.get(i)
						.getCustomer(), valueStyle);
				createCellWithVal(sheet, i + 10, valeHeight, 2, incomes.get(i)
						.getPeopleCount(), valueStyle);
				createCellWithVal(sheet, i + 10, valeHeight, 6, currencyMark,
						valueStyle);
				createCellWithVal(sheet, i + 10, valeHeight, 7, incomes.get(i)
						.getExchangeRate(), valueStyle);
				createCellWithVal(sheet, i + 10, valeHeight, 3, deposit,
						valueStyle);
				createCellWithVal(sheet, i + 10, valeHeight, 4, balancePayment,
						valueStyle);
				createCellWithVal(sheet, i + 10, valeHeight, 5, incomes.get(i)
						.getTotalAmount(), valueStyle);
				createCellWithVal(sheet, i + 10, valeHeight, 8, incomes.get(i)
						.getRmb(), valueStyle);
			}
			if (4 - incomes.size() > 0) {// 如果数据不到4条，则补空显示
				for (int i = 0; i < 4 - incomes.size(); i++) {
					for (int j = 0; j < incomeTitles.length; j++) {
						createCellWithVal(sheet, i + 10 + incomes.size(),
								valeHeight, j, null, valueStyle);
					}
				}
			} else {
				incomeSumIndex = incomeSumIndex + incomes.size() - 4;
			}
		} else {// 默认显示四行
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < incomeTitles.length; j++) {
					createCellWithVal(sheet, i + 10, valeHeight, j, null,
							valueStyle);
				}
			}
		}
		for (int i = 0; i < incomeTitles.length; i++) {
			if (0 == i) {
				createCellWithVal(sheet, incomeSumIndex, valeHeight, i, "收入合计",
						titleStyle);
			} else if (5 == i) {
				createCellWithVal(sheet, incomeSumIndex, valeHeight, i,
						settlement.getInComeSum(), valueStyle);
			} else if (i == incomeTitles.length - 1) {
				createCellWithVal(sheet, incomeSumIndex, valeHeight, i,
						settlement.getInComeSumRMB(), valueStyle);
			} else {
				createCellWithVal(sheet, incomeSumIndex, valeHeight, i, null,
						valueStyle);
			}
		}
		return incomeSumIndex;
	}

	/**
	 * 将成本项的数据添加到成本项的表格中，如果数据不到4条，补空显示，如果超过4条，则显示相应条数的数据
	 * 
	 * @param sheet
	 *            表格对象
	 * @param titleStyle
	 *            表题单元格样式
	 * @param valueStyle
	 *            数据单元格样式
	 * @param settlement
	 *            结算单数据对象
	 * @param costIndex
	 *            成本项，所在的行号
	 * @author shijun.liu
	 */
	private static void fillCost(Sheet sheet, CellStyle titleStyle,
			CellStyle valueStyle, SettlementJsonBean settlement, int costIndex) {
		int profiltIndex = 0;// 毛利所在的行号
		float titleHeight = 20.0f;
		float headHeight = 24.0f;
		float valeHeight = 16.0f;
		String[] costTitles = { "项目", "大编号", "航空公司", "人数", "成本单价", "总计", "币种",
				"汇率", "折合(RMB)" };
		for (int i = 0; i < costTitles.length; i++) {
			createCellWithVal(sheet, costIndex, titleHeight, i, "成    本     项",
					titleStyle);
		}
		for (int i = 0; i < costTitles.length; i++) {
			createCellWithVal(sheet, costIndex + 1, headHeight, i,
					costTitles[i], titleStyle);
		}
		if (null != settlement.getCosts()) {
			List<CostJsonBean> costs = settlement.getCosts();
			for (int i = 0; i < costs.size(); i++) {
				String currencyMark = costs.get(i).getCurrencyMark();
				String deposite = costs.get(i).getPrice();
				if (StringUtils.isNotBlank(deposite)) {
					deposite = currencyMark
							+ MoneyNumberFormat
									.getThousandsByRegex(deposite, 2);
				}
				String bigCode = costs.get(i).getPNR();
				if ("1".equals(costs.get(i).getInvoiceOriginalTypeCode())) {
					bigCode = costs.get(i).getTourOperatorName();
				}
				createCellWithVal(sheet, costIndex + 2 + i, valeHeight, 0,
						costs.get(i).getFundsName(), valueStyle);
				createCellWithVal(sheet, costIndex + 2 + i, valeHeight, 1,
						bigCode, valueStyle);
				createCellWithVal(sheet, costIndex + 2 + i, valeHeight, 2,
						costs.get(i).getAirlineCompany(), valueStyle);
				createCellWithVal(sheet, costIndex + 2 + i, valeHeight, 3,
						costs.get(i).getPeopleCount(), valueStyle);
				createCellWithVal(sheet, costIndex + 2 + i, valeHeight, 6,
						currencyMark, valueStyle);
				createCellWithVal(sheet, costIndex + 2 + i, valeHeight, 7,
						costs.get(i).getExchangeRate(), valueStyle);
				createCellWithVal(sheet, costIndex + 2 + i, valeHeight, 4,
						deposite, valueStyle);
				createCellWithVal(sheet, costIndex + 2 + i, valeHeight, 5,
						costs.get(i).getTotalAmount(), valueStyle);
				createCellWithVal(sheet, costIndex + 2 + i, valeHeight, 8,
						costs.get(i).getRmb(), valueStyle);
			}
			if (4 - costs.size() > 0) {// 如果数据不到4条，则补空显示
				for (int i = 0; i < 4 - costs.size(); i++) {
					for (int j = 0; j < costTitles.length; j++) {
						createCellWithVal(sheet, costIndex + 2 + costs.size()
								+ i, valeHeight, j, null, valueStyle);
					}
				}
				profiltIndex = costIndex + 2 + 4;
			} else {
				profiltIndex = costIndex + 2 + costs.size();
			}
		} else {// 默认显示四行
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < costTitles.length; j++) {
					createCellWithVal(sheet, costIndex + 2 + i, valeHeight, j,
							null, valueStyle);
				}
			}
			profiltIndex = costIndex + 2 + 4;
		}
		// 追加成本
		for (int i = 0; i < costTitles.length; i++) {
			if (0 == i) {
				createCellWithVal(sheet, profiltIndex, valeHeight, i, "追加成本",
						titleStyle);
			} else if (5 == i) {
				createCellWithVal(sheet, profiltIndex, valeHeight, i,
						settlement.getAdditionalCostSum(), valueStyle);
			} else if (i == costTitles.length - 1) {
				createCellWithVal(sheet, profiltIndex, valeHeight, i,
						settlement.getAdditionalCostSumRMB(), valueStyle);
			} else {
				createCellWithVal(sheet, profiltIndex, valeHeight, i, null,
						valueStyle);
			}
		}
		profiltIndex = profiltIndex + 1;// 空白行行号
		for (int i = 0; i < costTitles.length; i++) {
			createCellWithVal(sheet, profiltIndex, valeHeight, i, null,
					valueStyle);
		}
		profiltIndex = profiltIndex + 1;// 成本合计行号
		// 成本合计
		for (int i = 0; i < costTitles.length; i++) {
			if (0 == i) {
				createCellWithVal(sheet, profiltIndex, valeHeight, i, "成本合计",
						titleStyle);
			} else if (5 == i) {
				createCellWithVal(sheet, profiltIndex, valeHeight, i,
						settlement.getCostSum(), valueStyle);
			} else if (i == costTitles.length - 1) {
				createCellWithVal(sheet, profiltIndex, valeHeight, i,
						settlement.getCostSumRMB(), valueStyle);
			} else {
				createCellWithVal(sheet, profiltIndex, valeHeight, i, null,
						valueStyle);
			}
		}
		// 退款
		if (StringUtils.isNotBlank(settlement.getRefundSum())
				|| StringUtils.isNotBlank(settlement.getRefundSumRMB())) {
			profiltIndex = profiltIndex + 1;// 退款行号
			for (int i = 0; i < costTitles.length; i++) {
				if (0 == i) {
					createCellWithVal(sheet, profiltIndex, valeHeight, i, "退款",
							titleStyle);
				} else if (5 == i) {
					createCellWithVal(sheet, profiltIndex, valeHeight, i,
							settlement.getRefundSum(), valueStyle);
				} else if (i == costTitles.length - 1) {
					createCellWithVal(sheet, profiltIndex, valeHeight, i,
							settlement.getRefundSumRMB(), valueStyle);
				} else {
					createCellWithVal(sheet, profiltIndex, valeHeight, i, null,
							valueStyle);
				}
			}
		}
		profiltIndex = profiltIndex + 1;// 毛利所在行行号
		for (int i = 0; i < costTitles.length; i++) {
			if (0 == i) {
				createCellWithVal(sheet, profiltIndex, valeHeight, i, "毛利",
						titleStyle);
			} else if (1 == i) {
				createCellWithVal(sheet, profiltIndex, valeHeight, i,
						settlement.getGrossProfit(), valueStyle);
			} else if (3 == i) {
				createCellWithVal(sheet, profiltIndex, valeHeight, i, "毛利率%",
						titleStyle);
			} else if (4 == i) {
				createCellWithVal(sheet, profiltIndex, valeHeight, i,
						settlement.getGrossProfitRate(), valueStyle);
			} else {
				createCellWithVal(sheet, profiltIndex, valeHeight, i, null,
						valueStyle);
			}
		}
		// 制表，日期显示
		createCellWithVal(sheet, profiltIndex + 2, valeHeight, 0, "制表：", null);
		createCellWithVal(sheet, profiltIndex + 2, valeHeight, 1,
				settlement.getLister(), null);
		createCellWithVal(sheet, profiltIndex + 2, valeHeight, 5, "日期：", null);
		createCellWithVal(sheet, profiltIndex + 2, valeHeight, 6,
				settlement.getListerDate(), null);
	}

	/**
	 * 创建赋值的单元格
	 * 
	 * @param sheet
	 *            Excel的Sheet对象
	 * @param rowIndex
	 *            行号
	 * @param rowHeight
	 *            行高
	 * @param cellIndex
	 *            列号
	 * @param value
	 *            值
	 * @param cellStyle
	 *            单元格样式
	 * @return
	 * @author shijun.liu
	 */
	private static Cell createCellWithVal(Sheet sheet, int rowIndex,
			float rowHeight, int cellIndex, String value, CellStyle cellStyle) {
		Row row = sheet.getRow(rowIndex);
		if (null == row) {
			row = sheet.createRow(rowIndex);
		}
		row.setHeightInPoints(rowHeight);
		Cell cell = row.createCell(cellIndex);
		if (StringUtils.isNotBlank(value)) {
			cell.setCellValue(value);
		}
		if (null != cellStyle) {
			cellStyle.setWrapText(true);
			cell.setCellStyle(cellStyle);
		}
		return cell;
	}

	public static void main(String[] args) {
		SettlementJsonBean settlement = new SettlementJsonBean();
		settlement.setGroupNo("KCE0023");
		settlement.setPeopleCount("22");
		settlement.setInvoiceDate("2015-08-09");
		settlement.setItinerary("海南--北京");
		settlement.setAirlineCompany("南航");
		settlement.setTicketName("李四");
		settlement.setTravelPeriod("2013-09-09 至 2015-09-09");
		settlement.setSupplierName("唐辉国旅");
		settlement.setOrderer("张三");
		settlement.setRefundSum("$123,450.00");
		settlement.setRefundSumRMB("¥123,450.00");
		settlement.setInComeSum("$2,300.00+€300.00");
		settlement.setInComeSumRMB("¥5,300.00");
		settlement.setAdditionalCostSum("$2,400.00+€800.00");
		settlement.setAdditionalCostSumRMB("¥122,400.00");
		settlement.setCostSum("$2,300.00+¥300.00");
		settlement.setCostSumRMB("¥128,400.00");
		settlement.setGrossProfit("¥128,400.00");
		settlement.setGrossProfitRate("54.345%");
		int size = 0;
		List<InComeJsonBean> incomes = new ArrayList<InComeJsonBean>();
		for (int i = 0; i < size; i++) {
			InComeJsonBean income = new InComeJsonBean();
			income.setReceiveDate("2015-09-09");
			income.setCustomer("唐辉国旅" + i);
			income.setPeopleCount("22");
			income.setDeposit("$200.00");
			income.setBalancePayment("$300.00");
			income.setTotalAmount("$4000.00");
			income.setCurrencyMark("$");
			income.setExchangeRate("6.5");
			income.setRmb("¥128,400.00");
			incomes.add(income);
		}
		List<CostJsonBean> costs = new ArrayList<CostJsonBean>();
		for (int i = 0; i < size; i++) {
			CostJsonBean cost = new CostJsonBean();
			cost.setFundsName("测试项目");
			cost.setPNR("KCE01001");
			cost.setAirlineCompany("南航");
			cost.setPeopleCount("22");
			cost.setPrice("$200.00");
			cost.setTotalAmount("$4000.00");
			cost.setCurrencyMark("€");
			cost.setExchangeRate("6.5");
			cost.setRmb("¥128,400.00");
			costs.add(cost);
		}
		settlement.setCosts(costs);
		settlement.setIncomes(incomes);
		Workbook workbook = createSettlementExcel(settlement);
		try {
			workbook.write(new FileOutputStream(new File("d:/tt.xls")));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static String StrToDate(String str) {

		return DateUtils.date2String(DateUtils.string2Date(str, "yyyy-MM-dd"),
				"yyyy-MM-dd");

	}

	public static void exportExcel(String fileName, String[] columnTitles,
			String[] rowMsgs, List list, HttpServletRequest request,
			HttpServletResponse response, String orderType) throws IOException,
			IntrospectionException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		int rowNum = 0;
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet("sheet1");

		// 计算列数
		int countColumn = columnTitles.length;
		CellRangeAddress region = new CellRangeAddress(0, 0, 0, countColumn - 1);
		sheet.addMergedRegion(region);// 合并首行
		// sheet.setColumnWidth(2, 10);
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeight((short) 390);

		HSSFRow titleRow = sheet.createRow((short) rowNum++);
		// 字体设置
		HSSFFont font = workBook.createFont();
		font.setFontName("宋体");
		font.setFontHeight((short) HSSFFont.BOLDWEIGHT_NORMAL);
		font.setColor(HSSFFont.COLOR_NORMAL);

		// 样式
		HSSFCellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setFont(font);

		HSSFCell titleCell = titleRow.createCell(0);
		titleRow.setHeight((short) 500);// 设置首行高度
		// sheet.addMergedRegion(region)
		titleCell.setCellValue("游客信息");
		titleCell.setCellStyle(cellStyle);
		// 标题列
		/*
		 * for (int i = 0; i < countColumn;++i) { region = new
		 * CellRangeAddress(1,i,1,i+1); sheet.addMergedRegion(region);//合并标题列 }
		 */
		HSSFRow rowTwo = sheet.createRow(rowNum++);

		HSSFFont rowTwoCellFont = workBook.createFont();
		rowTwoCellFont.setFontName("宋体");
		rowTwoCellFont.setColor(HSSFFont.COLOR_NORMAL);

		HSSFCellStyle rowTwoCellStyle = workBook.createCellStyle();
		rowTwoCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		rowTwoCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		rowTwoCellStyle.setFont(rowTwoCellFont);

		for (int i = 0; i < countColumn; ++i) {
			HSSFCell rowTwoCell = rowTwo.createCell(i);
			rowTwoCell.setCellStyle(rowTwoCellStyle);
			rowTwoCell.setCellValue(columnTitles[i]);
		}
		for (int j = 0; j < list.size(); j++) {
			Map<Object, Object> o = (Map<Object, Object>) list.get(j);
			if (orderType.equals("orderCommon")) {
				List<Traveler> travelerList = (List<Traveler>) o
						.get("userList");
				for (int i = 0; i < travelerList.size(); ++i) {
					HSSFRow dataRow = sheet.createRow(rowNum++);
					HSSFCell dataCel = dataRow.createCell(0);
					dataCel.setCellStyle(rowTwoCellStyle);
					dataCel.setCellValue(rowNum - 2);
					if (i == 0) {
						for (int k = 0; k < 3; ++k) {
							dataCel = dataRow.createCell(k + 1);
							dataCel.setCellStyle(rowTwoCellStyle);
							dataCel.setCellValue(o.get(rowMsgs[k]) == null ? ""
									: o.get(rowMsgs[k]).toString());
						}
					}
					for (int k = 3; k < rowMsgs.length; ++k) {
						dataCel = dataRow.createCell(k + 1);
						dataCel.setCellStyle(rowTwoCellStyle);
						if (!rowMsgs[k].trim().equals("")) {
							PropertyDescriptor pd = new PropertyDescriptor(
									rowMsgs[k], Traveler.class);
							Method method = pd.getReadMethod();
							Object obj = method.invoke(travelerList.get(i));
							if (rowMsgs[k].trim().equals("sex")
									|| rowMsgs[k].trim().equals("travelSex")) {
								dataCel.setCellValue(obj == null ? "" : obj
										.toString().equals("1") ? "男" : "女");
							} else {
								dataCel.setCellValue(obj == null ? "" : obj
										.toString());
							}
						} else {
							dataCel.setCellValue("");
						}
					}

				}
			} else {
				List<Map<String, Object>> travelerList = (List<Map<String, Object>>) o
						.get("userList");
				for (int i = 0; i < travelerList.size(); ++i) {
					HSSFRow dataRow = sheet.createRow(rowNum++);
					HSSFCell dataCel = dataRow.createCell(0);
					dataCel.setCellStyle(rowTwoCellStyle);
					dataCel.setCellValue(rowNum - 2);
					if (i == 0) {
						for (int k = 0; k < 3; ++k) {
							dataCel = dataRow.createCell(k + 1);
							dataCel.setCellStyle(rowTwoCellStyle);
							dataCel.setCellValue(o.get(rowMsgs[k]) == null ? ""
									: o.get(rowMsgs[k]).toString());
						}
					}
					for (int k = 3; k < rowMsgs.length; ++k) {
						dataCel = dataRow.createCell(k + 1);
						dataCel.setCellStyle(rowTwoCellStyle);
						if (!rowMsgs[k].trim().equals("")) {
							if (rowMsgs[k].trim().equals("sex")
									|| rowMsgs[k].trim().equals("travelSex")) {
								dataCel.setCellValue(travelerList.get(i).get(
										rowMsgs[k]) == null ? "" : travelerList
										.get(i).get(rowMsgs[k]).toString()
										.equals("1") ? "男" : "女");
							} else {
								dataCel.setCellValue(travelerList.get(i).get(
										rowMsgs[k]) == null ? "" : travelerList
										.get(i).get(rowMsgs[k]).toString());
							}
						} else {
							dataCel.setCellValue("");
						}
					}

				}
			}
		}
		// 导出excel文档
		OutputStream op = null;
		fileName = fileName + ".xls";
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		setFileDownloadHeader(request, response, fileName);

		op = response.getOutputStream();
		workBook.write(op);
		op.close();

	}

}
