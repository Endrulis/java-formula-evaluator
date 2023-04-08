package com.endrulis.evaluator;

import com.endrulis.evaluator.entities.SpreadSheet;
import com.endrulis.evaluator.services.SheetService;

import java.util.Map;

public class Observer {
    public static void start( SheetService sheetService) throws Exception {
        try {
            System.out.println("Retrieving sheet data...");
            SpreadSheet responseData = sheetService.getSheetData();
            System.out.println("Successfully retrieved sheet data!");
            String submissionUrl = sheetService.getSubmissionUrl(responseData);
            sheetService.printSheetDetails(responseData);
            sheetService.postData(submissionUrl, responseData);
        } catch (Exception e) {
            System.out.println("Error occurred while retrieving sheet data: " + e.getMessage());
        }
    }
}
