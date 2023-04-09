package com.endrulis.evaluator.entities;

import lombok.Data;

import java.util.List;


@Data
public class RequestBody {
    private String email;
    private List<MySheet> results;

    public RequestBody( String email, List<MySheet> results) {
        this.email = email;
        this.results = results;
    }

}
