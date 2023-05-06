package com.endrulis.evaluator.formula.parser;

import com.endrulis.evaluator.formula.FormulaParser;

public class DivisionFormulaParser extends FormulaParser {
    public String evaluateFormula(String formula) {
        String[] args = formula.substring(formula.indexOf("(") + 1, formula.length() - 1).split(",");
        StringBuilder result = new StringBuilder(args[0].trim());
        for (int k = 1; k < args.length; k++) {
            result.append("/").append(args[k].trim());
        }
        return result.toString();
    }
}
