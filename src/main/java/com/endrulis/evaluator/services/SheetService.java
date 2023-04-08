package com.endrulis.evaluator.services;

import com.endrulis.evaluator.entities.RequestBody;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public Map<String, Object> getSheetData() throws Exception {
        Map<String, Object> response = restTemplate.getForObject(CONST_HUB_URL + "/sheets", Map.class);
        if (response != null) {
            return response;
        } else {
            throw new Exception("Failed to retrieve sheet data.");
        }
    }

    public String getSubmissionUrl( Map<String, Object> responseData) {
        String submissionUrl = (String) responseData.get("submissionUrl");
        System.out.println("Submission URL: " + submissionUrl);
        return submissionUrl;
    }

    public void printSheetDetails(Map<String, Object> responseData) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.valueToTree(responseData.get("sheets"));
        Iterator<JsonNode> sheets = jsonNode.elements();
        while (sheets.hasNext()) {
            JsonNode sheet = sheets.next();
            System.out.println(sheet.get("id"));
            System.out.println(sheet.get("data"));
        }
    }
    public void postData(String submissionUrl, Map<String, Object> responseData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Map<String, Object>> sheets = (List<Map<String, Object>>) responseData.get("sheets");

        RequestBody requestBody = new RequestBody(CONST_EMAIL, sheets);
        System.out.println("Request Body: " + requestBody);
        HttpEntity<RequestBody> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(submissionUrl, requestEntity, String.class);
        System.out.println("Response from server: " + responseEntity.getBody());
    }
}
