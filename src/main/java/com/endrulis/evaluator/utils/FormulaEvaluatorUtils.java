package com.endrulis.evaluator.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class FormulaEvaluatorUtils {
    public static String evaluateIF(String formula) {
        String[] args = formula.substring(3, formula.length() - 1).split(",(?![^()]*\\))");
        String condition = args[0].trim();
        String trueValue = args[1].trim();
        String falseValue = args[2].trim();
        StringBuilder sb = new StringBuilder();
        if (condition.startsWith("GT(")) {
            String customCondition = evaluateGT(condition);
            sb.append("IF(").append(customCondition).append(",").append(trueValue).append(",").append(falseValue).append(")");
        }
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
    public static String evaluateOR( String formula ) {
        String[] args = formula.substring(3, formula.length() - 1).split(",");
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
