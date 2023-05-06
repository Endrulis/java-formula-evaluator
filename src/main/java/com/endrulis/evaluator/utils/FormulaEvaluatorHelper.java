package com.endrulis.evaluator.utils;

import com.endrulis.evaluator.formula.evaluators.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class FormulaEvaluatorHelper {
    public static void evaluateLogical(FormulaEvaluator evaluator, Cell cell, String formula) {
        Evaluator logicalEvaluator = new LogicalEvaluator(formula);
        logicalEvaluator.evaluate(evaluator, cell);
    }
    public static void evaluateComparison( FormulaEvaluator evaluator, Cell cell, String formula ) {
        Evaluator comparisonEvaluator = new ComparisonEvaluator(formula);
        comparisonEvaluator.evaluate(evaluator, cell);
    }
    public static void evaluateFormulaWithNumberValue(FormulaEvaluator evaluator, Cell cell, String evaluatedFormula) {
        Evaluator numberValueEvaluator = new NumberValueEvaluator(evaluatedFormula);
        numberValueEvaluator.evaluate(evaluator, cell);
    }
    public static void evaluateFormulaWithStringValue( FormulaEvaluator evaluator, Cell cell, String formula ) {
        Evaluator stringValueEvaluator = new StringValueEvaluator(formula);
        stringValueEvaluator.evaluate(evaluator, cell);
    }
}
