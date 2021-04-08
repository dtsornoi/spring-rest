package com.rest.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class CustomerErrorResponse {
    private int status;
    private String message;
    private long timeStamp;
}
