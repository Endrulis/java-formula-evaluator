package com.endrulis.evaluator.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MySheet {
    private String id;
    private List<List<Object>> data;

}
