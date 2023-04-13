package com.endrulis.evaluator.formula;

import org.apache.poi.ss.usermodel.*;

import java.util.List;

import static com.endrulis.evaluator.utils.FormulaEvaluatorUtils.*;

public class MyFormulaEvaluator {
    public static void evaluateFormulasInNewSheet( FormulaEvaluator evaluator, List<List<Object>> mySheetData, Sheet newSheet ) {
        for (int i = 0; i < mySheetData.size(); i++) {
            Row row = newSheet.getRow(i);
            for (int j = 0; j < mySheetData.get(i).size(); j++) {
                Cell cell = row.getCell(j);
                Object cellValue = mySheetData.get(i).get(j);
                if (cellValue instanceof String && ((String) cellValue).startsWith("=")) {
                    String formula = ((String) cellValue).substring(1);
                    evaluateFormulaInCell(evaluator, cell, formula);
                    if(formula.startsWith("AND(") || formula.startsWith("OR(")){
                        for(int k = 0; k < mySheetData.get(i).size(); k++){
                            if(k != j && row.getCell(k).getCellType() != cell.getCellType()){
                                cell.setCellType(CellType.STRING);
                                cell.setCellValue("#ERROR: type does not match");
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void evaluateFormulaInCell(FormulaEvaluator evaluator, Cell cell, String formula ) {
        if (formula.startsWith("MULTIPLY(")) {
            cell.setCellFormula(evaluateMultiply(formula));
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getNumberValue());
        }else if (formula.startsWith("DIVIDE(")) {
            cell.setCellFormula(evaluateDivision(formula));
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getNumberValue());
        }else if (formula.startsWith("GT(")) {
            cell.setCellFormula(evaluateGT(formula));
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getNumberValue());
            cell.setCellType(CellType.BOOLEAN);
        }else if (formula.startsWith("EQ(")) {
            cell.setCellFormula(evaluateEQ(formula));
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getNumberValue());
            cell.setCellType(CellType.BOOLEAN);
        }else if (formula.startsWith("NOT(")) {
            cell.setCellFormula(evaluateNOT(formula));
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getNumberValue());
            cell.setCellType(CellType.BOOLEAN);
        }else if(formula.startsWith("AND(")){
            cell.setCellFormula(evaluateAND(formula));
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getBooleanValue());
            cell.setCellType(CellType.BOOLEAN);
        }else if(formula.startsWith("OR(")){
            cell.setCellFormula(evaluateOR(formula));
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getBooleanValue());
            cell.setCellType(CellType.BOOLEAN);
        }else if (formula.startsWith("IF(")) {
            cell.setCellFormula(evaluateIF(formula));
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getNumberValue());
        }else if (formula.startsWith("CONCAT(")) {
            cell.setCellFormula(formula);
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getStringValue());
            cell.setCellType(CellType.STRING);
        }
        else {
            cell.setCellFormula(formula);
        }
    }
}
