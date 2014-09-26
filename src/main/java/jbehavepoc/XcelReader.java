package xelenium;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class XcelReader {
	
	private InputStream  _file;
	private String _sheetName;
	private List<HashMap<String,String>> _data;
	private Iterator<HashMap<String,String>> _dataIterator;
	private HashMap<String,String> _currentRow;
	
	public XcelReader(String filePath, String sheetName){
        _file = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
		_sheetName = sheetName;
		read();
	}
	
	/**
	 * Gets the data from the excel sheet in List<Hashtable<String,String>> format
	 * @return
	 */
	public List<HashMap<String,String>> data(){
		if(_data == null){
			_data = new ArrayList<HashMap<String, String>>();
		}
		return _data;
	}
	
	/**
	 * Gets an iterator that is based on the value returned from data()
	 * @return and Iterator
	 */
	private Iterator<HashMap<String,String>> dataIterator(){
		if(_dataIterator == null){
			_dataIterator = data().iterator();
		}
		return _dataIterator;
	}
	/**
	 * Gets the current row that the XcelReader is located at.  Used for iterating through rows in the sheet.
	 * @return the current row.
	 */
	public HashMap<String,String> currentRow(){
		if(_currentRow == null){
			_currentRow = next();
		}
		return _currentRow;
	}
	
	/**
	 * Get whether or not the dataIterator has another row that can be accessed beyond the current one.
	 * Same as Iterator.hasNext()
	 * @return true or false
	 */
	public boolean hasNext(){
		dataIterator();
		return _dataIterator.hasNext();
	}
	
	/**
	 * Iterates the XcelReader to the next row in the sheet, and sets currentRow to that row.
	 * @return the next available row in the sheet.  If there are no more available rows, null is returned.
	 */
	public HashMap<String,String> next(){
		dataIterator();
		if(_dataIterator.hasNext()){
			_currentRow = _dataIterator.next();
			return currentRow();
		}
		else{
			_currentRow = null;
			return null;
		}
	}
	
	/**
	 * Get the value associated with the specified column from the currentRow()
	 * @param column
	 * @return value
	 */
	public String getValue(String column){
		return currentRow().get(column);
	}
	
	/**
	 * Get all values associated with the specified column throughout the entire sheet
	 * @param column
	 * @return values
	 */
	public List<String> getValues(String column){
		List<String> columnValues = new ArrayList<String>();
		for(HashMap<String,String> ht : data()){
			columnValues.add(ht.get(column));
		}
		return columnValues;
	}
	
	/**
	 * Reads the excel sheet and parses it into the List<Hashtable<String,String> for accessing
	 */
	private void read(){
		data();
		HSSFWorkbook workbook;
		try {
			workbook = new HSSFWorkbook(_file);
		} catch (Throwable e) {
			throw new XeleniumException(e);
		}
		HSSFSheet sheet = workbook.getSheet(_sheetName);
		
		Iterator<Row> rows = sheet.iterator();
		List<String> columns = new ArrayList<String>();
		boolean firstRow = true;
		while(rows.hasNext()){
			Row row = rows.next();
			Iterator<Cell> cells = row.cellIterator();
			if(firstRow){
				while(cells.hasNext()){
					Cell cell = cells.next();
					columns.add(cell.getStringCellValue());
				}
				firstRow = false;
			}
			else{
				HashMap<String,String> rowData = new HashMap<String, String>();
				while(cells.hasNext()){
					Cell cell = cells.next();
					rowData.put(columns.get(cell.getColumnIndex()), cell.getStringCellValue());
				}
				_data.add(rowData);
			}
		}
	}
}
