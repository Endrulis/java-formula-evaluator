package com.endrulis.evaluator.services;

import com.endrulis.evaluator.entities.RequestBody;
import com.endrulis.evaluator.entities.MySheet;
import com.endrulis.evaluator.entities.SpreadSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.endrulis.evaluator.constants.AppConstants.*;
import static com.endrulis.evaluator.utils.WorkbookUtils.*;

public class SheetService {

    private final RestTemplate restTemplate;

    public SheetService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SpreadSheet fetchSheetData() throws Exception {
        SpreadSheet spreadSheet = restTemplate.getForObject(CONST_HUB_URL + "/sheets", SpreadSheet.class);
        if (spreadSheet != null) {
            return spreadSheet;
        } else {
            throw new Exception("Failed to retrieve sheet data.");
        }
    }

    public String getSubmissionUrl( SpreadSheet responseData) {
        String submissionUrl = responseData.getSubmissionUrl();
        System.out.println("Submission URL: " + submissionUrl);
        return submissionUrl;
    }

    public void displaySheetDetails( SpreadSheet spreadSheet){
        List<MySheet> sheets = spreadSheet.getSheets();
        for (MySheet sheet : sheets) {
            System.out.println(sheet.getId());
            System.out.println(sheet.getData());
        }
    }
    public void updateSpreadsheetAndPostData( String submissionUrl, SpreadSheet spreadSheet) {
        List<MySheet> updatedSheets = updateSpreadSheet(spreadSheet);
        postData(submissionUrl, updatedSheets);
    }
    private List<MySheet> updateSpreadSheet( SpreadSheet spreadSheet ) {
        Workbook workbook = new XSSFWorkbook();
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        List<MySheet> updatedSheets = new ArrayList<>();
        int count = 0;
        for (MySheet mySheet : spreadSheet.getSheets()) {
            Sheet newSheet = createNewSheet(workbook, mySheet);
            fillNewSheetWithData(newSheet, mySheet.getData());
            evaluateFormulasInNewSheet(evaluator, mySheet.getData(), newSheet);
            List<List<Object>> updatedSheetData = getUpdatedSheetData(evaluator, newSheet);
            MySheet updatedSheet = new MySheet(mySheet.getId(), updatedSheetData);
            updatedSheets.add(updatedSheet);
            count++;
            if(count == 16) break;
        }
        return updatedSheets;
    }
    private void postData( String submissionUrl, List<MySheet> updatedSheets ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestBody requestBody = new RequestBody(CONST_EMAIL, updatedSheets);
        System.out.println("Request Body: " + requestBody);
        HttpEntity<RequestBody> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(submissionUrl, requestEntity, String.class);
        System.out.println("Response from server: " + responseEntity.getBody());
    }



    private static Sheet createNewSheet( Workbook workbook, MySheet mySheet ) {
        Sheet newSheet = workbook.createSheet(mySheet.getId());
        return newSheet;
    }


    private void fillNewSheetWithData(Sheet newSheet, List<List<Object>> mySheetData) {
        for (int i = 0; i < mySheetData.size(); i++) {
            Row row = newSheet.createRow(i);
            for (int j = 0; j < mySheetData.get(i).size(); j++) {
                Cell cell = row.createCell(j);
                Object cellValue = mySheetData.get(i).get(j);
                if (cellValue instanceof Integer) {
                    cell.setCellValue((Integer) cellValue);
                } else if (cellValue instanceof Boolean) {
                    cell.setCellValue((Boolean) cellValue);
                } else if (cellValue instanceof String) {
                    cell.setCellValue(cellValue.toString());
                } else {
                    cell.setCellValue(Double.parseDouble(mySheetData.get(i).get(j).toString()));
                }
            }
        }
    }
    private void evaluateFormulasInNewSheet( FormulaEvaluator evaluator, List<List<Object>> mySheetData, Sheet newSheet ) {
        for (int i = 0; i < mySheetData.size(); i++) {
            Row row = newSheet.getRow(i);
            for (int j = 0; j < mySheetData.get(i).size(); j++) {
                Cell cell = row.getCell(j);
                Object cellValue = mySheetData.get(i).get(j);
                if (cellValue instanceof String && ((String) cellValue).startsWith("=")) {
                    String formula = ((String) cellValue).substring(1);
                    evaluateFormulaInCell(evaluator, cell, formula);
                }
            }
        }
    }

    private void evaluateFormulaInCell( FormulaEvaluator evaluator, Cell cell, String formula ) {
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
            System.out.println(cell.getCellFormula());
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getBooleanValue());
            cell.setCellType(CellType.BOOLEAN);
        }else if (formula.startsWith("IF(")) {
            cell.setCellFormula(evaluateIF(formula));
        }
        else {
            cell.setCellFormula(formula);
            CellValue formulaValue = evaluator.evaluate(cell);
            cell.setCellValue(formulaValue.getNumberValue());
        }
    }
    private String evaluateIF(String formula) {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
    private String evaluateAND( String formula ) {
        String[] args = formula.substring(4, formula.length() - 1).split(",");
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i].trim());
            if (i < args.length - 1) {
                sb.append("&");
            }
        }
        sb.append(")*1");
        return sb.toString();
    }
    private static String evaluateNOT( String formula ) {
        String arg = formula.substring(4, formula.length() - 1).trim();
        StringBuilder sb = new StringBuilder();
        sb.append("(NOT(").append(arg).append("))*1");
        return sb.toString();
    }

    private static String evaluateEQ( String formula ) {
        String[] args = formula.substring(3, formula.length() - 1).split(",");
        String arg1 = args[0].trim();
        String arg2 = args[1].trim();
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(arg1).append("=").append(arg2).append(")*1");
        return sb.toString();
    }

    private static String evaluateGT( String formula ) {
        System.out.println(formula);
        String[] args = formula.substring(3, formula.length() - 1).split(",");
        String arg1 = args[0].trim();
        String arg2 = args[1].trim();
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(arg1).append(">").append(arg2).append(")*1");
        return sb.toString();
    }

    private String evaluateDivision( String formula ) {
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

    private String evaluateMultiply( String formula ) {
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
    private List<List<Object>> evaluateSheet(List<List<Object>> data) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        for(int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i);
            for(int j = 0; j < data.get(i).size(); j++) {
                Cell cell = row.createCell(j);
                if(data.get(i).get(j) instanceof String && ((String) data.get(i).get(j)).startsWith("=")) {
                    String formula = ((String) data.get(i).get(j)).substring(1);
                    System.out.println(formula);
                    if (formula.startsWith("MULTIPLY(")) {
                        cell.setCellFormula(evaluateMultiply(formula));
                    } else if (formula.startsWith("DIVIDE(")) {
                        cell.setCellFormula(evaluateDivision(formula));
                    } else if (formula.startsWith("GT(")) {
                        cell.setCellFormula(evaluateGT(formula));
                    } else if (formula.startsWith("EQ(")) {
                        cell.setCellFormula(evaluateEQ(formula));
                    } else if (formula.startsWith("NOT(")) {
                        cell.setCellFormula(evaluateNOT(formula));
                    }else if (formula.startsWith("IF(")) {
                        String[] args = formula.substring(3, formula.length() - 1).split(",(?![^()]*\\))");
                        String condition = args[0].trim();
                        String trueValue = args[1].trim();
                        String falseValue = args[2].trim();
                        StringBuilder sb = new StringBuilder();
                        if (condition.startsWith("GT(")) {
                            String customCondition = evaluateGT(condition);
                            sb.append("IF(").append(customCondition).append(",").append(trueValue).append(",").append(falseValue).append(")");
                        }
                        System.out.println(sb.toString());
                        cell.setCellFormula(sb.toString());
                    }else if (formula.startsWith("CONCAT(")) {
                        System.out.println("\"Hello\", \", \", \"World!\"");
                        String[] args = formula.substring(7, formula.length() - 1).split(",");
                        StringBuilder sb = new StringBuilder();
                        for (String arg : args) {
                            if (arg.startsWith("\"")) {
                                sb.append(arg.substring(1, arg.length() - 1));
                            }
                        }
                    }
                    else {
                        cell.setCellFormula(formula);
                    }
                }else if(data.get(i).get(j) instanceof Boolean){
                    cell.setCellValue(Boolean.parseBoolean(data.get(i).get(j).toString()));
                }else if(data.get(i).get(j) instanceof String){
                    cell.setCellValue(data.get(i).get(j).toString());
                }
                else {
                    cell.setCellValue(Double.parseDouble(data.get(i).get(j).toString()));
                }
            }
        }
        evaluator.evaluateAll();
        List<List<Object>> result = new ArrayList<>();
        for(int i = 0; i < data.size(); i++) {
            Row row = sheet.getRow(i);
            List<Object> rowValues = new ArrayList<>();
            for(int j = 0; j < data.get(i).size(); j++) {
                Cell cell = row.getCell(j);
                if(cell.getCellType() == CellType.NUMERIC) {
                    rowValues.add(cell.getNumericCellValue());
                } else if(cell.getCellType() == CellType.STRING) {
                    rowValues.add(cell.getStringCellValue());
                } else if(cell.getCellType() == CellType.BOOLEAN) {
                    rowValues.add(cell.getBooleanCellValue());
                }else if(cell.getCellType() == CellType.FORMULA){
                    rowValues.add((evaluator.evaluateFormulaCell(cell)));
                }
            }
            result.add(rowValues);
        }
        return result;
    }
}
