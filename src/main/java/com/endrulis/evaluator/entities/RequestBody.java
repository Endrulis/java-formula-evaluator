package com.endrulis.evaluator.entities;

import lombok.Data;

import java.util.List;


@Data
public class RequestBody {
    private String email;
    private List<Sheet> results;

    public RequestBody( String email, List<Sheet> results) {
        this.email = email;
        this.results = results;
    }

}
