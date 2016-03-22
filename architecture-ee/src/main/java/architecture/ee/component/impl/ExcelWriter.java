package architecture.ee.component.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import architecture.common.jdbc.schema.Table;

/**
 * @author donghyuck
 */
public class ExcelWriter {

    private Log log = LogFactory.getLog(getClass());
    /**
     * @uml.property name="workbook"
     */
    private Workbook workbook;
    /**
     * @uml.property name="sheetIndex"
     */
    private int sheetIndex = 0;
    /**
     * @uml.property name="style"
     */
    private CellStyle style;

    public ExcelWriter() {
	workbook = new HSSFWorkbook();
	this.style = createCellStyle();
    }

    public Cell addCell(int rownum, int column) {
	Cell cell = getRow(rownum).createCell(column);
	cell.setCellStyle(getStyle());
	return cell;
    }

    public Row addRow(int rownum) {
	Sheet sheet = getSheetAt(getSheetIndex());
	return sheet.createRow(rownum);
    }

    public void addSheet(String name) {
	Sheet sheet = workbook.createSheet(name);
	sheetIndex = workbook.getSheetIndex(sheet);
    }

    private CellStyle createCellStyle() {
	CellStyle style = workbook.createCellStyle();
	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	style.setBorderBottom(CellStyle.BORDER_THIN);
	style.setBottomBorderColor(HSSFColor.BLACK.index);
	style.setBorderLeft(CellStyle.BORDER_THIN);
	style.setLeftBorderColor(HSSFColor.GREEN.index);
	style.setBorderRight(CellStyle.BORDER_THIN);
	style.setRightBorderColor(HSSFColor.BLUE.index);
	style.setBorderTop(CellStyle.BORDER_THIN);
	// style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM_DASHED);
	style.setTopBorderColor(HSSFColor.BLACK.index);
	// style.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
	return style;
    }

    private Font createFont() {
	Font font = workbook.createFont();
	return font;
    }

    public int getFirstRowNum() {
	return getSheetAt(getSheetIndex()).getFirstRowNum();
    }

    public int getLastRowNum() {
	return getSheetAt(getSheetIndex()).getLastRowNum();
    }

    public int getNumberOfSheets() {
	return workbook.getNumberOfSheets();
    }

    public int getPhysicalNumberOfCells(int rownum) {
	return getRow(rownum).getPhysicalNumberOfCells();
    }

    public int getPhysicalNumberOfRows() {
	return getSheetAt(getSheetIndex()).getPhysicalNumberOfRows();
    }

    public Row getRow(int rownum) {
	return getSheetAt(getSheetIndex()).getRow(rownum);
    }

    public Sheet getSheetAt(int sheetIndex) {
	return workbook.getSheetAt(sheetIndex);
    }

    /**
     * @return
     * @uml.property name="sheetIndex"
     */
    public int getSheetIndex() {
	return sheetIndex;
    }

    /**
     * @return
     * @uml.property name="style"
     */
    public CellStyle getStyle() {
	return style;
    }

    /**
     * @return
     * @uml.property name="workbook"
     */
    public Workbook getWorkbook() {
	return workbook;
    }

    public void setDataToRow(Map<String, Object> data, Table table) {
	Row row = addRow(getLastRowNum() + 1);
	int column = 0;
	for (String columnName : table.getColumnNames()) {

	    Cell cell = addCell(row.getRowNum(), column);
	    Object value = data.get(columnName);
	    int type = table.getColumn(columnName).getType();
	    if (type == java.sql.Types.DECIMAL) {
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		if (value != null)
		    cell.setCellValue(((BigDecimal) value).doubleValue());
		else
		    cell.setCellValue(0);
	    } else if (type == java.sql.Types.VARCHAR) {
		cell.setCellType(Cell.CELL_TYPE_STRING);
		if (value != null)
		    cell.setCellValue(value.toString());
		else
		    cell.setCellValue("");
	    } else {
		cell.setCellType(Cell.CELL_TYPE_STRING);
		if (value != null)
		    cell.setCellValue(value.toString());
		else
		    cell.setCellValue("");
	    }
	    column++;
	}
    }

    public void setHeaderToFirstRow(Table table) {

	String[] names = table.getColumnNames();
	int rowNum = getFirstRowNum();
	Row row = addRow(rowNum);

	int column = 0;
	CellStyle style = createCellStyle();
	// style.setFillBackgroundColor((short)0x16);
	style.setFillForegroundColor((short) 0x16);
	style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	Font font = workbook.createFont();
	font.setBoldweight(Font.BOLDWEIGHT_BOLD);
	style.setFont(font);
	for (String name : names) {
	    Cell cell = getRow(row.getRowNum()).createCell(column);
	    cell.setCellStyle(style);
	    cell.setCellValue(name);
	    column++;
	}

    }

    /**
     * @param sheetIndex
     * @uml.property name="sheetIndex"
     */
    public void setSheetIndex(int sheetIndex) {
	this.sheetIndex = sheetIndex;
    }

    /**
     * @param style
     * @uml.property name="style"
     */
    public void setStyle(CellStyle style) {
	this.style = style;
    }

    /**
     * 
     * @param workbook
     * @uml.property name="workbook"
     */
    public void setWorkbook(Workbook workbook) {
	this.workbook = workbook;
    }

    public void write(File file) {
	FileOutputStream fs = null;
	try {

	    // File.createTempFile(prefix, suffix);

	    fs = new FileOutputStream(file);
	    workbook.write(fs);
	} catch (IOException e) {
	} finally {
	    if (fs != null)
		try {
		    fs.close();
		} catch (IOException e) {
		}
	}
    }

    public void write(OutputStream output) {
	try {
	    workbook.write(output);
	} catch (IOException e) {
	} finally {
	    if (output != null)
		try {
		    output.close();
		} catch (IOException e) {
		}
	}
    }
}
