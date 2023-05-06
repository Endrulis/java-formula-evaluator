package com.endrulis.evaluator.formula;

import com.endrulis.evaluator.formula.parser.*;
import com.endrulis.evaluator.utils.FormulaEvaluatorHelper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;



public class CellFormulaHandler {
    public static void evaluateFormulaInCell( FormulaEvaluator evaluator, Cell cell, String formula ) {
        if (formula.startsWith("SUM(MULTIPLY(")) {
            FormulaEvaluatorHelper.evaluateFormulaWithNumberValue(evaluator, cell, new SumMultiplyFormulaParser().evaluateFormula(formula));
        } else if (formula.startsWith("MULTIPLY(")) {
            FormulaEvaluatorHelper.evaluateFormulaWithNumberValue(evaluator, cell, new MultiplicationFormulaParser().evaluateFormula(formula));
        }  else if (formula.startsWith("DIVIDE(")) {
            FormulaEvaluatorHelper.evaluateFormulaWithNumberValue(evaluator, cell, new DivisionFormulaParser().evaluateFormula(formula));
        } else if (formula.startsWith("GT(")) {
            FormulaEvaluatorHelper.evaluateComparison(evaluator, cell, new GtFormulaParser().evaluateFormula(formula));
        }else if (formula.startsWith("LT(")) {
            FormulaEvaluatorHelper.evaluateComparison(evaluator, cell, new LtFormulaParser().evaluateFormula(formula));
        }else if (formula.startsWith("EQ(")) {
            FormulaEvaluatorHelper.evaluateComparison(evaluator, cell, new EqFormulaParser().evaluateFormula(formula));
        } else if (formula.startsWith("NOT(")) {
            FormulaEvaluatorHelper.evaluateComparison(evaluator, cell, new NotFormulaParser().evaluateFormula(formula));
        } else if (formula.startsWith("AND(")) {
            FormulaEvaluatorHelper.evaluateLogical(evaluator, cell, new AndFormulaParser().evaluateFormula(formula));
        }else if (formula.startsWith("OR(")) {
            FormulaEvaluatorHelper.evaluateLogical(evaluator, cell, new OrFormulaParser().evaluateFormula(formula));
        } else if (formula.startsWith("IF(")) {
            FormulaEvaluatorHelper.evaluateFormulaWithNumberValue(evaluator, cell, new IfFormulaParser().evaluateFormula(formula));
        } else if (formula.startsWith("CONCAT(")) {
            FormulaEvaluatorHelper.evaluateFormulaWithStringValue(evaluator, cell, formula);
        } else {
            cell.setCellFormula(formula);
        }
    }
}
