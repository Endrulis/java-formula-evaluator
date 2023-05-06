package com.endrulis.evaluator.entities;

import lombok.Data;

import java.util.List;


@Data
public class RequestBody {
    private String email;
    private List<ExcelSheet> results;

    public RequestBody( String email, List<ExcelSheet> results) {
        this.email = email;
        this.results = results;
    }

}
