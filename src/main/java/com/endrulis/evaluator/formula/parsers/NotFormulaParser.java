package com.endrulis.evaluator.formula.parsers;

import com.endrulis.evaluator.formula.FormulaParser;

public class NotFormulaParser extends FormulaParser {
    @Override
    public String evaluateFormula( String formula ) {
        String arg = formula.substring(formula.indexOf("(") + 1, formula.length() - 1).trim();
        return "NOT(" + arg.trim() + ")*1";
    }
}
