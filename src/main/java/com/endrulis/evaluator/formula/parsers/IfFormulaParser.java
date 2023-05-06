package com.endrulis.evaluator.formula.parsers;

import com.endrulis.evaluator.formula.FormulaParser;

public class IfFormulaParser extends FormulaParser {
    @Override
    public String evaluateFormula( String formula ) {
        String[] args = formula.substring(formula.indexOf("(") + 1, formula.length() - 1).split(",(?![^()]*\\))");
        String condition = args[0].trim();
        String trueValue = args[1].trim();
        String falseValue = args[2].trim();
        StringBuilder sb = new StringBuilder();
        if (condition.startsWith("GT(")) {
            String customCondition = new GtFormulaParser().evaluateFormula(condition);
            sb.append("IF(").append(customCondition).append(",").append(trueValue).append(",").append(falseValue).append(")");
        }
        return sb.toString();
    }
}
