package com.endrulis.evaluator.services;

import com.endrulis.evaluator.entities.RequestBody;
import com.endrulis.evaluator.entities.ExcelSheet;
import com.endrulis.evaluator.entities.SpreadSheet;
import com.endrulis.evaluator.formula.ExcelFormulaEvaluator;
import com.endrulis.evaluator.utils.WorkbookUtils;
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

    public SheetService( RestTemplate restTemplate ) {
        this.restTemplate = restTemplate;
    }

    public SpreadSheet fetchSheetData() throws Exception {
        SpreadSheet spreadSheet = restTemplate.getForObject(CONST_HUB_URL + "/sheets/?tag=nested_structures", SpreadSheet.class);
        if (spreadSheet != null) {
            return spreadSheet;
        } else {
            throw new Exception("Failed to retrieve sheet data.");
        }
    }

    public String getSubmissionUrl( SpreadSheet responseData ) {
        String submissionUrl = responseData.getSubmissionUrl();
        System.out.println("Submission URL: " + submissionUrl);
        return submissionUrl;
    }

    public void displaySheetDetails( SpreadSheet spreadSheet ) {
        List<ExcelSheet> sheets = spreadSheet.getSheets();
        for (ExcelSheet sheet : sheets) {
            System.out.println(sheet.getId());
            System.out.println(sheet.getData());
        }
    }

    public List<ExcelSheet> updateSpreadSheet( SpreadSheet spreadSheet ) {
        Workbook workbook = new XSSFWorkbook();
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        List<ExcelSheet> updatedSheets = new ArrayList<>();
        for (ExcelSheet excelSheet : spreadSheet.getSheets()) {
            Sheet newSheet = createNewSheet(workbook, excelSheet);
            WorkbookUtils.fillNewSheetWithData(newSheet, excelSheet.getData());
            ExcelFormulaEvaluator.evaluateFormulasInNewSheet(evaluator, excelSheet.getData(), newSheet);
            List<List<Object>> updatedSheetData = WorkbookUtils.getUpdatedSheetData(evaluator, newSheet);
            ExcelSheet updatedSheet = new ExcelSheet(excelSheet.getId(), updatedSheetData);
            updatedSheets.add(updatedSheet);
        }
        return updatedSheets;
    }

    public void postData( String submissionUrl, List<ExcelSheet> updatedSheets ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestBody requestBody = new RequestBody(CONST_EMAIL, updatedSheets);
        System.out.println("Request Body: " + requestBody);
        HttpEntity<RequestBody> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(submissionUrl, requestEntity, String.class);
        System.out.println("Response from server: " + responseEntity.getBody());
    }
}
