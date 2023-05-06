package com.endrulis.evaluator.formula.evaluators;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class StringValueEvaluator implements Evaluator {
    private String formula;

    public StringValueEvaluator(String formula) {
        this.formula = formula;
    }

    @Override
    public void evaluate( FormulaEvaluator evaluator, Cell cell) {
        cell.setCellFormula(formula);
        CellValue formulaValue = evaluator.evaluate(cell);
        cell.setCellValue(formulaValue.getStringValue());
        cell.setCellType(CellType.STRING);
    }
}
