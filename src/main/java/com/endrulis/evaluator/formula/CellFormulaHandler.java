package com.endrulis.evaluator.formula;

import com.endrulis.evaluator.formula.parser.*;
import com.endrulis.evaluator.utils.FormulaEvaluatorHelper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;


public class CellFormulaHandler {
    public static void evaluateFormulaInCell( FormulaEvaluator evaluator, Cell cell, String formula ) {
        FormulaParser parser = getParserForFormula(formula);
        if (parser != null) {
            String parsedFormula = parser.evaluateFormula(formula);
            if(!parsedFormula.isEmpty()){
                if(formula.startsWith("GT(") || formula.startsWith("LT(") || formula.startsWith("EQ(") || formula.startsWith("NOT(")){
                    FormulaEvaluatorHelper.evaluateComparison(evaluator, cell, parsedFormula);
                }else if(formula.startsWith("AND(") || formula.startsWith("OR(")){
                    FormulaEvaluatorHelper.evaluateLogical(evaluator, cell, parsedFormula);
                }else{
                    FormulaEvaluatorHelper.evaluateFormulaWithNumberValue(evaluator, cell, parsedFormula);
                }
            }
        } else if (formula.startsWith("CONCAT(")) {
            FormulaEvaluatorHelper.evaluateFormulaWithStringValue(evaluator, cell, formula);
        } else {
            cell.setCellFormula(formula);
        }
    }
    private static FormulaParser getParserForFormula( String formula ) {
        if (formula.startsWith("SUM(MULTIPLY(")) {
            return new SumMultiplyFormulaParser();
        } else if (formula.startsWith("MULTIPLY(")) {
            return new MultiplicationFormulaParser();
        } else if (formula.startsWith("DIVIDE(")) {
            return new DivisionFormulaParser();
        } else if (formula.startsWith("IF(")) {
            return new IfFormulaParser();
        } else if (formula.startsWith("GT(")) {
            return new GtFormulaParser();
        } else if (formula.startsWith("LT(")) {
            return new LtFormulaParser();
        } else if (formula.startsWith("EQ(")) {
            return new EqFormulaParser();
        } else if (formula.startsWith("NOT(")) {
            return new NotFormulaParser();
        } else if (formula.startsWith("AND(")) {
            return new AndFormulaParser();
        } else if (formula.startsWith("OR(")) {
            return new OrFormulaParser();
        }else {
            return null;
        }
    }
}
