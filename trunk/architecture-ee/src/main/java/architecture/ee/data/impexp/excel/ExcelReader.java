package architecture.ee.data.impexp.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs.FileObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import architecture.common.vfs.VFSUtils;


public class ExcelReader {
	
	private Workbook workbook;
	
    private int sheetIndex = 0 ;

	public ExcelReader(String uri) throws IOException {
		FileObject fo = VFSUtils.resolveFile(uri);		
		this.workbook = read(fo.getContent().getInputStream());
	}
	
	public ExcelReader(File file) throws IOException {
		this.workbook = read(new FileInputStream(file));
	}
	
	public ExcelReader(InputStream inputStream) throws IOException {
		this.workbook = read(inputStream);
	}

	private Workbook read (InputStream inputStream) throws IOException {
		if(inputStream.markSupported()){
			inputStream = new PushbackInputStream(inputStream, 8);
		}
		if(POIFSFileSystem.hasPOIFSHeader(inputStream)){
			
		}
		//if(POIXMLDocument.hasOOXMLHeader(inp)) {
		//	return new XSSFWorkbook(OPCPackage.open(inp));
		//}
		return new HSSFWorkbook (inputStream);
	}

	public int getLastRowNum(){
		return getSheetAt(getSheetIndex()).getLastRowNum();
	}
	
	public int getFirstRowNum(){
		return getSheetAt(getSheetIndex()).getFirstRowNum();
	}
	
	public Row getRow(int rownum){
		return getSheetAt(getSheetIndex()).getRow(rownum);
	}
	
	public int getPhysicalNumberOfCells(int rownum){
		return getRow(rownum).getPhysicalNumberOfCells();
	}
	
	public int getPhysicalNumberOfRows(){
		return getSheetAt(getSheetIndex()).getPhysicalNumberOfRows();
	}
	
	
	public Sheet getSheetAt(int sheetIndex){
		return workbook.getSheetAt(sheetIndex);
	} 
	
	public int getNumberOfSheets(){
		return workbook.getNumberOfSheets();
	}

	public int getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}	

	public String getSheetName(){
		return workbook.getSheetName(getSheetIndex());
	}
	
	public boolean isBooleanCell(Cell cell){
		if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
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

	public boolean isEmptyCell(Cell cell){
		if(cell.getCellType() == Cell.CELL_TYPE_BLANK )
			return true;		
		return false;		
	}
	
	public double getNumericCellValue(Cell cell){		
		return cell.getNumericCellValue();		
	}

	public boolean getBooleanCellValue(Cell cell){		
		return cell.getBooleanCellValue();		
	}

	public String getStringCellValue(Cell cell){		
		return cell.getStringCellValue();
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
			    String value = cell.toString();
				map.put(key, value); 
			}
			list.add(map);
		}
		return list;
	}
}
