package com.endrulis.evaluator;

import com.endrulis.evaluator.services.SheetService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.endrulis.evaluator.Observer.start;

@SpringBootApplication
public class EvaluatorApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(EvaluatorApplication.class, args);
		RestTemplate restTemplate = new RestTemplate();
		SheetService sheetService = new SheetService(restTemplate);
		start(sheetService);
	}
	private static void example1() throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1");
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue(10);
		Cell cell2 = row.createCell(1);
		cell2.setCellFormula("A1 * 2");
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		CellValue cellValue = evaluator.evaluate(cell2);
		System.out.println("Evaluated result: " + cellValue.getNumberValue());
		workbook.close();
	}
}