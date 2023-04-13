package com.endrulis.evaluator.utils;

import com.endrulis.evaluator.entities.MySheet;
import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.List;

public class WorkbookUtils {
    public static List<List<Object>> getUpdatedSheetData( FormulaEvaluator evaluator, Sheet newSheet ) {
        List<List<Object>> updatedSheetData = new ArrayList<>();
        for (Row row : newSheet) {
            List<Object> rowData = new ArrayList<>();
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:
                        rowData.add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        rowData.add(cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        rowData.add(cell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        CellValue cellValue = evaluator.evaluate(cell);
                        if(cellValue.getCellType() == CellType.BOOLEAN) {
                            rowData.add(cellValue.getBooleanValue());
                        }else if (cellValue.getCellType() == CellType.STRING) {
                            rowData.add(cellValue.getStringValue());
                        }else if(cellValue.getCellType() == CellType.NUMERIC){
                            rowData.add(cellValue.getNumberValue());
                        }
                        break;
                    default:
                        rowData.add(null);
                        break;
                }
            }
            updatedSheetData.add(rowData);
        }
        return updatedSheetData;
    }
    public static Sheet createNewSheet( Workbook workbook, MySheet mySheet ) {
        Sheet newSheet = workbook.createSheet(mySheet.getId());
        return newSheet;
    }


    public static void fillNewSheetWithData( Sheet newSheet, List<List<Object>> mySheetData ) {
        for (int i = 0; i < mySheetData.size(); i++) {
            Row row = newSheet.createRow(i);
            for (int j = 0; j < mySheetData.get(i).size(); j++) {
                Cell cell = row.createCell(j);
                Object cellValue = mySheetData.get(i).get(j);
                setCellValue(cell, cellValue);
            }
        }
    }
    private static void setCellValue(Cell cell, Object cellValue) {
        if (cellValue instanceof Integer) {
            cell.setCellValue((Integer) cellValue);
        } else if (cellValue instanceof Boolean) {
            cell.setCellValue((Boolean) cellValue);
        } else if (cellValue instanceof String) {
            cell.setCellValue(cellValue.toString());
        } else {
            cell.setCellValue(Double.parseDouble(cellValue.toString()));
        }
    }
}
