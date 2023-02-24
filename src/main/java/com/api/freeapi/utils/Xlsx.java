package com.api.freeapi.utils;

/**
 * Author: 于顺清
 * Coopyright(C),2019-2022
 * FileName：Xlsx
 * Date:     2022/1/11/01118:37
 * Description:yu1
 */
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class Xlsx {
//标题，内容，下表名
public XSSFWorkbook getWorkBook(List<String> titleList, List<List<String>> contentlist, String sheetName) {
    XSSFWorkbook xwk = new XSSFWorkbook();
    XSSFDataFormat format = xwk.createDataFormat();
    XSSFCellStyle cellStyle = xwk.createCellStyle();
    XSSFSheet xssfSheet = xwk.createSheet(sheetName);
    cellStyle.setDataFormat(format.getFormat("@"));//文本格式             
    int j = 0;
    createHeader(xssfSheet, cellStyle, titleList, j);
    int size = contentlist.size();
    for (j = 0; j < size; j++) {
        List<String> oneRow = contentlist.get(j);
        createContent(xssfSheet, cellStyle, oneRow, j);
        oneRow = null;
    }
    return xwk;
}
private void createHeader(XSSFSheet xssfSheet, XSSFCellStyle cellStyle, List<String> titleList, int j) {

    XSSFRow rowTitle = xssfSheet.createRow(j);
    for (int cellTitle = 0; cellTitle < titleList.size(); cellTitle++) {
        Cell cellIndex = rowTitle.createCell(cellTitle);
        cellIndex.setCellStyle(cellStyle);
        cellIndex.setCellValue(titleList.get(cellTitle));
    }
}
private void createContent(XSSFSheet xssfSheet, XSSFCellStyle cellStyle, List<String> oneRow, int j) {
    XSSFRow rowContent = xssfSheet.createRow(j + 1);
    for (int cellContent = 0; cellContent < oneRow.size(); cellContent++) {
        Cell cellIndex = rowContent.createCell(cellContent);
        cellIndex.setCellStyle(cellStyle);
        cellIndex.setCellValue(oneRow.get(cellContent));
    }
  }
}

