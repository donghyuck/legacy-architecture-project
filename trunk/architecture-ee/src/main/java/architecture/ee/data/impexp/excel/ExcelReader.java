package architecture.ee.data.impexp.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class ExcelReader {
	
	private Workbook workbook;
	
    private int sheetIndex = 0 ;
    
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

	public List<String> getDataFromRow (int rownum){
		Row row = getRow(rownum);
		int cells = row.getPhysicalNumberOfCells();		
		List<String> list = new ArrayList<String>(cells);
		for( int c = 0; c < cells; c++ ){
			Cell cell = row.getCell(c);			
			list.add( cell.toString() );			 
		}
		return list;
	}
	
}
