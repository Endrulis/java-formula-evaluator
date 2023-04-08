package com.endrulis.evaluator.entities;

import lombok.Data;

import java.util.List;

@Data
public class SpreadSheet {
    private String submissionUrl;
    private List<Sheet> sheets;
}
