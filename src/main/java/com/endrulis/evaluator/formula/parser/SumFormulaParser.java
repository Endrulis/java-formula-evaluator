package com.endrulis.evaluator.formula.parser;

import com.endrulis.evaluator.formula.FormulaParser;

public class SumFormulaParser extends FormulaParser {
    @Override
    public String evaluateFormula(String formula) {
        String[] args = formula.substring(formula.indexOf("(") + 1, formula.length() - 1).split(",");
        StringBuilder result = new StringBuilder("(");
        for (int k = 0; k < args.length; k++) {
            result.append(args[k].trim());
            if (k < args.length - 1) {
                result.append("+");
            }
        }
        result.append(")*1");
        return result.toString();
    }
}
