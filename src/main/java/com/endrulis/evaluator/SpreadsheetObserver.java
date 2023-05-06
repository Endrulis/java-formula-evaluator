package com.endrulis.evaluator;

import com.endrulis.evaluator.entities.ExcelSheet;
import com.endrulis.evaluator.entities.SpreadSheet;
import com.endrulis.evaluator.services.SheetService;

import java.util.List;

public class SpreadsheetObserver {
    public static void start( SheetService sheetService ) throws Exception {
        System.out.println("Retrieving sheet data...");
        SpreadSheet responseData = sheetService.fetchSheetData();
        System.out.println("Successfully retrieved sheet data!");
        String submissionUrl = sheetService.getSubmissionUrl(responseData);
        sheetService.displaySheetDetails(responseData);
        List<ExcelSheet> updatedSheets = sheetService.updateSpreadSheet(responseData);
        sheetService.postData(submissionUrl, updatedSheets);
    }
}
