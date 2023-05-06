package com.endrulis.evaluator.formula.parsers;

import com.endrulis.evaluator.formula.FormulaParser;

public class OrFormulaParser extends FormulaParser {
    @Override
    public String evaluateFormula( String formula ) {
        String[] args = formula.substring(formula.indexOf("(") + 1, formula.length() - 1).split(",");
        StringBuilder sb = new StringBuilder();
        sb.append("OR(");
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i].trim());
            if (i < args.length - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
