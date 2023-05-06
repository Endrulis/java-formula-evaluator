package com.endrulis.evaluator.formula.evaluators;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class LogicalEvaluator implements Evaluator {
    private final String formula;

    public LogicalEvaluator( String formula ) {
        this.formula = formula;
    }

    @Override
    public void evaluate( FormulaEvaluator evaluator, Cell cell ) {
        cell.setCellFormula(formula);
        CellValue formulaValue = evaluator.evaluate(cell);
        cell.setCellValue(formulaValue.getBooleanValue());
        cell.setCellType(CellType.BOOLEAN);
    }
}
