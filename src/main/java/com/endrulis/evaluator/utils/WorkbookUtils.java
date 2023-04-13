package com.endrulis.evaluator.utils;

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
                        rowData.add(cellValue.getNumberValue());
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
}
