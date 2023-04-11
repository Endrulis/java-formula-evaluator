package com.endrulis.evaluator.services;

import com.endrulis.evaluator.entities.RequestBody;
import com.endrulis.evaluator.entities.MySheet;
import com.endrulis.evaluator.entities.SpreadSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Console;
import java.util.*;

import static com.endrulis.evaluator.constants.AppConstants.*;

public class SheetService {

    private final RestTemplate restTemplate;

    public SheetService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SpreadSheet getSheetData() throws Exception {
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

    public void printSheetDetails(SpreadSheet spreadSheet){
        List<MySheet> sheets = spreadSheet.getSheets();
        for (MySheet sheet : sheets) {
            System.out.println(sheet.getId());
            System.out.println(sheet.getData());
        }
    }
    public void postData(String submissionUrl, SpreadSheet spreadSheet) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<MySheet> updatedSheets = new ArrayList<>();
        for(MySheet sheet : spreadSheet.getSheets()){
            MySheet updatedSheet = new MySheet(sheet.getId(), evaluateSheet(sheet.getData()));
            System.out.println(sheet.getData());
            updatedSheets.add(updatedSheet);
        }
        RequestBody requestBody = new RequestBody(CONST_EMAIL, updatedSheets);
        System.out.println("Request Body: " + requestBody);
        HttpEntity<RequestBody> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(submissionUrl, requestEntity, String.class);
        System.out.println("Response from server: " + responseEntity.getBody());
    }

    private List<List<Object>> evaluateSheet(List<List<Object>> data) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        for(int i = 0; i < data.size(); i++) {
            XSSFRow row = sheet.createRow(i);
            for(int j = 0; j < data.get(i).size(); j++) {
                XSSFCell cell = row.createCell(j);
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
                }
            }
            result.add(rowValues);
        }
        return result;
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
}
