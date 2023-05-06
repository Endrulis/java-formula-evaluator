package com.endrulis.evaluator.utils;

public class ConditionalFormulaParser {
    public static String evaluateIf( String formula) {
        String[] args = formula.substring(formula.indexOf("(") + 1, formula.length() - 1).split(",(?![^()]*\\))");
        String condition = args[0].trim();
        String trueValue = args[1].trim();
        String falseValue = args[2].trim();
        StringBuilder sb = new StringBuilder();
        if (condition.startsWith("GT(") ||
                condition.startsWith("LT(") || condition.startsWith("EQ(")) {
            String customCondition = evaluateGtOrLtOrEq(condition);
            sb.append("IF(").append(customCondition).append(",").append(trueValue).append(",").append(falseValue).append(")");
        }
        return sb.toString();
    }

    public static String evaluateAndOrOr( String formula ) {
        String[] args = formula.substring(formula.indexOf("(") + 1, formula.length() - 1).split(",");
        String operator = (formula.startsWith("AND") ? "AND(" : "OR(");
        StringBuilder sb = new StringBuilder();
        sb.append(operator);
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i].trim());
            if (i < args.length - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }
    public static String evaluateNot( String formula ) {
        String arg = formula.substring(formula.indexOf("(") + 1, formula.length() - 1).trim();
        return "NOT(" + arg.trim() + ")*1";
    }
    public static String evaluateGtOrLtOrEq( String formula ) {
        String[] args = formula.substring(formula.indexOf("(") + 1, formula.length() - 1).split(",");
        String operator =
                (formula.startsWith("GT")) ? ">" :
                        (formula.startsWith("LT")) ? "<" : "=";
        return "(" + args[0].trim() + operator + args[1].trim() + ")*1";
    }
    public static String evaluateMultiplyOrDivision( String formula) {
        String[] args = formula.substring(formula.indexOf("(") + 1, formula.length() - 1).split(",");
        String operator = (formula.startsWith("MULTIPLY") ? "*" : "/");
        StringBuilder result = new StringBuilder(args[0].trim());
        for (int k = 1; k < args.length; k++) {
            result.append(operator).append(args[k].trim());
        }
        return result.toString();
    }
    public static String evaluateSum(String formula){
        String[] args = formula.substring(formula.indexOf("(") + 1, formula.length() - 1).split(",(?![^()]*\\))");
        String condition = args[0].trim();
        String args1 = args[1].trim();
        StringBuilder sb = new StringBuilder();
        if (condition.startsWith("MULTIPLY(")) {
            String customCondition = evaluateMultiplyOrDivision(condition);
            sb.append("SUM(").append(customCondition).append(",").append(args1).append(")");
        }
        return sb.toString();
    }
}
