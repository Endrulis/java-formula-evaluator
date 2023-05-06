package com.endrulis.evaluator.formula;

import com.endrulis.evaluator.utils.ConditionalFormulaParser;
import com.endrulis.evaluator.utils.FormulaEvaluatorHelper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;



public class CellFormulaHandler {
    public static void evaluateFormulaInCell( FormulaEvaluator evaluator, Cell cell, String formula ) {
        if (formula.startsWith("SUM(MULTIPLY(")) {
            FormulaEvaluatorHelper.evaluateFormulaWithNumberValue(evaluator, cell, ConditionalFormulaParser.evaluateSum(formula));
        } else if (formula.startsWith("MULTIPLY(") || formula.startsWith("DIVIDE(")) {
            FormulaEvaluatorHelper.evaluateFormulaWithNumberValue(evaluator, cell, ConditionalFormulaParser.evaluateMultiplyOrDivision(formula));
        } else if (formula.startsWith("GT(") ||
                formula.startsWith("LT(") || formula.startsWith("EQ(")) {
            FormulaEvaluatorHelper.evaluateComparison(evaluator, cell, ConditionalFormulaParser.evaluateGtOrLtOrEq(formula));
        } else if (formula.startsWith("NOT(")) {
            FormulaEvaluatorHelper.evaluateComparison(evaluator, cell, ConditionalFormulaParser.evaluateNot(formula));
        } else if (formula.startsWith("AND(") || formula.startsWith("OR(")) {
            FormulaEvaluatorHelper.evaluateLogical(evaluator, cell, ConditionalFormulaParser.evaluateAndOrOr(formula));
        } else if (formula.startsWith("IF(")) {
            FormulaEvaluatorHelper.evaluateFormulaWithNumberValue(evaluator, cell, ConditionalFormulaParser.evaluateIf(formula));
        } else if (formula.startsWith("CONCAT(")) {
            FormulaEvaluatorHelper.evaluateFormulaWithStringValue(evaluator, cell, formula);
        } else {
            cell.setCellFormula(formula);
        }
    }
}
