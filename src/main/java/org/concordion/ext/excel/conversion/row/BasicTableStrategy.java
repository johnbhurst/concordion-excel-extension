package org.concordion.ext.excel.conversion.row;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.concordion.ext.excel.conversion.AbstractConversionStrategy;
import org.concordion.ext.excel.conversion.ConversionStrategy;
import org.concordion.ext.excel.conversion.HTMLBuilder;

/**
 * This strategy collects rows and outputs them as a table.
 * 
 * @author robmoffat
 *
 */
public class BasicTableStrategy extends AbstractConversionStrategy<XSSFTable> {

	protected ConversionStrategy<Cell> bodyCell;
	protected ConversionStrategy<Cell> headerCell = null;
	
	public BasicTableStrategy(ConversionStrategy<Cell> bodyCell) {
		super();
		this.bodyCell = bodyCell;
	}
	
	public BasicTableStrategy(ConversionStrategy<Cell> bodyCell, ConversionStrategy<Cell> headerCell) {
		this(bodyCell);
		this.headerCell = headerCell;
	}

	@Override
	public void process(XSSFTable table, HTMLBuilder result) {
		boolean header = shouldOutputFirstRowAsHeader(table);
		int columnFrom = table.getStartCellReference().getCol();
		int columnTo = table.getEndCellReference().getCol();
		int rowFrom = table.getStartCellReference().getRow();
		int rowTo = table.getEndCellReference().getRow();
		XSSFSheet sheet = table.getXSSFSheet();
				
		result.startTag("table");
		for (int rowNum = rowFrom; rowNum <= rowTo; rowNum++) {
			Row r = sheet.getRow(rowNum);
			result.startTag("tr");
			for (int i = columnFrom; i <= columnTo; i++) {
				if (header) {
					headerCell.process(r.getCell(i), result);
				} else {
					bodyCell.process(r.getCell(i), result);
				}
			}
			
			header = false;
			result.endTag();
		}
		result.endTag();
	}

	protected boolean shouldOutputFirstRowAsHeader(XSSFTable t) {
		return true;
	}

}
