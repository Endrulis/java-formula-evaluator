package com.endrulis.evaluator.entities;

import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class RequestBody {
    private String email;
    private List<Map<String, Object>> results;

    public RequestBody(String email, List<Map<String, Object>> results) {
        this.email = email;
        this.results = results;
    }

}
