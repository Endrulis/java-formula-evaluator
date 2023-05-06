package com.endrulis.evaluator.formula.evaluators;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class NumberValueEvaluator implements Evaluator {
    private final String formula;

    public NumberValueEvaluator(String formula) {
        this.formula = formula;
    }

    @Override
    public void evaluate( FormulaEvaluator evaluator, Cell cell) {
        cell.setCellFormula(formula);
        CellValue formulaValue = evaluator.evaluate(cell);
        cell.setCellValue(formulaValue.getNumberValue());
    }
}
