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
import static com.endrulis.evaluator.formula.MyFormulaEvaluator.*;
import static com.endrulis.evaluator.utils.WorkbookUtils.*;
import static com.endrulis.evaluator.utils.FormulaEvaluatorUtils.*;

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
            if(count == 24) break;
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
}
