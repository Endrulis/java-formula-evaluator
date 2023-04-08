package com.endrulis.evaluator.entities;

import lombok.Data;

import java.util.List;

@Data
public class Sheet {
    private String id;
    private List<List<Object>> data;

}
