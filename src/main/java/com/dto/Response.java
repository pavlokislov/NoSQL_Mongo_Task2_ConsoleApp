package com.dto;

import com.util.ObjectMapperUtil;
import lombok.Data;

import java.util.List;

@Data
public class Response {

    private List<?> result;
    private Status status;

    public Response(boolean status) {
        this.status = Status.parse(status);
        this.result = null;
    }

    public Response(List<?> result) {
        this.result = result;
        this.status = Status.SUCCESS;
    }

    public void printResponse() {
        if (result != null) {
            System.out.println("Execution was %s".formatted(this.status));
            result.stream()
                    .map(ObjectMapperUtil::toJson)
                    .forEach(System.out::println);
        } else {
            System.out.println("Execution was %s".formatted(this.status) );
        }
    }
}
