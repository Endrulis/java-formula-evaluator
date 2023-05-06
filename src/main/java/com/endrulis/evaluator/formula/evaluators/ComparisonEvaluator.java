package com.endrulis.evaluator.formula.evaluators;

import com.endrulis.evaluator.formula.FormulaParser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class ComparisonEvaluator implements Evaluator {
    private final String formula;

    public ComparisonEvaluator(String formula) {
        this.formula = formula;
    }

    @Override
    public void evaluate( FormulaEvaluator evaluator, Cell cell) {
        cell.setCellFormula(formula);
        CellValue formulaValue = evaluator.evaluate(cell);
        cell.setCellValue(formulaValue.getNumberValue());
        cell.setCellType(CellType.BOOLEAN);
    }
}
