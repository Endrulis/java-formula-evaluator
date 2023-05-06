package com.endrulis.evaluator.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

public class DataTypeChecker {
    public static void checkDataTypesInRow( Row row, Cell cell, String formula) {
        if (formula != null && (formula.startsWith("AND(") || formula.startsWith("OR("))) {
            for (int k = 0; k < row.getLastCellNum(); k++) {
                if (k != cell.getColumnIndex() && row.getCell(k).getCellType() != cell.getCellType()) {
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue("#ERROR: type does not match");
                    break;
                }
            }
        }
    }
}
