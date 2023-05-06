package com.endrulis.evaluator.utils;

import org.junit.jupiter.api.Test;

import static com.endrulis.evaluator.utils.ConditionalFormulaParser.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormulaEvaluator {
    @Test
    public void testEvaluateGT() {
        String formula = "GT(5, 3)";
        String expected = "(5>3)*1";
        String result = evaluateGtOrLtOrEq(formula);
        assertEquals(expected, result);
    }
    @Test
    public void testEvaluateLT() {
        String formula = "LT(2, 4)";
        String expected = "(2<4)*1";
        String result = evaluateGtOrLtOrEq(formula);
        assertEquals(expected, result);
    }
    @Test
    public void testEvaluateEQ() {
        String formula = "EQ(2, 2)";
        String expected = "(2=2)*1";
        String result = evaluateGtOrLtOrEq(formula);
        assertEquals(expected, result);
    }
}
