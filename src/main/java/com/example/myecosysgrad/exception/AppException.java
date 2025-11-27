package com.example.myecosysgrad.exception;

import lombok.*;

// @EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AppException extends RuntimeException {

    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
