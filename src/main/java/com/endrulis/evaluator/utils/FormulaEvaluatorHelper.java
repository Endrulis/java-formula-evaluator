package com.endrulis.evaluator.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class FormulaEvaluatorHelper {
    public static void evaluateLogical( FormulaEvaluator evaluator, Cell cell, String formula ) {
        cell.setCellFormula(formula);
        CellValue formulaValue = evaluator.evaluate(cell);
        cell.setCellValue(formulaValue.getBooleanValue());
        cell.setCellType(CellType.BOOLEAN);
    }
    public static void evaluateComparison( FormulaEvaluator evaluator, Cell cell, String formula ) {
        cell.setCellFormula(formula);
        CellValue formulaValue = evaluator.evaluate(cell);
        cell.setCellValue(formulaValue.getNumberValue());
        cell.setCellType(CellType.BOOLEAN);
    }
    public static void evaluateFormulaWithNumberValue(FormulaEvaluator evaluator, Cell cell, String evaluatedFormula) {
        cell.setCellFormula(evaluatedFormula);
        CellValue formulaValue = evaluator.evaluate(cell);
        cell.setCellValue(formulaValue.getNumberValue());
    }
    public static void evaluateFormulaWithStringValue( FormulaEvaluator evaluator, Cell cell, String formula ) {
        cell.setCellFormula(formula);
        CellValue formulaValue = evaluator.evaluate(cell);
        cell.setCellValue(formulaValue.getStringValue());
        cell.setCellType(CellType.STRING);
    }
}
