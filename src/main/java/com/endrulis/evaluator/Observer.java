package com.endrulis.evaluator;

import com.endrulis.evaluator.services.SheetService;

import java.util.Map;

public class Observer {
    public static void testConnection( SheetService sheetService) throws Exception {
        try {
            System.out.println("Retrieving sheet data...");
            Map<String, Object> response = sheetService.getSheetData();
            System.out.println("Successfully retrieved sheet data!");
            sheetService.printSubmissionUrl(response);
            sheetService.printSheetDetails(response);
        } catch (Exception e) {
            System.out.println("Error occurred while retrieving sheet data: " + e.getMessage());
        }
    }
}
