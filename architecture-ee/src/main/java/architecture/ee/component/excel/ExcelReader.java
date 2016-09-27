/*
 * Copyright 2016 donghyuck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package architecture.ee.component.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excel 파일을 쉽게 읽기 위한 유틸리티 클래스 
 * 
 * 
 * @author donghyuck
 *
 */
public class ExcelReader {

    private Logger log = LoggerFactory.getLogger(getClass());
    
    private Workbook workbook;

    private int sheetIndex = 0;

    public ExcelReader(File file) throws IOException, EncryptedDocumentException, InvalidFormatException {
	this.workbook = read(new FileInputStream(file));
    }

    public ExcelReader(InputStream inputStream) throws IOException, EncryptedDocumentException, InvalidFormatException {
	this.workbook = read(inputStream);
    }

    public ExcelReader(String uri) throws IOException, EncryptedDocumentException, InvalidFormatException {
	this(new File(URI.create(uri)));
    }

    public boolean getBooleanCellValue(Cell cell) {
	return cell.getBooleanCellValue();
    }

    public List<Map<String, String>> getDataAsList() {
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	List<String> keys = getHeaderFromFirstRow();
	for (int i = 1; i < getPhysicalNumberOfRows(); i++) {
	    Map<String, String> map = new HashMap<String, String>();
	    Row row = getRow(i);
	    int cells = row.getPhysicalNumberOfCells();
	    for (int c = 0; c < cells; c++) {
		Cell cell = row.getCell(c);
		String key = keys.get(c);
		String value = null;
		try {
		    value = cell.toString();
		} catch (NullPointerException e) {
		    value = "";
		}
		log.debug(key + "=" + value);
		map.put(key, value);
	    }
	    list.add(map);
	}
	return list;
    }

    public Map<String, String> getDataAsMapFromRow(int rownum) {
	Map<String, String> map = new HashMap<String, String>();
	Row row = getRow(rownum);
	List<String> keys = getHeaderFromFirstRow();
	int cells = row.getPhysicalNumberOfCells();
	for (int c = 0; c < cells; c++) {
	    Cell cell = row.getCell(c);
	    String key = keys.get(c);
	    String value = cell.toString();
	    map.put(key, value);
	}
	return map;
    }

    public List<String> getDataFromRow(int rownum) {
	Row row = getRow(rownum);
	int cells = row.getPhysicalNumberOfCells();
	List<String> list = new ArrayList<String>(cells);
	for (int c = 0; c < cells; c++) {
	    Cell cell = row.getCell(c);
	    list.add(cell.toString());
	}
	return list;
    }

    public int getFirstRowNum() {
	return getSheetAt(getSheetIndex()).getFirstRowNum();
    }

    public List<String> getHeaderFromFirstRow() {
	Row row = getRow(getFirstRowNum());
	int cells = row.getPhysicalNumberOfCells();
	List<String> list = new ArrayList<String>(cells);
	for (int c = 0; c < cells; c++) {
	    Cell cell = row.getCell(c);
	    list.add(cell.toString());
	}
	return list;
    }

    public int getLastRowNum() {
	return getSheetAt(getSheetIndex()).getLastRowNum();
    }

    public int getNumberOfSheets() {
	return workbook.getNumberOfSheets();
    }

    public double getNumericCellValue(Cell cell) {
	return cell.getNumericCellValue();
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


    public int getSheetIndex() {
	return sheetIndex;
    }

    public String getSheetName() {
	return workbook.getSheetName(getSheetIndex());
    }

    public String getStringCellValue(Cell cell) {
	return cell.getStringCellValue();
    }

    public boolean isBooleanCell(Cell cell) {
	if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
	    return true;
	return false;
    }

    public boolean isEmptyCell(Cell cell) {
	if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
	    return true;
	return false;
    }

    public boolean isNumericCell(Cell cell) {
	if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
	    return true;
	return false;
    }

    public boolean isStringCell(Cell cell) {
	if (cell.getCellType() == Cell.CELL_TYPE_STRING)
	    return true;
	return false;
    }

    private Workbook read(InputStream inputStream)
	    throws IOException, EncryptedDocumentException, InvalidFormatException {
	return WorkbookFactory.create(inputStream);
    }

    public void setSheetIndex(int sheetIndex) {
	this.sheetIndex = sheetIndex;
    }
}