package com.endrulis.evaluator.utils;

import com.endrulis.evaluator.formula.parsers.GtFormulaParser;
import com.endrulis.evaluator.formula.parsers.MultiplicationFormulaParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormulaEvaluator {
    @Test
    public void testEvaluateGT() {
        String formula = "GT(5, 3)";
        String expected = "(5>3)*1";
        String result = new GtFormulaParser().evaluateFormula(formula);
        assertEquals(expected, result);
    }
    @Test
    public void testEvaluateLT() {
        String formula = "LT(2, 4)";
        String expected = "(2<4)*1";
        String result = new GtFormulaParser().evaluateFormula(formula);
        assertEquals(expected, result);
    }
    @Test
    public void testEvaluateEQ() {
        String formula = "EQ(2, 2)";
        String expected = "(2=2)*1";
        String result = new GtFormulaParser().evaluateFormula(formula);
        assertEquals(expected, result);
    }
    @Test
    void testMultiplicationFormulaWithNestedFormulas() {
        String formula = "MULTIPLY(SUM(2, 3), MULTIPLY(4, 5), MULTIPLY(4, 5))";
        MultiplicationFormulaParser parser = new MultiplicationFormulaParser();
        String expected = "(((2+3)*1)*((4*5)*1)*((4*5)*1))*1";
        String actual = parser.evaluateFormula(formula);
        assertEquals(expected, actual);
    }
}
