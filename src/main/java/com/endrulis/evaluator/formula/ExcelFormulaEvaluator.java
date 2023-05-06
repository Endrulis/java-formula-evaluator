package com.endrulis.evaluator.formula;

import com.endrulis.evaluator.utils.DataTypeChecker;
import com.endrulis.evaluator.utils.FormulaParser;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

public class ExcelFormulaEvaluator {
    public static void evaluateFormulasInNewSheet( FormulaEvaluator evaluator, List<List<Object>> mySheetData, Sheet newSheet ) {
        for (int i = 0; i < mySheetData.size(); i++) {
            Row row = newSheet.getRow(i);
            for (int j = 0; j < mySheetData.get(i).size(); j++) {
                Cell cell = row.getCell(j);
                Object cellValue = mySheetData.get(i).get(j);
                if (cellValue instanceof String && ((String) cellValue).startsWith("=")) {
                    String formula = FormulaParser.parseFormula((String) cellValue);
                    CellFormulaHandler.evaluateFormulaInCell(evaluator, cell, formula);
                    DataTypeChecker.checkDataTypesInRow(row, cell, formula);
                }
            }
        }
    }
}
