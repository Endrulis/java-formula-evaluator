package com.endrulis.evaluator.formula.evaluators;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public interface Evaluator {
    void evaluate( FormulaEvaluator evaluator, Cell cell);
}
