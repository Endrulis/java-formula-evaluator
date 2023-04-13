package com.endrulis.evaluator.utils;

public class FormulaEvaluatorUtils {
    public static String evaluateIF(String formula) {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
    public static String evaluateAND( String formula ) {
        String[] args = formula.substring(4, formula.length() - 1).split(",");
        StringBuilder sb = new StringBuilder();
        sb.append("AND(");
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i].trim());
            if (i < args.length - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }
    public static String evaluateNOT( String formula ) {
        String arg = formula.substring(4, formula.length() - 1).trim();
        StringBuilder sb = new StringBuilder();
        sb.append("(NOT(").append(arg).append("))*1");
        return sb.toString();
    }

    public static String evaluateEQ( String formula ) {
        String[] args = formula.substring(3, formula.length() - 1).split(",");
        String arg1 = args[0].trim();
        String arg2 = args[1].trim();
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(arg1).append("=").append(arg2).append(")*1");
        return sb.toString();
    }

    public static String evaluateGT( String formula ) {
        System.out.println(formula);
        String[] args = formula.substring(3, formula.length() - 1).split(",");
        String arg1 = args[0].trim();
        String arg2 = args[1].trim();
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(arg1).append(">").append(arg2).append(")*1");
        return sb.toString();
    }

    public static String evaluateDivision( String formula ) {
        String[] args = formula.substring(7, formula.length() - 1).split(",");
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < args.length; k++) {
            if (k > 0) {
                sb.append("/");
            }
            sb.append(args[k].trim());
        }
        return sb.toString();
    }

    public static String evaluateMultiply( String formula ) {
        String[] args = formula.substring(9, formula.length() - 1).split(",");
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < args.length; k++) {
            if (k > 0) {
                sb.append("*");
            }
            sb.append(args[k].trim());
        }
        return sb.toString();
    }
}
