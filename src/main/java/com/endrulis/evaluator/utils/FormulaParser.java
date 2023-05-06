package com.endrulis.evaluator.utils;

public class FormulaParser {
    public static String parseFormula( String cellValue) {
        if (cellValue.startsWith("=")) {
            return cellValue.substring(1);
        }
        return null;
    }
}
