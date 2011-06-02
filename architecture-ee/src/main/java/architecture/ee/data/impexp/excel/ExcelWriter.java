package architecture.ee.data.impexp.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelWriter {

	private HSSFWorkbook workbook ;
    private int sheetIndex = 0 ;
    
	public ExcelWriter() {
		workbook = new HSSFWorkbook();
	}
	
	public int getNumberOfSheets(){
		return workbook.getNumberOfSheets();
	}
	
	public void addSheet(String name){
		
		HSSFSheet sheet = workbook.createSheet(name);
		sheetIndex = workbook.getSheetIndex(sheet);
		
	}
}
