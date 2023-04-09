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
            updatedSheets.add(updatedSheet);
        }
        RequestBody requestBody = new RequestBody(CONST_EMAIL, spreadSheet.getSheets());
        System.out.println("Request Body: " + requestBody);
        HttpEntity<RequestBody> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(submissionUrl, requestEntity, String.class);
        System.out.println("Response from server: " + responseEntity.getBody());
    }

    private List<List<Object>> evaluateSheet(List<List<Object>> data) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        for(int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i);
            for(int j = 0; j < data.get(i).size(); j++) {
                Cell cell = row.createCell(j);
                if(data.get(i).get(j) instanceof String && ((String) data.get(i).get(j)).startsWith("=")) {
                    cell.setCellFormula(((String) data.get(i).get(j)).substring(1));
                } else {
                    cell.setCellValue(Double.parseDouble(data.get(i).get(j).toString()));
                }
            }
        }
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
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
}
