package com.endrulis.evaluator.utils;

import com.endrulis.evaluator.entities.MySheet;
import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.List;

public class WorkbookUtils {
    public static List<List<Object>> getUpdatedSheetData( FormulaEvaluator evaluator, Sheet newSheet ) {
        List<List<Object>> updatedSheetData = new ArrayList<>();
        for (Row row : newSheet) {
            List<Object> rowData = getRowData(evaluator, row);
            updatedSheetData.add(rowData);
        }
        return updatedSheetData;
    }

    private static List<Object> getRowData( FormulaEvaluator evaluator, Row row ) {
        List<Object> rowData = new ArrayList<>();
        for (Cell cell : row) {
            Object cellValue = getCellValue(evaluator, cell);
            rowData.add(cellValue);
        }
        return rowData;
    }

    private static Object getCellValue( FormulaEvaluator evaluator, Cell cell ) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellType()) {
                    case BOOLEAN:
                        return cellValue.getBooleanValue();
                    case STRING:
                        return cellValue.getStringValue();
                    case NUMERIC:
                        return cellValue.getNumberValue();
                    default:
                        return null;
                }
            default:
                return null;
        }
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

    private static void setCellValue( Cell cell, Object cellValue ) {
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
