package com.endrulis.evaluator.formula;

import org.apache.poi.ss.usermodel.*;

import java.util.List;

import static com.endrulis.evaluator.utils.FormulaEvaluatorUtils.*;

public class FormulaEvaluator {
    public static void evaluateFormulasInNewSheet( org.apache.poi.ss.usermodel.FormulaEvaluator evaluator, List<List<Object>> mySheetData, Sheet newSheet ) {
        for (int i = 0; i < mySheetData.size(); i++) {
            Row row = newSheet.getRow(i);
            for (int j = 0; j < mySheetData.get(i).size(); j++) {
                Cell cell = row.getCell(j);
                Object cellValue = mySheetData.get(i).get(j);
                if (cellValue instanceof String && ((String) cellValue).startsWith("=")) {
                    String formula = ((String) cellValue).substring(1);
                    evaluateFormulaInCell(evaluator, cell, formula);
                }
            }
        }
    }

    private static void evaluateFormulaInCell( org.apache.poi.ss.usermodel.FormulaEvaluator evaluator, Cell cell, String formula ) {
        if (formula.startsWith("MULTIPLY(")) {
            cell.setCellFormula(evaluateMultiply(formula));
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getNumberValue());
        }else if (formula.startsWith("DIVIDE(")) {
            cell.setCellFormula(evaluateDivision(formula));
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getNumberValue());
        }else if (formula.startsWith("GT(")) {
            cell.setCellFormula(evaluateGT(formula));
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getNumberValue());
            cell.setCellType(CellType.BOOLEAN);
        }else if (formula.startsWith("EQ(")) {
            cell.setCellFormula(evaluateEQ(formula));
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getNumberValue());
            cell.setCellType(CellType.BOOLEAN);
        }else if (formula.startsWith("NOT(")) {
            cell.setCellFormula(evaluateNOT(formula));
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getNumberValue());
            cell.setCellType(CellType.BOOLEAN);
        }else if(formula.startsWith("AND(")){
            cell.setCellFormula(evaluateAND(formula));
            System.out.println(cell.getCellFormula());
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getBooleanValue());
            cell.setCellType(CellType.BOOLEAN);
        }else if (formula.startsWith("IF(")) {
            cell.setCellFormula(evaluateIF(formula));
        }
        else {
            cell.setCellFormula(formula);
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getNumberValue());
        }
    }
}
