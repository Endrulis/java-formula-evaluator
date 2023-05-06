package com.endrulis.evaluator.formula.parsers;

import com.endrulis.evaluator.formula.FormulaParser;

public class SumMultiplyFormulaParser extends FormulaParser {
    @Override
    public String evaluateFormula( String formula){
        String[] args = formula.substring(formula.indexOf("(") + 1, formula.length() - 1).split(",(?![^()]*\\))");
        String condition = args[0].trim();
        String args1 = args[1].trim();
        StringBuilder sb = new StringBuilder();
        if (condition.startsWith("MULTIPLY(")) {
            String customCondition = new MultiplicationFormulaParser().evaluateFormula(condition);
            sb.append("SUM(").append(customCondition).append(",").append(args1).append(")");
        }
        return sb.toString();
    }
}
