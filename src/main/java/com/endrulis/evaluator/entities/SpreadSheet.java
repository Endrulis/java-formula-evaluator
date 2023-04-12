package com.endrulis.evaluator.entities;

import lombok.Data;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

@Data
public class SpreadSheet {
    private String submissionUrl;
    private List<MySheet> sheets;
}
