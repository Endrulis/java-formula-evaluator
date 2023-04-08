package com.endrulis.evaluator.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.Map;

public class SheetService {
    private static final String HUB_URL = "https://www.wix.com/_serverless/hiring-task-spreadsheet-evaluator";

    private final RestTemplate restTemplate;

    public SheetService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> getSheetData() throws Exception {
        Map<String, Object> response = restTemplate.getForObject(HUB_URL + "/sheets", Map.class);
        if (response != null) {
            return response;
        } else {
            throw new Exception("Failed to retrieve sheet data.");
        }
    }

    public void printSubmissionUrl(Map<String, Object> response) {
        String submissionUrl = (String) response.get("submissionUrl");
        System.out.println("Submission URL: " + submissionUrl);
    }

    public void printSheetDetails(Map<String, Object> response) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.valueToTree(response.get("sheets"));
        Iterator<JsonNode> sheets = jsonNode.elements();
        while (sheets.hasNext()) {
            JsonNode sheet = sheets.next();
            System.out.println(sheet.get("id"));
            System.out.println(sheet.get("data"));
        }
    }
}
