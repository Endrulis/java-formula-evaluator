package com.endrulis.evaluator.formula.parser;

import com.endrulis.evaluator.formula.FormulaParser;

public class GtFormulaParser extends FormulaParser {
    @Override
    public String evaluateFormula(String formula) {
        String[] args = formula.substring(formula.indexOf("(") + 1, formula.length() - 1).split(",");
        return "(" + args[0].trim() + ">" + args[1].trim() + ")*1";
    }
}
