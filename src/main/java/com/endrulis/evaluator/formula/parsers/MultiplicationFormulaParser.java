package com.endrulis.evaluator.formula.parsers;

import com.endrulis.evaluator.formula.CellFormulaHandler;
import com.endrulis.evaluator.formula.FormulaParser;

import java.util.ArrayList;
import java.util.List;

public class MultiplicationFormulaParser extends FormulaParser {
    @Override
    public String evaluateFormula( String formula ) {
        formula = formula.substring(formula.indexOf("(") + 1, formula.length() - 1);
        List<String> args = splitArguments(formula);
        StringBuilder result = new StringBuilder("(");
        for (int k = 0; k < args.size(); k++) {
            String arg = args.get(k).trim();
            if (arg.contains("(") && arg.contains(")")) {
                FormulaParser formulaParser = CellFormulaHandler.getParserForFormula(arg);
                if(formulaParser!= null)
                    arg = "(" + formulaParser.evaluateFormula(arg) + ")";
            }
            result.append(arg);
            if (k < args.size() - 1) {
                result.append("*");
            }
        }
        result.append(")*1");
        return result.toString();
    }

    private List<String> splitArguments( String formula ) {
        List<String> args = new ArrayList<>();
        int depth = 0;
        int start = 0;
        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (c == ',' && depth == 0) {
                args.add(formula.substring(start, i));
                start = i + 1;
            } else if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
            }
        }
        args.add(formula.substring(start));
        return args;
    }
}
