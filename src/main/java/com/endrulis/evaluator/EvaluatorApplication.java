package com.endrulis.evaluator;

import com.endrulis.evaluator.services.SheetService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import static com.endrulis.evaluator.SpreadsheetObserver.start;

@SpringBootApplication
public class EvaluatorApplication {
    public static void main( String[] args ) throws Exception {
        SpringApplication.run(EvaluatorApplication.class, args);
        RestTemplate restTemplate = new RestTemplate();
        SheetService sheetService = new SheetService(restTemplate);
        start(sheetService);
    }
}