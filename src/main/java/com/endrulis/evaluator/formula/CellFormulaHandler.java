package com.endrulis.evaluator.formula;

import com.endrulis.evaluator.formula.parsers.*;
import com.endrulis.evaluator.utils.FormulaEvaluatorHelper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import java.util.HashMap;
import java.util.Map;


public class CellFormulaHandler {
    private static final Map<String, Class<? extends FormulaParser>> formulaParsers = new HashMap<>();
    static {
        formulaParsers.put("SUM(MULTIPLY(", SumMultiplyFormulaParser.class);
        formulaParsers.put("MULTIPLY(", MultiplicationFormulaParser.class);
        formulaParsers.put("DIVIDE(", DivisionFormulaParser.class);
        formulaParsers.put("IF(", IfFormulaParser.class);
        formulaParsers.put("GT(", GtFormulaParser.class);
        formulaParsers.put("LT(", LtFormulaParser.class);
        formulaParsers.put("EQ(", EqFormulaParser.class);
        formulaParsers.put("NOT(", NotFormulaParser.class);
        formulaParsers.put("AND(", AndFormulaParser.class);
        formulaParsers.put("OR(", OrFormulaParser.class);
        formulaParsers.put("SUM(", SumFormulaParser.class);
    }
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
    public static FormulaParser getParserForFormula(String formula) {
        for (String prefix : formulaParsers.keySet()) {
            if (formula.startsWith(prefix)) {
                try {
                    return formulaParsers.get(prefix).newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
