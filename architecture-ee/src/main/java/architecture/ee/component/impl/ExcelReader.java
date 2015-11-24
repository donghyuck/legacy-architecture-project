package architecture.ee.component.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileObject;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import architecture.common.util.vfs.VFSUtils;


/**
 * @author  donghyuck
 */
public class ExcelReader {
	
	private Log log = LogFactory.getLog(getClass());
	private Workbook workbook;
	
    /**
	 * @uml.property  name="sheetIndex"
	 */
    private int sheetIndex = 0 ;

	public ExcelReader(File file) throws IOException, EncryptedDocumentException, InvalidFormatException {
		this.workbook = read(new FileInputStream(file));
	}
	
	public ExcelReader(InputStream inputStream) throws IOException, EncryptedDocumentException, InvalidFormatException {
		this.workbook = read(inputStream);
	}

	
	public ExcelReader(String uri) throws IOException, EncryptedDocumentException, InvalidFormatException {
		FileObject fo = VFSUtils.resolveFile(uri);		
		this.workbook = read(fo.getContent().getInputStream());
	}

	public boolean getBooleanCellValue(Cell cell){		
		return cell.getBooleanCellValue();		
	}

	public List<Map<String, String>> getDataAsList(){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<String> keys = getHeaderFromFirstRow();
		for( int i = 1 ; i < getPhysicalNumberOfRows() ; i ++){
			Map<String, String> map = new HashMap<String, String>();
			Row row = getRow(i);
			int cells = row.getPhysicalNumberOfCells();		
			for( int c = 0; c < cells; c++ ){
				Cell cell = row.getCell(c);
			    String key = keys.get(c);	   
			    String value = null; 			    
			    try {
					value = cell.toString();
				} catch (NullPointerException e) {
					value = "";
				}	
				log.debug(key + "=" + value );
				map.put(key, value); 
			}
			list.add(map);
		}
		return list;
	}
	
	public Map<String, String> getDataAsMapFromRow(int rownum){		
		Map<String, String> map = new HashMap<String, String>();
		Row row = getRow(rownum);
		List<String> keys = getHeaderFromFirstRow();
		int cells = row.getPhysicalNumberOfCells();		
		for( int c = 0; c < cells; c++ ){
			Cell cell = row.getCell(c);
		    String key = keys.get(c);
		    String value = cell.toString();
			map.put(key, value); 
		}
		return map;
	}
	
	public List<String> getDataFromRow(int rownum){
		Row row = getRow(rownum);
		int cells = row.getPhysicalNumberOfCells();		
		List<String> list = new ArrayList<String>(cells);
		for( int c = 0; c < cells; c++ ){
			Cell cell = row.getCell(c);			
			list.add( cell.toString() );			 
		}
		return list;
	}
	
	public int getFirstRowNum(){
		return getSheetAt(getSheetIndex()).getFirstRowNum();
	}
	
	public List<String> getHeaderFromFirstRow (){
		Row row = getRow(getFirstRowNum());
		int cells = row.getPhysicalNumberOfCells();		
		List<String> list = new ArrayList<String>(cells);
		for( int c = 0; c < cells; c++ ){
			Cell cell = row.getCell(c);			
			list.add( cell.toString() );			 
		}
		return list;
	}
	
	
	public int getLastRowNum(){
		return getSheetAt(getSheetIndex()).getLastRowNum();
	} 
	
	public int getNumberOfSheets(){
		return workbook.getNumberOfSheets();
	}

	public double getNumericCellValue(Cell cell){		
		return cell.getNumericCellValue();		
	}

	public int getPhysicalNumberOfCells(int rownum){
		return getRow(rownum).getPhysicalNumberOfCells();
	}	

	public int getPhysicalNumberOfRows(){
		return getSheetAt(getSheetIndex()).getPhysicalNumberOfRows();
	}
	
	public Row getRow(int rownum){
		return getSheetAt(getSheetIndex()).getRow(rownum);
	}
	
	public Sheet getSheetAt(int sheetIndex){
		return workbook.getSheetAt(sheetIndex);
	}

	/**
	 * @return
	 * @uml.property  name="sheetIndex"
	 */
	public int getSheetIndex() {
		return sheetIndex;
	}

	public String getSheetName(){
		return workbook.getSheetName(getSheetIndex());
	}
	
	public String getStringCellValue(Cell cell){		
		return cell.getStringCellValue();
	}

	public boolean isBooleanCell(Cell cell){
		if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
			return true;
		return false;
	}

	public boolean isEmptyCell(Cell cell){
		if(cell.getCellType() == Cell.CELL_TYPE_BLANK )
			return true;		
		return false;		
	}
	
	public boolean isNumericCell(Cell cell){
		if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
			return true;
		return false;		
	}

	public boolean isStringCell(Cell cell){
		if(cell.getCellType() == Cell.CELL_TYPE_STRING )
			return true;		
		return false;		
	}

	private Workbook read (InputStream inputStream) throws IOException, EncryptedDocumentException, InvalidFormatException {
		/*if(inputStream.markSupported()){
			inputStream = new PushbackInputStream(inputStream, 8);
		}*/
		/*
		if(POIFSFileSystem.hasPOIFSHeader(inputStream)){
			
		}
		if(POIXMLDocument.hasOOXMLHeader(inp)) {
			return new XSSFWorkbook(OPCPackage.open(inp));
		}
		*/
		return WorkbookFactory.create(inputStream);
		
		//return new HSSFWorkbook (inputStream);
	}
	
	/**
	 * @param sheetIndex
	 * @uml.property  name="sheetIndex"
	 */
	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}
}
